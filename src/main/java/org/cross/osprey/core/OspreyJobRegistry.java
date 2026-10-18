package org.cross.osprey.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OspreyJobRegistry {
    private final ConcurrentHashMap<Integer, Set<UUID>> registry;

    public OspreyJobRegistry() {
        this.registry = new ConcurrentHashMap<>();
    }


    public void addJobEntry(UUID jobID, int workerID) {
       var jobList = registry.get(workerID);

       if (jobList != null) {
           jobList.add(jobID);
           return;
       }

       registry.put(workerID, new HashSet<>(List.of(jobID)));
    }

    public Set<UUID> getJobIDs(int workerID) {
        return registry.get(workerID);
    }

    public Set<UUID> getJobIDs(Collection<Integer> workerIDs) {
        Set<UUID> jobIDs = new HashSet<>();

        workerIDs.forEach(
                (workerID) -> {
                    jobIDs.addAll(registry.get(workerID));
                }
        );

        return jobIDs;
    }
}
