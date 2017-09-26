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
import com.til.prime.timesSubscription.model.UserSubscriptionAuditModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
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
                        userSubscriptionModel.setStatus(StatusEnum.EXPIRED);
                        userSubscriptionModel = subscriptionService.saveUserSubscription(userSubscriptionModel, false, null, null, EventEnum.USER_SUBSCRIPTION_EXPIRY);
                        UserSubscriptionModel userSubscriptionModel1 = userSubscriptionRepository.findFirstByUserSsoIdAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrueOrderById(
                                userSubscriptionModel.getUser().getSsoId(), StatusEnum.FUTURE, TimeUtils.addMillisInDate(userSubscriptionModel.getEndDate(), -2000));
                        if(userSubscriptionModel1!=null){
                            userSubscriptionModel1.setStatus(StatusEnum.ACTIVE);
                            subscriptionService.saveUserSubscription(userSubscriptionModel1, false, userSubscriptionModel1.getUser().getSsoId(), userSubscriptionModel1.getTicketId(), EventEnum.USER_SUBSCRIPTION_ACTIVE);
                            recordsAffected++;
                            affectedModels.add(userSubscriptionModel1.getId());
                        }else if(userSubscriptionModel.getSubscriptionVariant().isRecurring() && userSubscriptionModel.isAutoRenewal()){
                            LOG.info("Initiating SUBSCRIPTION RENEWAL for userSubscriptionId: "+userSubscriptionModel.getId()+", orderId: "+userSubscriptionModel.getOrderId());
                            boolean success = subscriptionServiceHelper.renewSubscription(userSubscriptionModel);
                            if(success){
                                UserSubscriptionModel userSubscriptionModel2 = userSubscriptionRepository.findFirstByUserSsoIdAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrueOrderById(
                                        userSubscriptionModel.getUser().getSsoId(), StatusEnum.ACTIVE, TimeUtils.addMillisInDate(userSubscriptionModel.getEndDate(), -2000));
                                if(userSubscriptionModel2!=null){
                                    UserSubscriptionAuditModel auditModel = subscriptionServiceHelper.getUserSubscriptionAuditModel(userSubscriptionModel2, EventEnum.SUBSCRIPTION_AUTO_RENEWAL);
                                    userSubscriptionAuditRepository.save(auditModel);
                                    recordsAffected++;
                                    affectedModels.add(userSubscriptionModel2.getId());
                                }
                            }
                        }
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
