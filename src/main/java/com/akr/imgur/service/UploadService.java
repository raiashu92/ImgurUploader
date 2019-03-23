package com.akr.imgur.service;

import com.akr.imgur.datastore.JobUrlLists;
import com.akr.imgur.exception.UploadException;
import com.akr.imgur.utility.CustomResponseHandler;
import com.akr.imgur.utility.ResponseObject;
import com.akr.imgur.utility.TimeZone;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/*
* Connects to imgur api to upload the provided Base64 string.
* Based on the response the respective lists- uploaded, failed,
* completed are updated.
* */

public class UploadService {
    private static final Logger LOGGER = Logger.getLogger(UploadService.class);
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
            //LOGGER.info("----------------------------------------");
            LOGGER.info(responseBody);

            status = responseBody.getStatusCode();
            if(status>=200 && status<300){
                jobUrlLists.getPending().remove(imageLink);
                jobUrlLists.getCompleted().add(responseBody.getLink());
                if(jobUrlLists.getPending().isEmpty()) {
                    TimeZone dateTime = new TimeZone();
                    jobUrlLists.setFinished(dateTime.getTimeNow());
                    jobUrlLists.setStatus("processed");
                }
                LOGGER.info("Adding the imgur link for " + imageLink + " to completed list.");
            } else {
                jobUrlLists.getPending().remove(imageLink);
                jobUrlLists.getFailed().add(imageLink);
                LOGGER.info("Adding the link " + imageLink + " to failed list");
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
