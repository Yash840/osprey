package org.cross.osprey.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "osprey.plugins")
public record PluginProperties (Path directory) {}
