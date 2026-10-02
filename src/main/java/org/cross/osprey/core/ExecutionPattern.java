package org.cross.osprey.core;

public enum ExecutionPattern {
    INSTANT,
    DELAYED,
    INSTANT_REPEATING,
    DELAYED_REPEATING;

    @Override
    public String toString() {
        return switch (this) {
            case INSTANT -> "instant";
            case DELAYED -> "delayed";
            case INSTANT_REPEATING -> "instant_repeating";
            case DELAYED_REPEATING -> "delayed_repeating";
        };
    }

    public static ExecutionPattern fromString(String s) {
        String sanitized = s.trim().toLowerCase();

        return switch (sanitized) {
            case "instant" -> INSTANT;
            case "delayed" -> DELAYED;
            case "instant_repeating" -> INSTANT_REPEATING;
            case "delayed_repeating" -> DELAYED_REPEATING;
            default -> throw new IllegalArgumentException("invalid execution pattern: " + s);
        };
    }
}
