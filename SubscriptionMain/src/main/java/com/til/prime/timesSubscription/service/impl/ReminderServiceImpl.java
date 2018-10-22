package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.enums.EventEnum;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.service.ReminderService;
import org.springframework.stereotype.Service;

@Service
public class ReminderServiceImpl implements ReminderService {
    @Override
    public boolean sendReminder(UserSubscriptionModel userSubscriptionModel, EventEnum eventEnum) {
        return false;
    }
}
