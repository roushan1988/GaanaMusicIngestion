package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.dto.internal.AffectedModelDetails;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="job_audit")
public class JobAuditModel extends BaseModel {
    @Column(name="job_id")
    private Long jobId;
    @Column
    private String owner;
    @Column(name="start_time")
    private Date startTime;
    @Column(name="end_time")
    private Date endTime;
    @Column
    private boolean completed;
    @Column(name="records_affected")
    private Long recordsAffected;
    @Column(name="affected_model_details")
    @Type(type = "com.til.prime.timesSubscription.pojo.XMLUserType")
    private AffectedModelDetails affectedModelDetails;
    @Column
    private String response;
    @Column
    private String exception;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getRecordsAffected() {
        return recordsAffected;
    }

    public void setRecordsAffected(Long recordsAffected) {
        this.recordsAffected = recordsAffected;
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
}
