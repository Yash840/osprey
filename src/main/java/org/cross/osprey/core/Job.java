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

    // Current execution number, used for avoiding stale worker progress (as Fence)
    @Setter
    private int attempt;

    // Total number of times Job is executed
    @Setter
    private int totalAttempts;

    // Job executions when Job is failed
    @Setter
    private int retries;

    private int maxRetries;

    @Enumerated(EnumType.STRING)
    private RetryPolicy retryPolicy;

    // Osprey instance id that is holding this Job
    @Setter
    private String claimedBy;

    @Setter
    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime lastAttemptCompletedAt;

    public Job(String type, String payload, int maxRetries, RetryPolicy retryPolicy) {
        this.id = IdGenerator.generateUUIDv7();
        this.status = JobStatus.PENDING;
        this.attempt = 0;
        this.retries = 0;
        this.claimedBy = "none";
        this.createdAt = LocalDateTime.now();

        this.type = type;
        this.payload = payload;
        this.maxRetries = maxRetries;
        this.retryPolicy = retryPolicy;
    }

    public String getFriendlyID() {
        return "job_" + this.id.toString();
    }
}