package org.cross.osprey.core;

public enum OspreyStatus {
    BOOTING,
    RUNNING,
    SHUTTING_DOWN,
    SHUTDOWN;

    @Override
    public String toString() {
        return switch (this) {
            case BOOTING -> "booting";
            case RUNNING -> "running";
            case SHUTTING_DOWN -> "shutting_down";
            case SHUTDOWN -> "shutdown";
        };
    }

    public static OspreyStatus fromString(String s) {
        String sanitized = s.trim().toLowerCase();

        return switch (sanitized) {
            case "booting" -> BOOTING;
            case "running" -> RUNNING;
            case "shutting_down" -> SHUTTING_DOWN;
            case "shutdown" -> SHUTDOWN;
            default -> throw new IllegalArgumentException("invalid osprey status: " + s);
        };
    }
}
