package org.cross.osprey.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cross.osprey.config.OspreyProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Dispatcher {
    final long HEARTBEAT_INTERVAL_SECONDS = 4;
    final long THREAD_REST_DUR_SECONDS = 2;
    final long SHUTDOWN_WAITING_SECONDS = 30;

    private final JobRepository jobRepository;

    private final OspreyJobQueue jobQueue;
    private final OspreyProperties properties;

    @Getter
    private OspreyStatus status;

    private final Set<Worker> workers;
    private final OspreyWorkerRegistry workerRegistry;
    private final ExecutorService executorService;


    public Dispatcher(JobRepository jobRepository, OspreyJobQueue jobQueue, OspreyProperties properties) {
        log.info("osprey dispatcher booting");

        this.jobRepository = jobRepository;
        this.jobQueue = jobQueue;
        this.properties = properties;

        this.status = OspreyStatus.CREATED;

        this.workers = new HashSet<>();
        this.workerRegistry = new OspreyWorkerRegistry();

        this.executorService = Executors.newFixedThreadPool(properties.maxWorkers());

        log.info("osprey dispatcher is ready");
    }

    private void pollJobs() {
        executorService.submit(() -> {
            log.info("Started polling for Jobs");
            try {
                while (status == OspreyStatus.RUNNING && jobQueue.getAvailableCapacity() > 0){
                    List<Job> jobs = jobRepository.claimExecutableJobs(jobQueue.getAvailableCapacity());
                    log.info("Polled {} jobs", jobs.size());

                    this.jobQueue.enqueue(jobs);

                    if (jobs.isEmpty()) {
                        Thread.sleep(Duration.ofSeconds(THREAD_REST_DUR_SECONDS));
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initiateWorkers() {
        log.info("Initiating Osprey workers");

            int i = 0;

            for (; i < properties.maxWorkers(); i++) {
                var worker = new Worker(i, workerRegistry, jobQueue);

                workers.add(worker);

                var now = LocalDateTime.now();
                workerRegistry.updateHeartBeat(worker.getId(), new HeartBeat( now.plusSeconds(HEARTBEAT_INTERVAL_SECONDS)));

                log.info("Osprey worker #{} initiated", worker.getId());

                executorService.submit(worker);
                log.info("Osprey worker #{} ready to execute", worker.getId());
            }

        log.info("Initiated {} workers", i);
    }

    public void start() {
        log.info("Starting osprey dispatcher...");

        this.status = OspreyStatus.RUNNING;

        pollJobs();

        initiateWorkers();
    }

    public void shutdown() {
        log.info("Graceful shutdown initiated...");
        this.status = OspreyStatus.SHUTTING_DOWN;

        try {
            executorService.shutdown();

            if (! executorService.awaitTermination(SHUTDOWN_WAITING_SECONDS, TimeUnit.SECONDS)) {
                log.warn("Executor waiting time elapsed, Some Jobs may not be finished");
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.status = OspreyStatus.SHUTDOWN;
        log.info("Graceful Shutdown!!");
    }
}
