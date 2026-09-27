package org.cross.osprey.core;

import org.cross.osprey.exception.InvalidJobStatusException;

import java.util.Locale;

public enum JobStatus {
    PENDING,
    CLAIMED,
    RUNNING,
    WAIT_RETRY,
    WAIT_NEXT,
    FAILED;


    @Override
    public String toString() {
        return switch (this) {
            case PENDING -> "pending";
            case CLAIMED -> "claimed";
            case RUNNING -> "running";
            case WAIT_NEXT -> "wait_next";
            case WAIT_RETRY -> "wait_retry";
            case FAILED -> "failed";
        };
    }

    public static JobStatus fromString(String s) {
        String sanitized = s.trim().toLowerCase();

        return switch (sanitized) {
            case "pending" -> PENDING;
            case "claimed" -> CLAIMED;
            case "running" -> RUNNING;
            case "wait_next" -> WAIT_NEXT;
            case "wait_retry" -> WAIT_RETRY;
            case "failed" -> FAILED;
            default -> throw new InvalidJobStatusException(s);
        };
    }
}
