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
    CancelSubscriptionResponse cancelSubscription(CancelSubscriptionRequest request, boolean serverRequest);
    ExtendExpiryResponse extendExpiry(ExtendExpiryRequest request);
    GenericValidationResponse checkEligibility(CheckEligibilityRequest request);
    GenericValidationResponse checkValidVariant(CheckValidVariantRequest request);
    CheckStatusResponse checkStatusViaApp(CheckStatusRequest request);
    CheckStatusResponse checkStatusViaServer(CheckStatusRequest request, boolean external);
    UserModel getOrCreateUser(GenericRequest request);
    UserSubscriptionModel saveUserSubscription(UserSubscriptionModel userSubscriptionModel, boolean retryForOrderId, String ssoId, String ticketId, EventEnum event);
}
