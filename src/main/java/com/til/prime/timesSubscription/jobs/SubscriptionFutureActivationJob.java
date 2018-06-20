package com.til.prime.timesSubscription.jobs;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dao.UserSubscriptionAuditRepository;
import com.til.prime.timesSubscription.dao.UserSubscriptionRepository;
import com.til.prime.timesSubscription.dto.internal.AffectedModelDetails;
import com.til.prime.timesSubscription.dto.internal.JobDetails;
import com.til.prime.timesSubscription.enums.EventEnum;
import com.til.prime.timesSubscription.enums.JobKeyEnum;
import com.til.prime.timesSubscription.enums.StatusEnum;
import com.til.prime.timesSubscription.model.JobModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.service.CommunicationService;
import com.til.prime.timesSubscription.service.SubscriptionService;
import com.til.prime.timesSubscription.service.SubscriptionServiceHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionFutureActivationJob extends AbstractJob {
    private final JobKeyEnum jobKeyEnum = JobKeyEnum.SUBSCRIPTION_FUTURE_ACTIVATION;
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
            int page = 0;
            loop1:
            while(true) {
                List<UserSubscriptionModel> userSubscriptions = userSubscriptionRepository.findUserSubscriptionsForFutureActivation(date, StatusEnum.ACTIVE, StatusEnum.FUTURE, new PageRequest(page++, GlobalConstants.DEFAULT_PAGE_SIZE));
                if(CollectionUtils.isEmpty(userSubscriptions)){
                    break loop1;
                }
                loop:
                for (UserSubscriptionModel userSubscriptionModel : userSubscriptions) {
                    try {
                        if(userSubscriptionModel.getUser().isDeleted()){
                            continue loop;
                        }
                        userSubscriptionModel.setStatus(StatusEnum.ACTIVE);
                        userSubscriptionModel.setStatusDate(new Date());
                        subscriptionService.saveUserSubscription(userSubscriptionModel, false, EventEnum.USER_SUBSCRIPTION_ACTIVE, true);
                        communicationService.sendExistingSubscriptionActivationCommunication(userSubscriptionModel);
                        recordsAffected++;
                        affectedModels.add(userSubscriptionModel.getId());
                    }catch (Exception e){
                        LOG.error("Exception in SubscriptionFutureActivationJob, userSubscription id: "+userSubscriptionModel.getId(), e);
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
    @Scheduled(cron = "${subscription.future.activation.cron}")
    public void run() {
        runJob();
    }
}
