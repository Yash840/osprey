package org.cross.osprey.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record HeartBeat(
        LocalDateTime instant
) {
    public boolean isValid(int timeoutSeconds) {
        long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), instant);

        return seconds >= timeoutSeconds;
    }
}
