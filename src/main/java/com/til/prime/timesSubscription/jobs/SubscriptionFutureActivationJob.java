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
        Date later = TimeUtils.addMillisInDate(date, GlobalConstants.MILLIS_IN_A_DAY+900000);
        try {
            JobModel jobModel = jobRepository.findByJobKey(jobKeyEnum);
            jobDetails.setJob(jobModel);
            jobDetails.setStartTime(new Date());
            Long count = userSubscriptionRepository.countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum.EXPIRED, date, later);
            for (Long x = 0l; x <= (Math.max(0l, count - 1) / GlobalConstants.CRON_BATCH_PROCESSING_COUNT); x++) {
                Page<UserSubscriptionModel> page = userSubscriptionRepository.findByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum.EXPIRED, date, later, new PageRequest(x.intValue(), GlobalConstants.CRON_BATCH_PROCESSING_COUNT.intValue()));
                List<UserSubscriptionModel> userSubscriptionModelList = page.getContent();
                if (userSubscriptionModelList != null) {
                    for (UserSubscriptionModel userSubscriptionModel : userSubscriptionModelList) {
                        UserSubscriptionModel userSubscriptionModel1 = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrueOrderById(
                                userSubscriptionModel.getUser().getMobile(), StatusEnum.FUTURE, TimeUtils.addMillisInDate(userSubscriptionModel.getEndDate(), -2000));
                        if(userSubscriptionModel1!=null){
                            userSubscriptionModel1.setStatus(StatusEnum.ACTIVE);
                            subscriptionService.saveUserSubscription(userSubscriptionModel1, false, EventEnum.USER_SUBSCRIPTION_ACTIVE, true);
                            subscriptionService.updateUserStatus(userSubscriptionModel1, userSubscriptionModel1.getUser());
                            communicationService.sendPaidSubscriptionSuccessCommunication(userSubscriptionModel1);
                            recordsAffected++;
                            affectedModels.add(userSubscriptionModel1.getId());
                        }
                    }
                }
            }
            Long count1 = userSubscriptionRepository.countByStatusAndEndDateBeforeAndDeletedFalseAndOrderCompletedTrue(StatusEnum.FUTURE, date);
            for (Long x = 0l; x <= (Math.max(0l, count1 - 1) / GlobalConstants.CRON_BATCH_PROCESSING_COUNT); x++) {
                Page<UserSubscriptionModel> page = userSubscriptionRepository.findByStatusAndEndDateBeforeAndDeletedFalseAndOrderCompletedTrue(StatusEnum.FUTURE, date, new PageRequest(x.intValue(), GlobalConstants.CRON_BATCH_PROCESSING_COUNT.intValue()));
                List<UserSubscriptionModel> userSubscriptionModelList = page.getContent();
                if (userSubscriptionModelList != null) {
                    for (UserSubscriptionModel userSubscriptionModel : userSubscriptionModelList) {
                        Long userCount = userSubscriptionRepository.countByUserMobileAndUserDeletedFalseAndStatusAndDeletedFalseAndOrderCompletedTrue(
                                userSubscriptionModel.getUser().getMobile(), StatusEnum.ACTIVE);
                        if(userCount==null || userCount<=0){
                            userSubscriptionModel.setStatus(StatusEnum.ACTIVE);
                            subscriptionService.saveUserSubscription(userSubscriptionModel, false, EventEnum.USER_SUBSCRIPTION_ACTIVE, true);
                            subscriptionService.updateUserStatus(userSubscriptionModel, userSubscriptionModel.getUser());
                            communicationService.sendPaidSubscriptionSuccessCommunication(userSubscriptionModel);
                            recordsAffected++;
                            affectedModels.add(userSubscriptionModel.getId());
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
    @Scheduled(cron = "${subscription.future.activation.cron}")
    public void run() {
        runJob();
    }
}
