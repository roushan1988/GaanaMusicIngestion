package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.model.BackendSubscriptionUserModel;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.List;

public interface CommunicationService {
    void sendUserSuspensionCommunication(UserModel user, List<UserSubscriptionModel> relevantSubscriptions);

    void sendUserCreationWithExistingMobileCommunication(UserModel user, List<UserSubscriptionModel> relevantSubscriptions);

    void sendSubscriptionCancellationCommunication(UserSubscriptionModel userSubscription);

    void sendSubscriptionActiveCommunication(UserSubscriptionModel userSubscription);

    void sendBackendActivationPendingCommunication(BackendSubscriptionUserModel model);

    void sendFreeTrialSubscriptionSuccessCommunication(UserSubscriptionModel userSubscription);

    void sendPaidSubscriptionSuccessCommunication(UserSubscriptionModel userSubscription);

    void sendExistingSubscriptionActivationCommunication(UserSubscriptionModel userSubscription);

    void sendFreeTrailExpiryReminderCommunication(UserSubscriptionModel userSubscription, Long days);

    void sendFreeTrailExpiredCommunication(UserSubscriptionModel userSubscription);

    void sendSubscriptionRenewalReminderAutoDebitOnCommunication(UserSubscriptionModel model);

    void sendSubscriptionRenewalReminderAutoDebitOffCommunication(UserSubscriptionModel model);

    void sendSubscriptionExpiredCommunication(UserSubscriptionModel userSubscription);

    void sendSubscriptionRenewedCommunication(UserSubscriptionModel model);

    void sendSubscriptionExpiryExtensionCommunication(UserSubscriptionModel userSubscription);

}
