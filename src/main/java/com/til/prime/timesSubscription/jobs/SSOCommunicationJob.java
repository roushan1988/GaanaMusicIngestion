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
import com.til.prime.timesSubscription.service.SubscriptionServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SSOCommunicationJob extends AbstractJob {

    private final JobKeyEnum jobKeyEnum = JobKeyEnum.SSO_COMMUNICATION;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private SubscriptionServiceHelper subscriptionServiceHelper;
    @Autowired
    SubscriptionService subscriptionService;

    @Override
    public JobKeyEnum getJobKey() {
        return jobKeyEnum;
    }

    @Override
    @Transactional
    public JobDetails execute() {
        JobDetails jobDetails = new JobDetails();
        Long recordsAffected = 0l;
        AffectedModelDetails affectedModelDetails = new AffectedModelDetails();
        List<UserSubscriptionModel> affectedModels = new ArrayList<>();
        try {
            JobModel jobModel = jobRepository.findByJobKey(jobKeyEnum);
            jobDetails.setJob(jobModel);
            jobDetails.setStartTime(new Date());
            Long count = userSubscriptionRepository.countBySsoCommunicatedFalseAndOrderCompletedTrueAndDeletedFalseAndOrderCompletedTrue();
            for (Long x = 0l; x <= (Math.max(0l, count - 1) / GlobalConstants.CRON_BATCH_PROCESSING_COUNT); x++) {
                Page<UserSubscriptionModel> page = userSubscriptionRepository.findBySsoCommunicatedFalseAndOrderCompletedTrueAndDeletedFalseAndOrderCompletedTrueOrderById(new PageRequest(x.intValue(), GlobalConstants.CRON_BATCH_PROCESSING_COUNT.intValue()));
                List<UserSubscriptionModel> userSubscriptionModelList = page.getContent();
                if (userSubscriptionModelList != null) {
                    for (UserSubscriptionModel userSubscriptionModel : userSubscriptionModelList) {
                        userSubscriptionModel = subscriptionServiceHelper.updateSSOStatus(userSubscriptionModel);
                        if(userSubscriptionModel.isSsoCommunicated()) {
                            userSubscriptionModel = subscriptionService.saveUserSubscription(userSubscriptionModel, false, null, null, EventEnum.SSO_COMMUNICATION);
                            recordsAffected++;
                            affectedModels.add(userSubscriptionModel);
                        }
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
    @Scheduled(cron = "${sso.communication.cron}")
    public void run() {
        runJob();
    }
}
