package com.til.prime.timesSubscription.jobs;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dao.UserSubscriptionAuditRepository;
import com.til.prime.timesSubscription.dao.UserSubscriptionRepository;
import com.til.prime.timesSubscription.dto.internal.AffectedModelDetails;
import com.til.prime.timesSubscription.dto.internal.JobDetails;
import com.til.prime.timesSubscription.dto.internal.SubscriptionExpired;
import com.til.prime.timesSubscription.enums.*;
import com.til.prime.timesSubscription.model.JobModel;
import com.til.prime.timesSubscription.model.UserSubscriptionAuditModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.service.CommunicationService;
import com.til.prime.timesSubscription.service.SubscriptionService;
import com.til.prime.timesSubscription.service.SubscriptionServiceHelper;
import com.til.prime.timesSubscription.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionExpiryJob extends AbstractJob {
    private final JobKeyEnum jobKeyEnum = JobKeyEnum.SUBSCRIPTION_EXPIRY;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private UserSubscriptionAuditRepository userSubscriptionAuditRepository;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private SubscriptionServiceHelper subscriptionServiceHelper;

    @Override
    public JobKeyEnum getJobKey() {
        return jobKeyEnum;
    }

    @Override
    public JobDetails execute() {
        JobDetails jobDetails = new JobDetails();
        Long recordsAffected = 0l;
        AffectedModelDetails<Long> affectedModelDetails = new AffectedModelDetails<>();
        List<Long> affectedModels = new ArrayList<>();
        Date date = new Date();
        try {
            JobModel jobModel = jobRepository.findByJobKey(jobKeyEnum);
            jobDetails.setJob(jobModel);
            jobDetails.setStartTime(new Date());
            Long count = userSubscriptionRepository.countByStatusAndEndDateBeforeAndDeletedFalseAndOrderCompletedTrue(StatusEnum.ACTIVE, date);
            for (Long x = 0l; x <= (Math.max(0l, count - 1) / GlobalConstants.CRON_BATCH_PROCESSING_COUNT); x++) {
                Page<UserSubscriptionModel> page = userSubscriptionRepository.findByStatusAndEndDateBeforeAndDeletedFalseAndOrderCompletedTrue(StatusEnum.ACTIVE, date, new PageRequest(x.intValue(), GlobalConstants.CRON_BATCH_PROCESSING_COUNT.intValue()));
                List<UserSubscriptionModel> userSubscriptionModelList = page.getContent();
                if (userSubscriptionModelList != null) {
                    for (UserSubscriptionModel userSubscriptionModel : userSubscriptionModelList) {
                        try {
                            SubscriptionExpired subscriptionExpired = subscriptionService.expireSubscription(userSubscriptionModel.getId());
                            recordsAffected += subscriptionExpired.getRecordsAffected();
                            affectedModels.addAll(subscriptionExpired.getAffectedModels());
                        } catch (Exception e) {
                            LOG.error("Exception in SubscriptionExpiryJob, userSubscription id: " + userSubscriptionModel.getId(), e);
                        }
                    }
                }
            }
            jobDetails.setCompleted(true);
            jobDetails.setResponse(GlobalConstants.SUCCESS);
        } catch (Exception e) {
            jobDetails.setException(e.getMessage());
            jobDetails.setResponse(GlobalConstants.EXCEPTION);
        } finally {
            affectedModelDetails.setAffectedModels(affectedModels);
            jobDetails.setRecordsAffected(recordsAffected);
            jobDetails.setAffectedModelDetails(affectedModelDetails);
            jobDetails.setEndTime(new Date());
            jobDetails.setCompleted(true);
            return jobDetails;
        }
    }


    @Override
    @Scheduled(cron = "${subscription.expiry.cron}")
    public void run() {
        runJob();
    }

}
