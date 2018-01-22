package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.SMSTask;
import com.til.prime.timesSubscription.model.BackendSubscriptionUserModel;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.List;

public interface CommunicationServiceHelper {
    EmailTask getUserMobileUpdateEmailTask(UserModel userModel, List<UserSubscriptionModel> userSubscriptionModels);

    EmailTask getSubscriptionSuccessEmailTask(UserSubscriptionModel userSubscription);

    SMSTask getSubscriptionSuccessSMSTask(UserSubscriptionModel userSubscription);

    SMSTask getBackendActivationSMSTask(BackendSubscriptionUserModel user);

    EmailTask getBackendActivationEmailTask(BackendSubscriptionUserModel user);

    SMSTask getFreeTrialSubscriptionSMSTask(UserSubscriptionModel userSubscriptionModel);

    EmailTask getFreeTrialSubscriptionEmailTask(UserSubscriptionModel userSubscriptionModel);

    SMSTask getFirstTimePurchaseSMSTask(UserSubscriptionModel userSubscriptionModel);

    EmailTask getFirstTimePurchaseEmailTask(UserSubscriptionModel userSubscriptionModel);

    SMSTask getFreeTrialExpiryReminderSMSTask(UserSubscriptionModel userSubscriptionModel, Long days);

    EmailTask getFreeTrialExpiryReminderEmailTask(UserSubscriptionModel userSubscriptionModel, Long days);

    SMSTask getFreeTrialExpiredSMSTask(UserSubscriptionModel userSubscriptionModel);

    EmailTask getFreeTrialExpiredEmailTask(UserSubscriptionModel userSubscriptionModel);

    SMSTask getSubscriptionRenewalReminderSMSTask(UserSubscriptionModel userSubscriptionModel, boolean autoDebit);

    EmailTask getSubscriptionRenewalReminderEmailTask(UserSubscriptionModel userSubscriptionModel, boolean autoDebit);

    SMSTask getSubscriptionExpiredSMSTask(UserSubscriptionModel userSubscriptionModel);

    EmailTask getSubscriptionExpiredEmailTask(UserSubscriptionModel userSubscriptionModel);


    SMSTask getSubscriptionRenewedSMSTask(UserSubscriptionModel userSubscriptionModel);

    EmailTask getSubscriptionRenewedEmailTask(UserSubscriptionModel userSubscriptionModel);

    SMSTask getSubscriptionExtensionSMSTask(UserSubscriptionModel userSubscriptionModel);

    EmailTask getSubscriptionExtensionEmailTask(UserSubscriptionModel userSubscriptionModel);


}
