package com.akr.leadIq.controller;

import com.akr.leadIq.datastore.DatabaseMap;
import com.akr.leadIq.datastore.JobUrlLists;
import com.akr.leadIq.domain.JobIdObject;
import com.akr.leadIq.domain.JobStatusObject;
import com.akr.leadIq.domain.UrlObject;
import com.akr.leadIq.service.MainRequestExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/images", produces = "application/json")
public class JobController {

    @Autowired
    private DatabaseMap databaseMap;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<JobIdObject> uploadJob(@RequestBody UrlObject urlObject){
        JobUrlLists jobUrlLists = databaseMap.create(urlObject);
        JobIdObject jobId = new JobIdObject();
        jobId.setJobId(jobUrlLists.getId());

        MainRequestExecutor mainRequestExecutor = new MainRequestExecutor(jobUrlLists);
        mainRequestExecutor.mainExecutor();

        return new ResponseEntity<>(jobId, HttpStatus.CREATED);
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
