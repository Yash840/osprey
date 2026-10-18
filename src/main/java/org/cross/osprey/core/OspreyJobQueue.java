package org.cross.osprey.core;

import org.cross.osprey.config.OspreyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

@Component
public class OspreyJobQueue {
    private final ArrayBlockingQueue<Job> pendingJobs;
    private final ArrayBlockingQueue<Job> runningJobs;

    @Autowired
    public OspreyJobQueue(OspreyProperties properties) {
        this.pendingJobs = new ArrayBlockingQueue<>(properties.queueCapacity());
        this.runningJobs = new ArrayBlockingQueue<>(properties.queueCapacity());
    }

    public void enqueue(Collection<Job> jobs) {
        for(var job: jobs) {
            pendingJobs.offer(job);
        }
    }

    public synchronized Job acquireJob() {
        var job = pendingJobs.poll();
        runningJobs.offer(job);

        return job;
    }

    public void releaseJob(UUID jobID) {
        runningJobs.removeIf(j -> j.getId().equals(jobID));
    }

    public List<Job> getPendingJobs() {
        return pendingJobs.stream().toList();
    }

    public List<Job> getRunningJobs() {
        return runningJobs.stream().toList();
    }

    public int getAvailableCapacity() {
        return pendingJobs.remainingCapacity();
    }
}
