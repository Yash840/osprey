package org.cross.osprey.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osprey.plugin.spi.JobExecutor;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PluginLoader implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(PluginLoader.class);
    private final Map<String, JobExecutor> pluginRegistry = new HashMap<>();
    private final List<URLClassLoader> classLoaders = new ArrayList<>();

    public void loadPlugins(Path dirPath) {
        if (!Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException(
                    "Plugin directory does not exist: " + dirPath
            );
        }

        try (var files = Files.list(dirPath)) {
            List<Path> jars = files
                    .filter(Files::isRegularFile)
                    .filter(jar -> jar.endsWith(".jar"))
                    .sorted()
                    .toList();

            int loaded = 0;

            for (var jar : jars) {
                loadPlugin(jar);
                loaded++;
            }

            log.info("Plugins Loader: total {} plugins from {} jars", loaded, jars.size());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadPlugin(Path jar) {
        URLClassLoader loader = null;

        try {
            loader = new URLClassLoader(
                    new URL[] {jar.toUri().toURL()},
                    JobExecutor.class.getClassLoader()
            );

            ServiceLoader<JobExecutor> services = ServiceLoader.load(JobExecutor.class, loader);

            boolean discovered = false;

            for (JobExecutor executor : services) {
                discovered = true;

                String type = executor.type();

                if (type == null || type.isBlank()) {
                    throw new IllegalStateException( "Plugin declares an empty job type: " + jar);
                }

                if (pluginRegistry.containsKey(type)) {
                    throw new IllegalStateException(
                            "Duplicate plugin job type: " + type
                    );
                }

                pluginRegistry.put(type, executor);
                log.info("Loaded {} plugin from {}", type, jar);
            }

            if (!discovered) {
                throw new IllegalStateException(
                        "No JobExecutor provider found in: " + jar
                );
            }

            classLoaders.add(loader);

        } catch (Exception | ServiceConfigurationError e) {
            try {
                if (loader != null) {
                    loader.close();
                }
            } catch (IOException ex) {
                e.addSuppressed(ex);
            }

            throw new IllegalStateException(
                    "Failed to load plugin JAR: " + jar, e
            );
        }
    }

    public Map<String, JobExecutor> getPluginRegistry() {
        return Map.copyOf(pluginRegistry);
    }


    @Override
    public void close() throws Exception {
        IOException failure = null;

        for (URLClassLoader loader : classLoaders) {
            try {
                loader.close();
            } catch (IOException e) {
                if (failure == null) {
                    failure = e;
                } else {
                    failure.addSuppressed(e);
                }
            }
        }

        classLoaders.clear();

        if (failure != null) {
            throw failure;
        }
    }
}
