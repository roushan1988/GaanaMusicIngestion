package com.til.prime.timesSubscription.jobs;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dao.UserSubscriptionRepository;
import com.til.prime.timesSubscription.dto.internal.AffectedModelDetails;
import com.til.prime.timesSubscription.dto.internal.JobDetails;
import com.til.prime.timesSubscription.enums.EventEnum;
import com.til.prime.timesSubscription.enums.JobKeyEnum;
import com.til.prime.timesSubscription.model.JobModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.service.SubscriptionService;
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
    private SubscriptionService subscriptionService;
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
            Long count = userSubscriptionRepository.countByExpiredFalseAndEndDateBeforeAndDeletedFalse(date);
            for (Long x = 0l; x <= (Math.max(0l, count - 1) / GlobalConstants.CRON_BATCH_PROCESSING_COUNT); x++) {
                Page<UserSubscriptionModel> page = userSubscriptionRepository.findByExpiredFalseAndEndDateBeforeAndDeletedFalse(date, new PageRequest(x.intValue(), GlobalConstants.CRON_BATCH_PROCESSING_COUNT.intValue()));
                List<UserSubscriptionModel> userSubscriptionModelList = page.getContent();
                if (userSubscriptionModelList != null) {
                    for (UserSubscriptionModel userSubscriptionModel : userSubscriptionModelList) {
                        userSubscriptionModel.setExpired(true);
                        userSubscriptionModel = subscriptionService.saveUserSubscription(userSubscriptionModel, false, null, null, EventEnum.USER_SUBSCRIPTION_EXPIRY);
                        recordsAffected++;
                        affectedModels.add(userSubscriptionModel.getId());
                    }
                }
            }
            jobDetails.setCompleted(true);
            jobDetails.setResponse(GlobalConstants.SUCCESS);
        }catch (Exception e){
            jobDetails.setException(e.getMessage());
            jobDetails.setResponse(GlobalConstants.EXCEPTION);
        }finally {
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
