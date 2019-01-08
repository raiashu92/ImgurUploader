package com.akr.leadIq.service;

import com.akr.leadIq.datastore.DatabaseMap;
import com.akr.leadIq.datastore.JobUrlLists;
import com.akr.leadIq.domain.JobIdObject;
import com.akr.leadIq.domain.UrlObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UrlUploadRequestService {
    private static final Logger LOGGER = Logger.getLogger(UrlUploadRequestService.class);

    @Autowired
    private DatabaseMap databaseMap;

    @Async
    public CompletableFuture<JobIdObject> getJobIdForUrl(UrlObject urlObject) {
        JobUrlLists jobUrlLists = databaseMap.create(urlObject);
        JobIdObject jobId = new JobIdObject();
        jobId.setJobId(jobUrlLists.getId());
        LOGGER.info("current jobid: " + jobId.getJobId());
        MainRequestExecutor mainRequestExecutor = new MainRequestExecutor(jobUrlLists);
        mainRequestExecutor.mainExecutor();

        return CompletableFuture.completedFuture(jobId);
    }

}
