package com.akr.leadIq.domain;

import java.util.List;
//import java.util.concurrent.List;

public class LinkStatusObject {

    List<String> pending;
    List<String> complete;
    List<String> failed;

    public List<String> getPending() {
        return pending;
    }

    public void setPending(List<String> pending) {
        this.pending = pending;
    }

    public List<String> getComplete() {
        return complete;
    }

    public void setComplete(List<String> complete) {
        this.complete = complete;
    }

    public List<String> getFailed() {
        return failed;
    }

    public void setFailed(List<String> failed) {
        this.failed = failed;
    }
}
