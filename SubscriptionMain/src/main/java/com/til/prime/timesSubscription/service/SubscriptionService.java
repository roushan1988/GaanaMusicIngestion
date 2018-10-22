package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.dto.internal.SubscriptionExpired;
import com.til.prime.timesSubscription.enums.EventEnum;
import com.til.prime.timesSubscription.model.BackendSubscriptionUserModel;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.List;

public interface SubscriptionService {
    PlanListResponse getAllPlans(PlanListRequest request);
    SubscriptionPlanVariantResponse getPlanDetailsByVariant(PlanDetailsRequest request, boolean server);
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
    UserModel getOrCreateUserWithMobileCheck(GenericRequest request, ValidationResponse validationResponse, boolean crmRequest);
    UserSubscriptionModel saveUserSubscription(UserSubscriptionModel userSubscriptionModel, boolean retryForOrderId, EventEnum event, boolean publishStatus, boolean updateSSO);
    UserSubscriptionModel saveUserSubscription(UserSubscriptionModel userSubscriptionModel, boolean retryForOrderId, EventEnum event, boolean publishStatus, boolean updateSSO, boolean ssoIdUpdated, boolean publishDetailsUpdated);
    SubscriptionStatusDTO updateUserStatus(UserSubscriptionModel userSubscriptionModel, UserModel userModel);
    SubscriptionStatusDTO getUserStatusCacheWithUpdateByMobile(String mobile);
    void clearUserStatusCache(String mobile);
    UserModel saveUserModel(UserModel userModel, EventEnum eventEnum, boolean retryForPrimeId);
    SubscriptionStatusDTO updateUserStatus(UserSubscriptionModel userSubscriptionModel);
    GenericResponse updateCacheForMobile(GenericRequest request);
    CustomerSearchCRMResponse customerSearchCRM(CustomerSearchRequest request);
    CustomerDetailsCRMResponse customerDetailsCRM(CustomerSearchRequest request);
    OrderDetailsCRMResponse getOrderDetailsCRM(OrderDetailsRequest request);
    OrderSearchCRMResponse orderSearchCRM(OrderSearchRequest request);
    GenericResponse sendOtpSmsForSSO(SsoOtpRequest request);
    GenericResponse sendOtp(OtpRequest request);
    OtpVerificationResponse verifyOtp(OtpVerificationRequest request);
    BackendSubscriptionUserModel saveBackendSubscriptionUser(BackendSubscriptionUserModel user, EventEnum eventEnum);
    PropertyDataGetResponseCRM getPropertyTableData(PropertyDataRequestCRM request);
    GenericResponse updatePropertyTableData(PropertyDataUpdateRequestCRM request);
    GenericResponse deleteUser(GenericRequest request);
    List<Long> sendReminder(Long days, Long subsId);
    SubscriptionExpired expireSubscription(Long subsId) throws Exception;
    PlanPriceUpdateResponse updatePlanPrice(PlanPriceUpdateRequest request, boolean bypassValidation);
    void reloadPriceCacheViaUrl();
    GenericResponse reloadPlanCache(PlanCacheReloadRequest request);
    void saveUserSubscriptionAuditWithExternalUpdates(UserSubscriptionModel userSubscriptionModel, SubscriptionStatusDTO statusDTO, EventEnum event, boolean publishStatus, boolean updateSSO, boolean ssoIdUpdated, boolean publishDetailsUpdated);
    // don't call below method from anywhere except from saveSubscription or saveUser, this is just for async implementation
    void saveUserAuditAsync(UserModel userModel, EventEnum event);
}
