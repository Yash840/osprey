package org.cross.osprey.http;

import org.cross.osprey.core.ExecutionPattern;
import org.cross.osprey.core.Job;
import org.cross.osprey.core.RetryPolicy;
import org.cross.osprey.share.NewJobDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JobMapper {
    public Job toJob(NewJobDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("job request must not be null");
        }
        if (dto.type() == null || dto.type().isBlank()) {
            throw new IllegalArgumentException("job type must not be blank");
        }

        LocalDateTime scheduledAt = dto.scheduledAt() == null
                ? LocalDateTime.now()
                : dto.scheduledAt();
        LocalDateTime nextRunAt = dto.nextRunAt() == null
                ? scheduledAt
                : dto.nextRunAt();

        return new Job(
                dto.type(),
                dto.payload(),
                scheduledAt,
                parseExecutionPattern(dto.executionPattern()),
                nextRunAt,
                valueOrDefault(dto.totalAttempts(), 1),
                valueOrDefault(dto.maxRetries(), 0),
                parseRetryPolicy(dto.retryPolicy()),
                LocalDateTime.now(),
                valueOrDefault(dto.priority(), 0)
        );
    }

    private ExecutionPattern parseExecutionPattern(String value) {
        return value == null || value.isBlank()
                ? ExecutionPattern.INSTANT
                : ExecutionPattern.fromString(value);
    }

    private RetryPolicy parseRetryPolicy(String value) {
        return value == null || value.isBlank()
                ? RetryPolicy.NONE
                : RetryPolicy.fromString(value);
    }

    private int valueOrDefault(Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }
}
