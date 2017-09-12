package com.til.prime.timesSubscription.dto.internal;

import com.til.prime.timesSubscription.model.JobModel;

import java.util.Date;

public class JobDetails {
    private JobModel job;
    private String owner;
    private Date startTime;
    private Date endTime;
    private Long recordsAffected;
    private boolean completed;
    private AffectedModelDetails affectedModelDetails;
    private String response;
    private String exception;

    public JobModel getJob() {
        return job;
    }

    public void setJob(JobModel job) {
        this.job = job;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getRecordsAffected() {
        return recordsAffected;
    }

    public void setRecordsAffected(Long recordsAffected) {
        this.recordsAffected = recordsAffected;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public AffectedModelDetails getAffectedModelDetails() {
        return affectedModelDetails;
    }

    public void setAffectedModelDetails(AffectedModelDetails affectedModelDetails) {
        this.affectedModelDetails = affectedModelDetails;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JobDetails{");
        sb.append("job=").append(job);
        sb.append(", owner='").append(owner).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", recordsAffected=").append(recordsAffected);
        sb.append(", completed=").append(completed);
        sb.append(", affectedModelDetails=").append(affectedModelDetails);
        sb.append(", response='").append(response).append('\'');
        sb.append(", exception='").append(exception).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
