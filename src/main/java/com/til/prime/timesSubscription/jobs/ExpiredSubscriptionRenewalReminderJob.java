package com.til.prime.timesSubscription.jobs;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dao.UserSubscriptionRepository;
import com.til.prime.timesSubscription.dto.internal.AffectedModelDetails;
import com.til.prime.timesSubscription.dto.internal.JobDetails;
import com.til.prime.timesSubscription.enums.EventEnum;
import com.til.prime.timesSubscription.enums.JobKeyEnum;
import com.til.prime.timesSubscription.enums.StatusEnum;
import com.til.prime.timesSubscription.model.JobModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.service.PropertyService;
import com.til.prime.timesSubscription.service.ReminderService;
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
public class ExpiredSubscriptionRenewalReminderJob extends AbstractJob {
    private final JobKeyEnum jobKeyEnum = JobKeyEnum.EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ReminderService reminderService;
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
            List<Long> days = propertyService.getExpiredSubscriptionRenewalReminderDays();
            if(days!=null){
                for(Long day: days){
                    List<Long> successList = executeForExpiredRenewalInDays(date, day);
                    affectedModels.addAll(successList);
                    recordsAffected += successList.size();
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
    @Scheduled(cron = "${expired.subscription.renewal.reminder.cron}")
    public void run() {
        runJob();
    }

    private List<Long> executeForExpiredRenewalInDays(Date jobStartDate, Long days) {
        Date date = TimeUtils.addDaysInDate(jobStartDate, days.intValue());
        Date startDate = TimeUtils.getDayStartTime(date);
        Date endDate = TimeUtils.addDaysInDate(startDate, 1);
        List<Long> idList = new ArrayList<>();
        Long count = userSubscriptionRepository.countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum.EXPIRED, startDate, endDate);
        for (Long x = 0l; x <= (Math.max(0l, count - 1) / GlobalConstants.CRON_BATCH_PROCESSING_COUNT); x++) {
            Page<UserSubscriptionModel> userSubscriptionModels = userSubscriptionRepository.findByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalFalse(StatusEnum.EXPIRED, startDate, endDate, new PageRequest(x.intValue(), GlobalConstants.CRON_BATCH_PROCESSING_COUNT.intValue()));
            List<UserSubscriptionModel> userSubscriptionModelList = userSubscriptionModels.getContent();
            for(UserSubscriptionModel userSubscriptionModel: userSubscriptionModelList){
                boolean reminderSent = reminderService.sendReminder(userSubscriptionModel, EventEnum.EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER);
                if (reminderSent) {
                    idList.add(userSubscriptionModel.getId());
                }
            }
        }
        return idList;
    }
}
