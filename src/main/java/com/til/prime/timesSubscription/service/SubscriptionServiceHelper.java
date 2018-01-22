package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.enums.*;
import com.til.prime.timesSubscription.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface SubscriptionServiceHelper {
    UserSubscriptionModel generateInitPurchaseUserSubscription(InitPurchaseRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel lastUserSubscription, UserModel userModel, BigDecimal price, boolean crmRequest, boolean free);
    UserSubscriptionModel generateInitPurchaseUserSubscription(UserDTO userDTO, ChannelEnum channel, PlatformEnum platform, PlanTypeEnum planType, Long durationDays, SubscriptionVariantModel variantModel, UserSubscriptionModel lastUserSubscription, UserModel userModel, BigDecimal price, boolean crmRequest, boolean free);
    UserSubscriptionModel updateGenerateOrderUserSubscription(GenerateOrderRequest request, UserSubscriptionModel userSubscriptionModel);
    UserSubscriptionModel updateSubmitPurchaseUserSubscription(SubmitPurchaseRequest request, UserSubscriptionModel userSubscriptionModel, UserSubscriptionModel lastUserSubscription);
    UserSubscriptionModel updateSSOStatus(UserSubscriptionModel userSubscriptionModel);
    PlanListResponse preparePlanListResponse(PlanListResponse response, List<SubscriptionPlanDTO> subscriptionPlans, ValidationResponse validationResponse);
    InitPurchaseResponse prepareInitPurchaseResponse(InitPurchaseResponse response, UserSubscriptionModel userSubscriptionModel, UserSubscriptionModel lastUserSubscription, ValidationResponse validationResponse);
    GenerateOrderResponse prepareGenerateOrderResponse(GenerateOrderResponse response, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse);
    SubmitPurchaseResponse prepareSubmitPurchaseResponse(SubmitPurchaseResponse response, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse);
    List<UserSubscriptionDTO> generateUserSubscriptionDTOList(List<UserSubscriptionModel> userSubscriptionModelList);
    PurchaseHistoryResponse preparePurchaseHistoryResponse(PurchaseHistoryResponse response, List<UserSubscriptionDTO> userSubscriptionDTOList, ValidationResponse validationResponse);
    CancelSubscriptionResponse prepareCancelSubscriptionResponse(CancelSubscriptionResponse response, BigDecimal refundAmount, ValidationResponse validationResponse);
    GenericResponse prepareTurnOffAutoDebitResponse(GenericResponse response, ValidationResponse validationResponse);
    GenericResponse prepareBlockUnblockResponse(GenericResponse response, ValidationResponse validationResponse);
    BackendSubscriptionResponse prepareBackendSubscriptionResponse(BackendSubscriptionResponse response, Set<ValidationError> validationErrors, List<BackendActivationUserDTO> successList, List<BackendActivationUserDTO> failureList);
    BackendSubscriptionActivationResponse prepareBackendSubscriptionActivationResponse(BackendSubscriptionActivationResponse response, UserSubscriptionModel userSubscription, ValidationResponse validationResponse);
    UserSubscriptionAuditModel getUserSubscriptionAuditModel(UserSubscriptionModel userSubscriptionModel, EventEnum event);
    BigDecimal calculateRefundAmount(UserSubscriptionModel userSubscriptionModel);
    UserSubscriptionModel extendSubscription(UserSubscriptionModel userSubscriptionModel, Long extensionDays);
    ExtendExpiryResponse prepareExtendExpiryResponse(ExtendExpiryResponse response, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse);
    GenericValidationResponse prepareCheckEligibilityResponse(GenericValidationResponse response, ValidationResponse validationResponse, boolean validExecution);
    GenericValidationResponse prepareValidVariantResponse(GenericValidationResponse response, ValidationResponse validationResponse, boolean validExecution);
    CheckStatusResponse prepareCheckStatusResponse(CheckStatusResponse response, boolean external, SubscriptionStatusDTO subscriptionStatusDTO, ValidationResponse validationResponse);
    UserModel getUser(GenericRequest request);
    UserAuditModel getUserAudit(UserModel userModel, EventEnum eventEnum);
    BackendSubscriptionUserModel getBackendSubscriptionUser(BackendSubscriptionUserModel model, BackendActivationUserDTO dto);
    BackendSubscriptionUserAuditModel getBackendSubscriptionUserAudit(BackendSubscriptionUserModel user, EventEnum event);
    boolean renewSubscription(UserSubscriptionModel userSubscriptionModel);
    boolean refundPayment(String orderId, Double refundAmount);
    String shortenUrl(String longUrl);

}
