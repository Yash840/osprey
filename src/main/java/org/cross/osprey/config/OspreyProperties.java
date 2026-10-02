package org.cross.osprey.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "osprey")
public record OspreyProperties(
        int maxWorkers,
        int queueCapacity
) {
}
