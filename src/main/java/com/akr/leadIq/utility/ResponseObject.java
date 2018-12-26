package com.akr.leadIq.utility;

public class ResponseObject {
    private String link;
    private String id;
    private String imgType;
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if (statusCode >= 200 && statusCode < 300) {
            return "ResponseObject{" +
                    "link='" + link + '\'' +
                    ", id='" + id + '\'' +
                    ", imgType='" + imgType + '\'' +
                    ", status='" + statusCode + '\'' +
                    '}';
        } else {
            return "ResponseObject{" +
                    ", status='" + statusCode + '\'' +
                    '}';
        }
    }
}
