package org.cross.osprey.share;

import java.time.LocalDateTime;

public record NewJobDto(
        String type,
        String payload,
        LocalDateTime scheduledAt,
        String executionPattern,
        LocalDateTime nextRunAt,
        Integer totalAttempts,
        Integer maxRetries,
        String retryPolicy,
        Integer priority
) {
}
