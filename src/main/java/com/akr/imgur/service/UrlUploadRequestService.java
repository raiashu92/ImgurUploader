package com.akr.imgur.service;

import com.akr.imgur.datastore.DatabaseMap;
import com.akr.imgur.datastore.JobUrlLists;
import com.akr.imgur.domain.JobIdObject;
import com.akr.imgur.domain.UrlObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class UrlUploadRequestService {
    private static final Logger LOGGER = Logger.getLogger(UrlUploadRequestService.class);
    public static final int NUMBER_OF_THREADS = 4;
    ExecutorService execService;

    @Autowired
    private DatabaseMap databaseMap;

    public UrlUploadRequestService() {
        LOGGER.info("Starting the executor service for link download and upload to imgur with " + NUMBER_OF_THREADS + " threads ...");
        execService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    @Async
    public CompletableFuture<JobIdObject> getJobIdForUrl(UrlObject urlObject) {
        JobUrlLists jobUrlLists = databaseMap.create(urlObject);
        JobIdObject jobId = new JobIdObject();
        jobId.setJobId(jobUrlLists.getId());
        LOGGER.info("current jobid: " + jobId.getJobId());
        MainRequestExecutor mainRequestExecutor = new MainRequestExecutor(execService);
        mainRequestExecutor.mainExecutor(jobUrlLists);

        return CompletableFuture.completedFuture(jobId);
    }

    @PreDestroy
    public void closeExecService() {
        execService.shutdown();
        LOGGER.info("Shutting down upload executor service ...");
        //forced termination if still not closed
        try {
            if (!execService.awaitTermination(3000L, TimeUnit.MILLISECONDS)) {
                LOGGER.warn("ExecutorService didn't terminate in the specified time.");
                List<Runnable> droppedTasks = execService.shutdownNow();
                LOGGER.warn("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
