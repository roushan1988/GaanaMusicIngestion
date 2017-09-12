package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.enums.JobKeyEnum;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="job")
public class JobModel extends BaseModel{
    @Column
    private String name;
    @Column(name="job_key")
    @Enumerated(EnumType.STRING)
    private JobKeyEnum jobKey;
    @Column
    private String owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobKeyEnum getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKeyEnum jobKey) {
        this.jobKey = jobKey;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
