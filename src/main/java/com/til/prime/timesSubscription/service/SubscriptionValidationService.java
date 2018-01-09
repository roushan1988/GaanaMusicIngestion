package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.model.SubscriptionVariantModel;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.List;

public interface SubscriptionValidationService {
    ValidationResponse validatePreAllPlans(PlanListRequest request);
    ValidationResponse validatePostAllPlans(UserModel userModel, ValidationResponse validationResponse);
    ValidationResponse validatePreInitPurchasePlan(InitPurchaseRequest request, boolean crmRequest);
    ValidationResponse validatePostInitPurchasePlan(InitPurchaseRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel restrictedModel, UserSubscriptionModel lastUserSubscription, boolean crmRequest, ValidationResponse validationResponse);
    ValidationResponse validatePreGenerateOrder(GenerateOrderRequest request);
    ValidationResponse validatePostGenerateOrder(GenerateOrderRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel userSubscriptionModel, UserSubscriptionModel restrictedModel, ValidationResponse validationResponse);
    ValidationResponse validatePreSubmitPurchasePlan(SubmitPurchaseRequest request);
    ValidationResponse validatePostSubmitPurchasePlan(SubmitPurchaseRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse);
    ValidationResponse validatePurchaseHistory(PurchaseHistoryRequest request);
    ValidationResponse validatePreCancelSubscription(CancelSubscriptionRequest request, boolean serverRequest);
    ValidationResponse validatePostCancelSubscription(CancelSubscriptionRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse);
    ValidationResponse validatePreTurnOffAutoDebit(TurnOffAutoDebitRequest request);
    ValidationResponse validatePreBlockUnblockUser(BlockUnblockRequest request);
    ValidationResponse validatePostBlockUnblockUser(BlockUnblockRequest request, UserModel userModel, ValidationResponse validationResponse);
    ValidationResponse validatePostTurnOffAutoDebit(TurnOffAutoDebitRequest request, List<UserSubscriptionModel> userSubscriptionModelList, ValidationResponse validationResponse);
    ValidationResponse validatePreExtendExpiry(ExtendExpiryRequest request);
    ValidationResponse validatePostExtendExpiry(ExtendExpiryRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse);
    ValidationResponse validatePreCheckEligibility(CheckEligibilityRequest request);
    ValidationResponse validatePostCheckEligibility(CheckEligibilityRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel lastModel, UserSubscriptionModel restrictedModel, ValidationResponse validationResponse);
    ValidationResponse validatePreValidVariant(CheckValidVariantRequest request);
    ValidationResponse validatePostValidVariant(CheckValidVariantRequest request, SubscriptionVariantModel model, ValidationResponse validationResponse);
    ValidationResponse validatePreCheckStatusViaApp(CheckStatusRequest request);
    ValidationResponse validatePreCheckStatusViaServer(CheckStatusRequest request, boolean external);
    ValidationResponse validatePostCheckStatus(CheckStatusRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse);
    ValidationResponse validateUser(GenericRequest request, ValidationResponse validationResponse);
    ValidationResponse validateCredentials(GenericRequest request, ValidationResponse validationResponse);
    ValidationResponse validateEncryptionForSubmitPurchase(SubmitPurchaseRequest request, ValidationResponse validationResponse);
    ValidationResponse validateEncryptionForOrder(GenerateOrderRequest request, ValidationResponse validationResponse);
    ValidationResponse validateEncryptionForCheckStatus(CheckStatusRequest request, ValidationResponse validationResponse, boolean external);
    ValidationResponse validateEncryptionForValidVariant(CheckValidVariantRequest request, ValidationResponse validationResponse);
    ValidationResponse validateBlockedUser(UserModel userModel, ValidationResponse validationResponse);
}
