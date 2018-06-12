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
    UserSubscriptionModel saveUserSubscription(UserSubscriptionModel userSubscriptionModel, boolean retryForOrderId, EventEnum event, boolean publishStatus);
    void updateUserStatus(UserSubscriptionModel userSubscriptionModel, UserModel userModel);
    UserModel saveUserModel(UserModel userModel, EventEnum eventEnum, boolean retryForPrimeId);
    void updateUserStatus(UserSubscriptionModel userSubscriptionModel);
    GenericResponse updateCacheForMobile(GenericRequest request);
    CustomerSearchCRMResponse customerSearchCRM(CustomerSearchRequest request);
    CustomerDetailsCRMResponse customerDetailsCRM(CustomerSearchRequest request);
    OrderDetailsCRMResponse getOrderDetailsCRM(OrderDetailsRequest request);
    OrderSearchCRMResponse orderSearchCRM(OrderSearchRequest request);
    GenericResponse sendOtp(OtpRequest request);
    OtpVerificationResponse verifyOtp(OtpVerificationRequest request);
    BackendSubscriptionUserModel saveBackendSubscriptionUser(BackendSubscriptionUserModel user, EventEnum eventEnum);
    PropertyDataGetResponseCRM getPropertyTableData(PropertyDataRequestCRM request);
    GenericResponse updatePropertyTableData(PropertyDataUpdateRequestCRM request);
    GenericResponse deleteUser(GenericRequest request);
}
