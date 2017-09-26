package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.enums.EventEnum;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

public interface ReminderService {
    boolean sendReminder(UserSubscriptionModel userSubscriptionModel, EventEnum eventEnum);
}
