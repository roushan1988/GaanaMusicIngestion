package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.model.SubscriptionVariantModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

public interface SubscriptionValidationService {
    ValidationResponse validateAllPlans(PlanListRequest request);
    ValidationResponse validatePreInitPurchasePlan(InitPurchaseRequest request);
    ValidationResponse validatePostInitPurchasePlan(InitPurchaseRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel restrictedModel, UserSubscriptionModel lastUserSubscription, ValidationResponse validationResponse);
    ValidationResponse validatePreInitGenerateOrder(GenerateOrderRequest request);
    ValidationResponse validatePostInitGenerateOrder(GenerateOrderRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel userSubscriptionModel, UserSubscriptionModel restrictedModel, ValidationResponse validationResponse);
    ValidationResponse validatePreSubmitPurchasePlan(SubmitPurchaseRequest request);
    ValidationResponse validatePostSubmitPurchasePlan(SubmitPurchaseRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse);
    ValidationResponse validatePurchaseHistory(PurchaseHistoryRequest request);
    ValidationResponse validatePreCancelSubscription(CancelSubscriptionRequest request);
    ValidationResponse validatePostCancelSubscription(CancelSubscriptionRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse);
    ValidationResponse validatePreExtendExpiry(ExtendExpiryRequest request);
    ValidationResponse validatePostExtendExpiry(ExtendExpiryRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse);
    ValidationResponse validatePreCheckEligibility(CheckEligibilityRequest request);
    ValidationResponse validatePostCheckEligibility(CheckEligibilityRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel lastModel, UserSubscriptionModel restrictedModel, ValidationResponse validationResponse);
    ValidationResponse validateUser(GenericRequest request, ValidationResponse validationResponse);
    ValidationResponse validateCredentials(GenericRequest request, ValidationResponse validationResponse);
}
