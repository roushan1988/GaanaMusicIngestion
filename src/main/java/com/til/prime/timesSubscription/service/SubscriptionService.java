package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.enums.EventEnum;
import com.til.prime.timesSubscription.model.BackendSubscriptionUserModel;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

public interface SubscriptionService {
    PlanListResponse getAllPlans(PlanListRequest request);
    InitPurchaseResponse initPurchasePlan(InitPurchaseRequest request);
    InitPurchaseResponse initPurchasePlan(InitPurchaseRequest request, boolean crmRequest, boolean isFree);
    GenerateOrderResponse generateOrder(GenerateOrderRequest request);
    SubmitPurchaseResponse submitPurchasePlan(SubmitPurchaseRequest request);
    PurchaseHistoryResponse getPurchaseHistory(PurchaseHistoryRequest request);
    CancelSubscriptionResponse cancelSubscription(CancelSubscriptionRequest request, boolean serverRequest);
    GenericResponse turnOffAutoDebit(TurnOffAutoDebitRequest request);
    GenericResponse blockUnblockUser(BlockUnblockRequest request);
    ExtendExpiryResponse extendExpiry(ExtendExpiryRequest request);
    GenericValidationResponse checkEligibility(CheckEligibilityRequest request);
    GenericValidationResponse checkValidVariant(CheckValidVariantRequest request);
    CheckStatusResponse checkStatusViaApp(CheckStatusRequest request);
    CheckStatusResponse checkStatusViaServer(CheckStatusRequest request, boolean external);
    BackendSubscriptionResponse backendSubscriptionViaServer(BackendSubscriptionRequest request);
    BackendSubscriptionActivationResponse backendSubscriptionActivation(BackendSubscriptionActivationRequest request);
    UserModel getOrCreateUserWithMobileCheck(GenericRequest request, ValidationResponse validationResponse);
    UserSubscriptionModel saveUserSubscription(UserSubscriptionModel userSubscriptionModel, boolean retryForOrderId, String ssoId, String ticketId, EventEnum event, boolean communicateSSO);
    void updateUserStatus(UserSubscriptionModel userSubscriptionModel, UserModel userModel);
    UserModel saveUserModel(UserModel userModel, EventEnum eventEnum);
    void updateUserStatus(UserSubscriptionModel userSubscriptionModel);
    GenericResponse sendOtp(OtpRequest request);
    GenericResponse verifyOtp(OtpVerificationRequest request);
    String updateCacheForMobile(UpdateCacheForMobileRequest request);
    CustomerSearchDTOs customerSearchCRM(CustomerSearchRequest request);
    CustomerCRM customerDetailsCRM(CustomerSearchRequest request);
    OrderDetailsCRM getOrderDetailsCRM(OrderDetailsRequest request);
    OrderSearchResultsCRM orderSearchCRM(OrderSearchRequest request);
    BackendSubscriptionUserModel saveBackendSubscriptionUser(BackendSubscriptionUserModel user, EventEnum eventEnum);
}
