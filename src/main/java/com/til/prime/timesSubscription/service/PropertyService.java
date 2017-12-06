package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.SubscriptionPlanDTO;
import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.CountryEnum;
import com.til.prime.timesSubscription.enums.PropertyEnum;
import com.til.prime.timesSubscription.model.ExternalClientModel;
import com.til.prime.timesSubscription.model.SubscriptionPlanModel;

import java.util.List;

public interface PropertyService {
    void reload();
    Object getProperty(PropertyEnum propertyEnum);
    List<Long> getSubscriptionRenewalReminderDays();
    List<Long> getSubscriptionExpiryReminderDays();
    List<Long> getExpiredSubscriptionRenewalReminderDays();
    ExternalClientModel getExternalClient(String clientId);
    List<SubscriptionPlanDTO> getAllPlans(BusinessEnum business, CountryEnum country);
    List<SubscriptionPlanModel> getAllPlanModels(BusinessEnum business, CountryEnum country);
}
