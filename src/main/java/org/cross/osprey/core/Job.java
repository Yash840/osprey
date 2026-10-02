package org.cross.osprey.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cross.osprey.utils.IdGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String type;

    @Setter
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "execution_pattern")
    private ExecutionPattern executionPattern;

    @Column(name = "next_run_at")
    private LocalDateTime nextRunAt;

    private int priority;

    // Current execution number, used for avoiding stale worker progress (as Fence)
    @Setter
    private int attempt;

    // Total number of times Job is executed
    @Setter
    @Column(name = "total_attempts")
    private int totalAttempts;

    // Job executions when Job is failed
    @Setter
    private int retries;

    @Column(name = "max_retries")
    private int maxRetries;

    @Enumerated(EnumType.STRING)
    @Column(name = "retry_policy")
    private RetryPolicy retryPolicy;

    // Osprey instance id that is holding this Job
    @Setter
    @Column(name = "claimed_by")
    private String claimedBy;

    @Setter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Setter
    @Column(name = "last_attempt_completed_at")
    private LocalDateTime lastAttemptCompletedAt;

    public Job(String type, String payload, LocalDateTime scheduledAt, ExecutionPattern executionPattern, LocalDateTime nextRunAt, int totalAttempts, int maxRetries, RetryPolicy retryPolicy, LocalDateTime createdAt) {
        this.id = IdGenerator.generateUUIDv7();
        this.status = JobStatus.PENDING;
        this.attempt = 0;
        this.retries = 0;
        this.claimedBy = "none";
        this.createdAt = LocalDateTime.now();

        this.type = type;
        this.payload = payload;
        this.scheduledAt = scheduledAt;
        this.executionPattern = executionPattern;
        this.nextRunAt = nextRunAt;
        this.totalAttempts = totalAttempts;
        this.maxRetries = maxRetries;
        this.retryPolicy = retryPolicy;
        this.createdAt = createdAt;
    }

    public String getFriendlyID() {
        return "job_" + this.id.toString();
    }
}