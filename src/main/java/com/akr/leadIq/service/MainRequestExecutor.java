package com.akr.leadIq.service;

import com.akr.leadIq.datastore.JobUrlLists;
import com.akr.leadIq.utility.URLToBase64;
import org.apache.log4j.Logger;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* Main executor class, responsible for creating executor service
* and delegating tasks.
* */

public class MainRequestExecutor {
    private static final Logger LOGGER = Logger.getLogger(MainRequestExecutor.class);
    URLToBase64 urlToBase64;
    UploadService uploadService;
    JobUrlLists jobUrlLists;
    ExecutorService execService;

    public MainRequestExecutor(ExecutorService execService) {
        this.urlToBase64 = new URLToBase64();
        this.execService = execService;
    }

    public void mainExecutor(JobUrlLists jobUrlLists) {
        this.jobUrlLists = jobUrlLists;
        this.uploadService = new UploadService(jobUrlLists);



        for (String link : jobUrlLists.getPending()){
            RequestExecutor reqExec = new RequestExecutor(link, urlToBase64, uploadService);
            execService.submit(reqExec);
        }

        //shutdown logic moved to controller level
        //execService.shutdown();
        //forced termination not required
        /*try {
            if (!execService.awaitTermination(25000L, TimeUnit.MILLISECONDS)) {
                System.out.println("ExecutorService didn't terminate in the specified time.");
                List<Runnable> droppedTasks = execService.shutdownNow();
                System.out.println("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
