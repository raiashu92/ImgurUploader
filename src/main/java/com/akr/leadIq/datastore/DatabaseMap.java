package com.akr.leadIq.datastore;

import com.akr.leadIq.domain.JobStatusObject;
import com.akr.leadIq.domain.LinkStatusObject;
import com.akr.leadIq.domain.UrlObject;
import com.akr.leadIq.utility.IdGenerator;
import com.akr.leadIq.utility.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DatabaseMap {

    @Autowired
    private IdGenerator idGenerator;

    public static final Map<Long, JobUrlLists> jobs = new ConcurrentHashMap<Long, JobUrlLists>();

    public Map<Long, JobUrlLists> getJobs() {
        return jobs;
    }

    public JobUrlLists create(UrlObject urlObject){
        JobUrlLists jobUrlListsObj = new JobUrlLists();
        Long id = idGenerator.getNextId();
        jobUrlListsObj.setId(String.valueOf(id));
        jobUrlListsObj.setStatus("in-progress");

        //removing duplicates
        Set<String> hashSet = new LinkedHashSet<>(urlObject.getUrls());
        jobUrlListsObj.getPending().addAll(hashSet);

        TimeZone dateTime = new TimeZone();
        jobUrlListsObj.setCreated(dateTime.getTimeNow());

        jobs.put(id, jobUrlListsObj);
        return jobUrlListsObj;
    }

    public Optional<JobStatusObject> getJobStatusById (Long id) {
        JobStatusObject jobStatusObject = null;
        if(jobs.containsKey(id)) {
            JobUrlLists jobListObject = jobs.get(id);

            jobStatusObject = new JobStatusObject();
            jobStatusObject.setId(jobListObject.getId());
            jobStatusObject.setCreated(jobListObject.getCreated());
            jobStatusObject.setStatus(jobListObject.getStatus());

            LinkStatusObject linkStatusObject = new LinkStatusObject();
            if (!jobListObject.getPending().isEmpty()) {
                linkStatusObject.setPending(jobListObject.getPending());
            } else {
                jobStatusObject.setFinished(jobListObject.getFinished());
            }
            if (!jobListObject.getCompleted().isEmpty()) {
                linkStatusObject.setComplete(jobListObject.getCompleted());
            }
            if (!jobListObject.getFailed().isEmpty()) {
                linkStatusObject.setFailed(jobListObject.getFailed());
            }
            jobStatusObject.setUploaded(linkStatusObject);
        }
        return Optional.ofNullable(jobStatusObject);
    }

    public List<String> getAllUploadedLinks () {
        List<String> uploadedUrlsList = new ArrayList<String>();
        for (Map.Entry<Long, JobUrlLists> entry : jobs.entrySet()){
            uploadedUrlsList.addAll(entry.getValue().getCompleted());
        }
        return uploadedUrlsList;

    }
}
