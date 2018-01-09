package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.List;

public interface CommunicationService {
    void sendUserSuspensionCommunication(UserModel user, List<UserSubscriptionModel> relevantSubscriptions);
    void sendUserCreationWithExistingMobileCommunication(UserModel user, List<UserSubscriptionModel> relevantSubscriptions);
    void sendSubscriptionExpiryCommunication(UserSubscriptionModel userSubscription);
    void sendSubscriptionExpiryExtensionCommunication(UserSubscriptionModel userSubscription);
    void sendSubscriptionCancellationCommunication(UserSubscriptionModel userSubscription);
    void sendSubscriptionActiveCommunication(UserSubscriptionModel userSubscription);
    void sendSubscriptionSuccessCommunication(UserSubscriptionModel userSubscription);
}
