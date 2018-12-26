package com.akr.leadIq.service;

import com.akr.leadIq.datastore.DatabaseMap;
import com.akr.leadIq.datastore.JobUrlLists;
import com.akr.leadIq.exception.UploadException;
import com.akr.leadIq.utility.TimeZone;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import com.akr.leadIq.utility.CustomResponseHandler;
import com.akr.leadIq.utility.ResponseObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/*
* Connects to imgur api to upload the provided Base64 string.
* Based on the response the respective lists- uploaded, failed,
* completed are updated.
* */

public class UploadService {
    public static final String CLIENT_ID = "6c5dfa87e16af0c";
    public static final String IMGUR_URL = "https://api.imgur.com/3/image";
    JobUrlLists jobUrlLists;

    public UploadService(JobUrlLists jobUrlLists) {
        this.jobUrlLists = jobUrlLists;
    }

    public JobUrlLists getJobUrlLists() {
        return jobUrlLists;
    }

    public void uploadImage(String base64String, String imageLink) throws UploadException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPostRequest = new HttpPost(IMGUR_URL);
        httpPostRequest.setHeader("Authorization", "Client-ID " + CLIENT_ID);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", base64String));
        CustomResponseHandler customResponseHandler = new CustomResponseHandler();
        int status = -1;
        try {
            httpPostRequest.setEntity(new UrlEncodedFormEntity(params));
            ResponseObject responseBody = (ResponseObject) httpClient.execute(httpPostRequest, customResponseHandler);
            System.out.println(Thread.currentThread().toString() + "----------------------------------------");
            System.out.println(Thread.currentThread().toString() + " : " + responseBody);

            status = responseBody.getStatusCode();
            if(status>=200 && status<300){
                jobUrlLists.getPending().remove(imageLink);
                jobUrlLists.getCompleted().add(responseBody.getLink());
                if(jobUrlLists.getPending().isEmpty()) {
                    TimeZone dateTime = new TimeZone();
                    jobUrlLists.setFinished(dateTime.getTimeNow());
                    jobUrlLists.setStatus("processed");
                }
                System.out.println("Adding the link to completed");
            } else {
                jobUrlLists.getPending().remove(imageLink);
                jobUrlLists.getFailed().add(imageLink);
                System.out.println("Adding the link to failed");
            }

            httpClient.close();
        } catch (UnsupportedEncodingException e) {
            throw new UploadException(e, status);
        } catch (ClientProtocolException e) {
            throw new UploadException(e, status);
        } catch (IOException e) {
            throw new UploadException(e, status);
        }

    }
}
