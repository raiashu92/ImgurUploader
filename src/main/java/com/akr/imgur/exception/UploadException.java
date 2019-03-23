package com.akr.imgur.exception;

/*
* Custom exception class to handle failure when our upload job fails
* for some reason like no internet connection, upload to imgur failed etc
*
* */

public class UploadException extends Exception {
    int status;

    public String getStatus() {
        switch (status)
        {
            case 1:
                return "UNKNOWN_HOST";
            case 200:
                return "SUCCESS";
            case 400:
                return "BAD_REQUEST";
            case 401:
                return "UNAUTHORIZED";
            case 403:
                return "FORBIDDEN";
            case 404:
                return "NOT_FOUND";
            case 413:
                return "FILE_TOO_BIG";
            case 429:
                return "UPLOAD_LIMITED";
            case 500:
                return "INTERNAL_SERVER_ERROR";
            case 502:
                return "SERVICE_UNAVAILABLE";
            case 555:
                return "MALFORMED_URL_EXCEPTION";
            default:
                return "UNKNOWN_ERROR, internet connection may be down";
        }
    }

    public UploadException(Throwable cause, int status) {
        super(cause);
        this.status = status;
    }

    public UploadException(Throwable cause) {
        this(cause, -1);
    }
}
