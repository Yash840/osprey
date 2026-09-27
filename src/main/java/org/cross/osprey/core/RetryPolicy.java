package org.cross.osprey.core;

public enum RetryPolicy {
    LINEAR,
    EXPONENTIAL,
    NONE;

    @Override
    public String toString() {
        return switch (this) {
            case LINEAR -> "linear";
            case EXPONENTIAL -> "exponential";
            case NONE -> "none";
        };
    }

    public RetryPolicy fromString(String s) {
        String sanitized = s.trim().toLowerCase();

        return switch (sanitized) {
            case "linear" -> LINEAR;
            case "exponential" -> EXPONENTIAL;
            case "none" -> NONE;
            default -> throw new IllegalArgumentException("invalid retry policy: " + s);
        };
    }
}
