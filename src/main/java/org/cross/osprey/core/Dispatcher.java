package org.cross.osprey.core;

import lombok.extern.slf4j.Slf4j;
import org.cross.osprey.config.OspreyProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class Dispatcher {
    private final JobRepository jobRepository;

    private final OspreyJobQueue jobQueue;
    private final OspreyProperties properties;

    private OspreyStatus status;

    private Set<Worker> workers;
    private OspreyWorkerRegistry workerRegistry;

    public Dispatcher(JobRepository jobRepository, OspreyJobQueue jobQueue, OspreyProperties properties) {
        log.info("osprey dispatcher booting");

        this.jobRepository = jobRepository;
        this.jobQueue = jobQueue;
        this.properties = properties;

        this.status = OspreyStatus.RUNNING;

        this.workers = new HashSet<>();
        this.workerRegistry = new OspreyWorkerRegistry();

        log.info("osprey dispatcher is ready");
    }

    private void pollJobs() {
        if (status != OspreyStatus.RUNNING) {
            return;
        }

        List<Job> jobs = jobRepository.claimExecutableJobs(jobQueue.getAvailableCapacity());

        this.jobQueue.enqueue(jobs);
    }

    private Job acquireJob() {
        return jobQueue.dequeue();
    }

    private void initiateWorkers() {
        log.info("Initiating Osprey workers");

        try (var executor = Executors.newFixedThreadPool(properties.maxWorkers())){
            for (int i = 0; i < properties.maxWorkers(); i++) {
                var worker = new Worker(i, workerRegistry, jobQueue);

                workers.add(worker);

                log.info("Osprey worker #{} initiated", worker.getId());

                executor.submit(worker);
                log.info("Osprey worker #{} ready to execute", worker.getId());
            }
        }
    }
}
