package org.cross.osprey.core;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OspreyWorkerRegistry {
    private ConcurrentMap<Integer, HeartBeat> registry;

    public OspreyWorkerRegistry() {
        this.registry = new ConcurrentHashMap<>();
    }

    public void updateHeartBeat(int workerID, HeartBeat heartBeat) {
        this.registry.put(workerID, heartBeat);
    }

    public List<Integer> getActiveWorkerIDs(int validTimeoutSeconds) {
        List<Integer> workerIDs = new ArrayList<>();

        registry.forEach((workerID, heartBeat) -> {
            if (heartBeat.isValid(validTimeoutSeconds)) {
                workerIDs.add(workerID);
            }
        });

        return workerIDs;
    }

 }