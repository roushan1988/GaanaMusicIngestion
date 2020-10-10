package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.enums.JobEnum;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="job")
public class JobEntity extends BaseModel {
    @Column
    @Enumerated(EnumType.STRING)
    private JobEnum name;
    @Column(name="start_time")
    private Date startTime;
    @Column(name="end_time")
    private Date endTime;
    @Column(name="record_count")
    private Integer recordCount;

    public JobEnum getName() {
        return name;
    }

    public void setName(JobEnum name) {
        this.name = name;
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

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    @Column(name="created")
    private Date created;
    @Column(name="updated", insertable=false, updatable = false)
    private Date updated;
    @Column(name="deleted")
    private boolean deleted;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
