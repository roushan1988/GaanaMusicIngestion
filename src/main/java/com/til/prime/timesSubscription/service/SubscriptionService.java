package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.model.UserModel;

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
}
