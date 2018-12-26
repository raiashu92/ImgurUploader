package com.akr.leadIq.service;

import com.akr.leadIq.datastore.JobUrlLists;
import com.akr.leadIq.utility.URLToBase64;

public class RequestExecutor implements Runnable{
    private String imageLink;
    URLToBase64 urlToBase64;
    UploadService uploadService;

    public RequestExecutor(String imageLink, URLToBase64 urlToBase64, UploadService uploadService) {
        this.imageLink = imageLink;
        this.uploadService = uploadService;
        this.urlToBase64 = urlToBase64;
    }

    @Override
    public void run() {
        //download image and convert to Base64 string
        String base64String = urlToBase64.getBase64String(imageLink);
        //System.out.println(Thread.currentThread().toString() + ": got base64 string: " + base64String);

        //upload the image to imgur using the provided base 64 string
        uploadService.uploadImage(base64String, imageLink);

        //System.out.println(Thread.currentThread().toString() + "lists list" + jobUrlLists);

    }
}
