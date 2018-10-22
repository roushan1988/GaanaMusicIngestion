package com.til.prime.timesSubscription.jobs;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dao.JobAuditRepository;
import com.til.prime.timesSubscription.dao.JobRepository;
import com.til.prime.timesSubscription.dto.internal.JobDetails;
import com.til.prime.timesSubscription.model.JobAuditModel;
import com.til.prime.timesSubscription.model.JobModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractJob implements Job {
    protected static final Logger LOG = Logger.getLogger(AbstractJob.class);
    protected AtomicBoolean lockAcquired = new AtomicBoolean(false);
    @Autowired
    protected JobRepository jobRepository;
    @Autowired
    protected JobAuditRepository jobAuditRepository;
    @Resource(name = "config_properties")
    private Properties properties;

    public String getCurrentSystemIP() throws Exception{
        return InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    @Transactional
    public boolean acquireLock(){
        return updateCurrentExecutionOwner(true);
    }

    @Override
    @Transactional
    public boolean releaseLock(){
        return updateCurrentExecutionOwner(false);
    }

    public boolean updateCurrentExecutionOwner(boolean acquireLock){
        try {
            if (acquireLock) {
                int count = jobRepository.updateOwnerIfNull(getCurrentSystemIP(), getJobKey());
                if (count > 0) {
                    lockAcquired.compareAndSet(false, true);
                    return true;
                }
                return false;
            } else {
                JobModel jobModel = jobRepository.findByJobKey(getJobKey());
                jobModel.setOwner(null);
                jobRepository.save(jobModel);
                lockAcquired.compareAndSet(true, false);
                return true;
            }
        }catch (Exception e){
            LOG.error("Exception while updating currentExecutionOwner, key: "+getJobKey()+", acquireLock: "+acquireLock, e);
            return false;
        }
    }

    @Override
    @Transactional
    public void runJob(){
        String cronEnabled = properties.getProperty(GlobalConstants.CRON_ENABLED);
        LOG.info("Starting job execution with key: " + getJobKey());
        if(StringUtils.isNotEmpty(cronEnabled) && Boolean.valueOf(cronEnabled)) {
            boolean lock = false;
            try {
                if (acquireLock()) {
                    lock = true;
                    JobDetails jobDetails = execute();
                    jobAuditRepository.save(getJobAudit(jobDetails));
                }
            } catch (Exception e) {
                LOG.error("Exception while running job with key: " + getJobKey(), e);
            } finally {
                if (lock) {
                    releaseLock();
                    LOG.info("Released lock after execution with key: " + getJobKey());
                }else{
                    LOG.info("Lock not acquired for job key: "+getJobKey());
                }
            }
        }else{
            LOG.info("Cron not enabled on this server for job key: "+getJobKey());
        }
    }

    @PostConstruct
    public void startup() throws Exception{
        LOG.info("Checking locks for job with key: "+getJobKey());
        try {
            JobModel jobModel = jobRepository.findByJobKeyAndOwner(getJobKey(), getCurrentSystemIP());
            if(jobModel!=null){
                LOG.info("Releasing lock for job with key: "+getJobKey());
                boolean released = releaseLock();
                LOG.info("Lock release "+(released?"SUCCESS":"FAILURE")+" for job with key: "+getJobKey());
            }
        } catch (Exception e) {
            LOG.error("Exception while releasing lock on startup", e);
            throw e;
        }
        LOG.info("Checkup complete for job with key: "+getJobKey());
    }

    @PreDestroy
    public void beforeShutdown(){
        LOG.info("Shutting down job with key: "+getJobKey());
        LOG.info("Job key: "+getJobKey()+", Lock: "+lockAcquired.get());
        if(lockAcquired.get()){
            LOG.info("Releasing lock for job with key: "+getJobKey());
            boolean released = releaseLock();
            LOG.info("Lock release "+(released?"SUCCESS":"FAILURE")+" for job with key: "+getJobKey());
        }
        LOG.info("Shutdown complete for job with key: "+getJobKey());
    }

    @Override
    public JobAuditModel getJobAudit(JobDetails jobDetails){
        JobAuditModel jobAuditModel = new JobAuditModel();
        jobAuditModel.setJobId(jobDetails.getJob().getId());
        jobAuditModel.setOwner(jobDetails.getOwner());
        jobAuditModel.setStartTime(jobDetails.getStartTime());
        jobAuditModel.setEndTime(jobDetails.getEndTime());
        jobAuditModel.setCompleted(jobDetails.isCompleted());
        jobAuditModel.setRecordsAffected(jobDetails.getRecordsAffected());
        jobAuditModel.setAffectedModelDetails(jobDetails.getAffectedModelDetails());
        jobAuditModel.setResponse(jobDetails.getResponse());
        jobAuditModel.setException(jobDetails.getException());
        jobAuditModel.setCreated(new Date());
        return jobAuditModel;
    }
}
