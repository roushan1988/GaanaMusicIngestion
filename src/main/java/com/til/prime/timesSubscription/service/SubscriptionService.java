package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.enums.EventEnum;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

public interface SubscriptionService {

    PlanListResponse getAllPlans(PlanListRequest request);
    InitPurchaseResponse initPurchasePlan(InitPurchaseRequest request);
    GenerateOrderResponse generateOrder(GenerateOrderRequest request);
    SubmitPurchaseResponse submitPurchasePlan(SubmitPurchaseRequest request);
    PurchaseHistoryResponse getPurchaseHistory(PurchaseHistoryRequest request);
    CancelSubscriptionResponse cancelSubscription(CancelSubscriptionRequest request);
    ExtendExpiryResponse extendExpiry(ExtendExpiryRequest request);
    GenericResponse checkEligibility(CheckEligibilityRequest request);
    UserModel getOrCreateUser(GenericRequest request);
    UserSubscriptionModel saveUserSubscription(UserSubscriptionModel userSubscriptionModel, boolean retryForOrderId, String ssoId, String ticketId, EventEnum event);
}
