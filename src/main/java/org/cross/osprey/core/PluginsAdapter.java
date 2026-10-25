package org.cross.osprey.core;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cross.osprey.config.PluginProperties;
import org.springframework.stereotype.Component;
import osprey.plugin.spi.JobExecutor;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class PluginsAdapter {
    private final PluginProperties properties;
    private Map<String, JobExecutor> pluginRegistry;

    @PostConstruct
    public void loadPlugins() {
        log.info("Loading execution plugins...");

        try (var pluginLoader = new PluginLoader()) {
            pluginLoader.loadPlugins(properties.directory());

            pluginRegistry = pluginLoader.getPluginRegistry();
        } catch (Exception ex) {

        }
    }
}
