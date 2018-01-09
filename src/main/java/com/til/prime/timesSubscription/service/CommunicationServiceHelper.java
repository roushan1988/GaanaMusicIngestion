package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.SMSTask;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.List;

public interface CommunicationServiceHelper {
    EmailTask getUserMobileUpdateEmailTask(UserModel userModel, List<UserSubscriptionModel> userSubscriptionModels);
    EmailTask getSubscriptionSuccessEmailTask(UserSubscriptionModel userSubscription);
    SMSTask getSubscriptionSuccessSMSTask(UserSubscriptionModel userSubscription);
}
