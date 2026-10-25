package org.cross.osprey.core;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Getter
@Setter
@Slf4j
public class Worker{
    private final Dispatcher dispatcher;

    private int id;
    private Job acquiredJob;
    private Thread thread;

    public Worker(int id, Dispatcher dispatcher) {
        this.id = id;
        this.dispatcher = dispatcher;
    }


    public void execute() {
        while (dispatcher.getStatus() == OspreyStatus.RUNNING) {
            Job job = dispatcher.acquireJob();
            log.debug("Worker {}: acquired {}", id, job.getFriendlyID());
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Worker w) {
            return this.id == w.id;
        }

        return false;
    }
}
