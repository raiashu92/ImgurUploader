package com.akr.leadIq.controller;

import com.akr.leadIq.datastore.DatabaseMap;
import com.akr.leadIq.domain.JobIdObject;
import com.akr.leadIq.domain.JobStatusObject;
import com.akr.leadIq.domain.UrlObject;
import com.akr.leadIq.service.UrlUploadRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
* controller class for our REST service.
* Supports 2 GET and 1 PUT operations
* */

@RestController
@RequestMapping(value = "/v1/images", produces = "application/json")
public class JobController {

    @Autowired
    private DatabaseMap databaseMap;

    @Autowired
    private UrlUploadRequestService asyncRequestService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<JobIdObject> uploadJob(@RequestBody UrlObject urlObject){

        CompletableFuture<JobIdObject> responseJobId = asyncRequestService.getJobIdForUrl(urlObject);
        try {
            return new ResponseEntity<>(responseJobId.get(), HttpStatus.CREATED);
        } catch (InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ExecutionException e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/upload/{jobId}", method = RequestMethod.GET)
    public ResponseEntity<JobStatusObject> findByJobId(@PathVariable Long jobId){
        Optional<JobStatusObject> jobStatusObject = databaseMap.getJobStatusById(jobId);

        if(jobStatusObject.isPresent()) {
            return new ResponseEntity<>(jobStatusObject.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<UrlObject> findAllJobs(){
        UrlObject urlObject = new UrlObject();
        urlObject.setUrls(databaseMap.getAllUploadedLinks());

        return new ResponseEntity<>(urlObject, HttpStatus.OK);
    }

}
