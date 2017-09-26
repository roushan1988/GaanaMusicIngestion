package com.til.prime.timesSubscription.service;

import java.util.List;

public interface PropertyService {
    void reload();
    List<Long> getSubscriptionRenewalReminderDays();
    List<Long> getSubscriptionExpiryReminderDays();
    List<Long> getExpiredSubscriptionRenewalReminderDays();
}
