package com.til.prime.timesSubscription.jobs;

import com.til.prime.timesSubscription.dto.internal.JobDetails;
import com.til.prime.timesSubscription.enums.JobKeyEnum;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionRenewalReminderJob extends AbstractJob {
    private final JobKeyEnum jobKeyEnum = JobKeyEnum.SUBSCRIPTION_RENEWAL_REMINDER;
    @Override
    public JobKeyEnum getJobKey() {
        return jobKeyEnum;
    }

    @Override
    public JobDetails execute() {
        return null;
    }

    @Override
    @Scheduled(cron = "${subscription.renewal.reminder.cron}")
    public void run() {
        runJob();
    }
}
