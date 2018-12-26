package com.akr.leadIq.service;

import com.akr.leadIq.datastore.JobUrlLists;
import com.akr.leadIq.utility.URLToBase64;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainRequestExecutor {
    public static final int NUMBER_OF_THREADS = 4;
    URLToBase64 urlToBase64;
    UploadService uploadService;
    JobUrlLists jobUrlLists;

    public MainRequestExecutor(JobUrlLists jobUrlLists) {
        this.urlToBase64 = new URLToBase64();
        this.jobUrlLists = jobUrlLists;
        this.uploadService = new UploadService(jobUrlLists);

    }

    public void mainExecutor() {
        ExecutorService execService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        for (String link : jobUrlLists.getPending()){
            RequestExecutor reqExec = new RequestExecutor(link, urlToBase64, uploadService);
            execService.submit(reqExec);
        }


        execService.shutdown();
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
