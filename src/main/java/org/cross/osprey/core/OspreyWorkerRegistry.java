package org.cross.osprey.core;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OspreyWorkerRegistry {
    private ConcurrentMap<String, HeartBeat> registry;

    public OspreyWorkerRegistry() {
        this.registry = new ConcurrentHashMap<>();
    }

    public void updateHeartBeat(String workerID, HeartBeat heartBeat) {
        this.registry.put(workerID, heartBeat);
    }

    public List<String> getActiveWorkerIDs(int validTimeoutSeconds) {
        List<String> workerIDs = new ArrayList<>();

        registry.forEach((workerID, heartBeat) -> {
            if (heartBeat.isValid(validTimeoutSeconds)) {
                workerIDs.add(workerID);
            }
        });

        return workerIDs;
    }

 }