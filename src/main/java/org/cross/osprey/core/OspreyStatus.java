package org.cross.osprey.core;

public enum OspreyStatus {
    CREATED,
    RUNNING,
    SHUTTING_DOWN,
    SHUTDOWN;

    @Override
    public String toString() {
        return switch (this) {
            case CREATED -> "created";
            case RUNNING -> "running";
            case SHUTTING_DOWN -> "shutting_down";
            case SHUTDOWN -> "shutdown";
        };
    }

    public static OspreyStatus fromString(String s) {
        String sanitized = s.trim().toLowerCase();

        return switch (sanitized) {
            case "created" -> CREATED;
            case "running" -> RUNNING;
            case "shutting_down" -> SHUTTING_DOWN;
            case "shutdown" -> SHUTDOWN;
            default -> throw new IllegalArgumentException("invalid osprey status: " + s);
        };
    }
}
