package com.akr.imgur.datastore;

import java.util.concurrent.CopyOnWriteArrayList;

/*
* Store the state of each job alongwith
* other relevant fields.
* */

public class JobUrlLists {
    String id;
    String created;
    String finished;
    String status;
    private final CopyOnWriteArrayList<String> pending;
    private final CopyOnWriteArrayList<String> completed;
    private final CopyOnWriteArrayList<String> failed;

    public JobUrlLists() {
        completed = new CopyOnWriteArrayList<>();
        pending = new CopyOnWriteArrayList<>();
        failed = new CopyOnWriteArrayList<>();
    }

    @Override
    public String toString() {
        return "JobUrlLists{" +
                "id='" + id + '\'' +
                ", created='" + created + '\'' +
                ", finished='" + finished + '\'' +
                ", status='" + status + '\'' +
                ", pending=" + pending +
                ", completed=" + completed +
                ", failed=" + failed +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CopyOnWriteArrayList<String> getPending() {
        return pending;
    }

    public CopyOnWriteArrayList<String> getCompleted() {
        return completed;
    }

    public CopyOnWriteArrayList<String> getFailed() {
        return failed;
    }

}
