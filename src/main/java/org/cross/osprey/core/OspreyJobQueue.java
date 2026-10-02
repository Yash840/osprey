package org.cross.osprey.core;

import org.cross.osprey.config.OspreyProperties;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

@Component
public class OspreyJobQueue {
    private final ArrayBlockingQueue<Job> buffer;

    public OspreyJobQueue(OspreyProperties properties) {
        this.buffer = new ArrayBlockingQueue<>(properties.queueCapacity());
    }

    public void enqueue(Collection<Job> jobs) {
        for(var job: jobs) {
            buffer.offer(job);
        }
    }

    public Job dequeue() {
        return buffer.poll();
    }

    public int getAvailableCapacity() {
        return buffer.remainingCapacity();
    }
}
