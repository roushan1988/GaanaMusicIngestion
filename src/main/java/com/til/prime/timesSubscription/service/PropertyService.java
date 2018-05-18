package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.GenericResponse;
import com.til.prime.timesSubscription.dto.external.PropertyDataUpdateRequestCRM;
import com.til.prime.timesSubscription.dto.external.SubscriptionPlanDTO;
import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.CountryEnum;
import com.til.prime.timesSubscription.enums.PropertyEnum;
import com.til.prime.timesSubscription.model.ExternalClientModel;
import com.til.prime.timesSubscription.model.SubscriptionPlanModel;
import com.til.prime.timesSubscription.model.SubscriptionVariantModel;

import java.util.List;
import java.util.Map;

public interface PropertyService {
    void reload();
    Object getProperty(PropertyEnum propertyEnum);
    List<Long> getSubscriptionRenewalReminderDays();
    List<Long> getSubscriptionExpiryReminderDays();
    List<Long> getExpiredSubscriptionRenewalReminderDays();
    ExternalClientModel getExternalClient(String clientId);
    SubscriptionVariantModel getBackendFreeTrialVariant(BusinessEnum business, CountryEnum country);
    List<SubscriptionPlanDTO> getAllPlans(BusinessEnum business, CountryEnum country);
    List<SubscriptionPlanModel> getAllPlanModels(BusinessEnum business, CountryEnum country);
    Map<PropertyEnum, String> getPropertyTableData();
    GenericResponse updatePropertyTableData(PropertyDataUpdateRequestCRM request);
}
