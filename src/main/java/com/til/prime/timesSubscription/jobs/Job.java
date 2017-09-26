package com.til.prime.timesSubscription.jobs;

import com.til.prime.timesSubscription.dto.internal.JobDetails;
import com.til.prime.timesSubscription.enums.JobKeyEnum;
import com.til.prime.timesSubscription.model.JobAuditModel;

public interface Job {
    JobKeyEnum getJobKey();
    String getCurrentSystemIP() throws Exception;
    boolean acquireLock();
    boolean releaseLock();
    boolean updateCurrentExecutionOwner(boolean acquireLock);
    void runJob();
    void beforeShutdown();
    JobDetails execute();
    void run();
    JobAuditModel getJobAudit(JobDetails jobDetails);
}
