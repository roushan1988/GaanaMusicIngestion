package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.enums.PropertyEnum;
import com.til.prime.timesSubscription.model.ExternalClientModel;

import java.util.List;

public interface PropertyService {
    void reload();
    Object getProperty(PropertyEnum propertyEnum);
    List<Long> getSubscriptionRenewalReminderDays();
    List<Long> getSubscriptionExpiryReminderDays();
    List<Long> getExpiredSubscriptionRenewalReminderDays();
    ExternalClientModel getExternalClient(String clientId);
}
