package org.cross.osprey.core;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Worker implements Runnable{
    private final OspreyWorkerRegistry workerRegistry;
    private final OspreyJobQueue jobQueue;

    private int id;
    private Job acquiredJob;
    private Thread thread;

    public Worker(int id, OspreyWorkerRegistry workerRegistry, OspreyJobQueue jobQueue) {
        this.id = id;

        this.workerRegistry = workerRegistry;
        this.jobQueue = jobQueue;
    }

    @Override
    public void run() {

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
