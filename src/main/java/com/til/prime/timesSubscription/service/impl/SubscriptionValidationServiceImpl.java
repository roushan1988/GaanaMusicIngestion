package com.til.prime.timesSubscription.service.impl;

import com.google.gson.Gson;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.enums.*;
import com.til.prime.timesSubscription.model.ExternalClientModel;
import com.til.prime.timesSubscription.model.SubscriptionVariantModel;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.service.ChecksumService;
import com.til.prime.timesSubscription.service.PropertyService;
import com.til.prime.timesSubscription.service.SubscriptionValidationService;
import com.til.prime.timesSubscription.util.PreConditions;
import com.til.prime.timesSubscription.util.RestTemplateUtil;
import com.til.prime.timesSubscription.util.TimeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SubscriptionValidationServiceImpl implements SubscriptionValidationService {

    private static final Logger LOG = Logger.getLogger(SubscriptionValidationServiceImpl.class);
    @Autowired
    private RestTemplateUtil restTemplateUtil;
    @Autowired
    private ChecksumService checksumService;
    @Resource(name = "config_properties")
    private Properties properties;
    @Autowired
    PropertyService propertyService;

    @Override
    public ValidationResponse validatePreAllPlans(PlanListRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        if(request.getUser()!=null){
            validationResponse = validateUser(request, validationResponse);
        }
        PreConditions.notNull(request.getBusiness(), ValidationError.INVALID_BUSINESS, validationResponse);
        PreConditions.notNull(request.getCountry(), ValidationError.INVALID_COUNTRY, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostAllPlans(UserModel userModel, ValidationResponse validationResponse) {
        if(userModel!=null){
            PreConditions.mustBeFalse(userModel.isBlocked(), ValidationError.BLOCKED_USER, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreInitPurchasePlan(InitPurchaseRequest request, boolean crmRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        if(!crmRequest){
            validationResponse = validateUser(request, validationResponse);
        }
        PreConditions.notNull(request.getPlanId(), ValidationError.INVALID_PLAN_ID, validationResponse);
        PreConditions.notNull(request.getVariantId(), ValidationError.INVALID_VARIANT_ID, validationResponse);
        PreConditions.notNull(request.getPrice(), ValidationError.INVALID_PRICE, validationResponse);
        PreConditions.notNull(request.getDurationDays(), ValidationError.INVALID_DURATION_DAYS, validationResponse);
        PreConditions.notNullEnumCheck(request.getPlanType(), PlanTypeEnum.names(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
        PreConditions.notNullEnumCheck(request.getBusiness(), BusinessEnum.names(), ValidationError.INVALID_BUSINESS, validationResponse);
        PreConditions.notNullEnumCheck(request.getChannel(), ChannelEnum.names(), ValidationError.INVALID_CHANNEL, validationResponse);
        PreConditions.notNullEnumCheck(request.getPlatform(), PlatformEnum.names(), ValidationError.INVALID_PLATFORM, validationResponse);
        validationResponse = updateValid(validationResponse);
        if(validationResponse.isValid() && crmRequest){
            validationResponse = validateEncryptionForInitPurchaseSubscription(request, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostInitPurchasePlan(InitPurchaseRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel restrictedModel, UserSubscriptionModel lastUserSubscription, boolean crmRequest, ValidationResponse validationResponse) {
        PreConditions.notNull(variantModel, ValidationError.INVALID_SUBSCRIPTION_VARIANT, validationResponse);
        PreConditions.mustBeNull(restrictedModel, ValidationError.USER_PLAN_DOES_NOT_QUALIFY, validationResponse);
        if(variantModel!=null) {
            PreConditions.mustBeEqual(request.getPlanType(), variantModel.getPlanType().name(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
        }
        if(lastUserSubscription!=null){
            PreConditions.notGreater(lastUserSubscription.getSubscriptionVariant().getPlanType().getOrder(), PlanTypeEnum.valueOf(request.getPlanType()).getOrder(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
            PreConditions.notAfter(lastUserSubscription.getEndDate(), TimeUtils.addDaysInDate(new Date(), GlobalConstants.MAX_DAYS_DIFF_FOR_NEW_SUBSCRIPTION_PURCHASE), ValidationError.USER_PLAN_DOES_NOT_QUALIFY, validationResponse);
            PreConditions.mustBeFalse(lastUserSubscription.getUser().isBlocked(), ValidationError.BLOCKED_USER, validationResponse);
        }
        if(crmRequest){
            PreConditions.mustBeFalse(lastUserSubscription.getStatus().equals(StatusEnum.FUTURE), ValidationError.USER_PLAN_DOES_NOT_QUALIFY, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreGenerateOrder(GenerateOrderRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse = validateEncryptionForOrder(request, validationResponse);
        PreConditions.mustBeEqual(request.getSecretKey(), properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY), ValidationError.INVALID_SECRET_KEY, validationResponse);
        PreConditions.notNull(request.getUserSubscriptionId(), ValidationError.INVALID_USER_SUBSCRIPTION_ID, validationResponse);
        PreConditions.notNull(request.getPlanId(), ValidationError.INVALID_PLAN_ID, validationResponse);
        PreConditions.notNull(request.getVariantId(), ValidationError.INVALID_VARIANT_ID, validationResponse);
        PreConditions.notNull(request.getPrice(), ValidationError.INVALID_PRICE, validationResponse);
        PreConditions.notNull(request.getDurationDays(), ValidationError.INVALID_DURATION_DAYS, validationResponse);
        PreConditions.notNullEnumCheck(request.getPlanType(), PlanTypeEnum.names(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
        PreConditions.notNullEnumCheck(request.getBusiness(), BusinessEnum.names(), ValidationError.INVALID_BUSINESS, validationResponse);
        PreConditions.notEmpty(request.getPaymentMethod(), ValidationError.INVALID_PAYMENT_METHOD, validationResponse);
        if(request.isRenewal() && !request.isJob()){
            PreConditions.notNullEnumCheck(request.getPlatform(), PlatformEnum.names(), ValidationError.INVALID_PLATFORM, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostGenerateOrder(GenerateOrderRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel userSubscriptionModel, UserSubscriptionModel restrictedModel, ValidationResponse validationResponse) {
        PreConditions.notNull(userSubscriptionModel, ValidationError.INVALID_USER_SUBSCRIPTION_ID, validationResponse);
        PreConditions.notNull(variantModel, ValidationError.INVALID_SUBSCRIPTION_VARIANT, validationResponse);
        if(userSubscriptionModel!=null) {
            PreConditions.mustBeFalse(userSubscriptionModel.getUser().isBlocked(), ValidationError.BLOCKED_USER, validationResponse);
            if (request.isRetryOnFailure() || request.isDuplicate()) {
                PreConditions.notNull(userSubscriptionModel.getOrderId(), ValidationError.ORDER_NOT_GENERATED, validationResponse);
            } else {
                PreConditions.mustBeNull(userSubscriptionModel.getOrderId(), ValidationError.ORDER_ALREADY_GENERATED, validationResponse);
                PreConditions.mustBeFalse(TransactionStatusEnum.COMPLETED_STATES.contains(userSubscriptionModel.getTransactionStatus()), ValidationError.INVALID_TRANSACTION_STATUS, validationResponse);
            }
        }
        if(variantModel!=null) {
            PreConditions.mustBeEqual(request.getBusiness(), userSubscriptionModel.getBusiness().name(), ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
            PreConditions.mustBeEqual(request.getPlanId(), variantModel.getSubscriptionPlan().getId(), ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
            PreConditions.mustBeEqual(request.getVariantId(), variantModel.getId(), ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
            PreConditions.bigDecimalComparisonMustBeEqual(request.getPrice(), variantModel.getPrice(), ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
            PreConditions.mustBeEqual(request.getDurationDays(), variantModel.getDurationDays(), ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
            PreConditions.mustBeEqual(request.getPlanType(), variantModel.getPlanType().name(), ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
        }
        if(userSubscriptionModel!=null){
            PreConditions.mustBeFalse(userSubscriptionModel.isOrderCompleted(), ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
        }
        PreConditions.mustBeNull(restrictedModel, ValidationError.USER_PLAN_DOES_NOT_QUALIFY, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreSubmitPurchasePlan(SubmitPurchaseRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse = validateEncryptionForSubmitPurchase(request, validationResponse);
        PreConditions.mustBeEqual(request.getSecretKey(), properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY), ValidationError.INVALID_SECRET_KEY, validationResponse);
        PreConditions.notNull(request.getUserSubscriptionId(), ValidationError.INVALID_USER_SUBSCRIPTION_ID, validationResponse);
        PreConditions.notEmpty(request.getOrderId(), ValidationError.INVALID_ORDER_ID, validationResponse);
        PreConditions.notNull(request.getVariantId(), ValidationError.INVALID_VARIANT_ID, validationResponse);
        PreConditions.notEmpty(request.getPaymentMethod(), ValidationError.INVALID_PAYMENT_DETAILS, validationResponse);
//        PreConditions.notEmpty(request.getPaymentReference(), ValidationError.INVALID_PAYMENT_DETAILS, validationResponse);
        PreConditions.notNull(request.getPrice(), ValidationError.INVALID_PRICE, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostSubmitPurchasePlan(SubmitPurchaseRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        PreConditions.notNull(userSubscriptionModel, ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
        if(userSubscriptionModel!=null){
            PreConditions.mustBeFalse(userSubscriptionModel.getUser().isBlocked(), ValidationError.BLOCKED_USER, validationResponse);
            PreConditions.mustBeFalse(userSubscriptionModel.isOrderCompleted(), ValidationError.ORDER_ALREADY_COMPLETED, validationResponse);
            PreConditions.mustBeEqual(request.getPaymentMethod(), userSubscriptionModel.getPaymentMethod(), ValidationError.INVALID_PAYMENT_METHOD, validationResponse);
            PreConditions.bigDecimalComparisonMustBeEqual(userSubscriptionModel.getSubscriptionVariant().getPrice(), request.getPrice(), ValidationError.INVALID_PRICE, validationResponse);
            if(request.isAutoRenewal()){
                PreConditions.mustBeTrue(userSubscriptionModel.getSubscriptionVariant().isRecurring(), ValidationError.INVALID_RECURRING_PAYMENT, validationResponse);
            }
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePurchaseHistory(PurchaseHistoryRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse = validateCredentials(request, validationResponse);
        if(StringUtils.isNotEmpty(request.getBusiness())){
            PreConditions.notNullEnumCheck(request.getBusiness(), BusinessEnum.names(), ValidationError.INVALID_BUSINESS, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreCancelSubscription(CancelSubscriptionRequest request, boolean serverRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        if(!serverRequest){
            validationResponse = validateCredentials(request, validationResponse);
        }
        if(validationResponse.isValid()){
            PreConditions.notNull(request.getUserSubscriptionId(), ValidationError.INVALID_USER_SUBSCRIPTION_ID, validationResponse);
            PreConditions.notEmpty(request.getOrderId(), ValidationError.INVALID_ORDER_ID, validationResponse);
            PreConditions.notNull(request.getVariantId(), ValidationError.INVALID_SUBSCRIPTION_VARIANT, validationResponse);
            validationResponse = updateValid(validationResponse);
        }
        if(serverRequest){
            validationResponse = validateEncryptionForCancelSubscription((CancelSubscriptionServerRequest) request, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostCancelSubscription(CancelSubscriptionRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        PreConditions.notNull(userSubscriptionModel, ValidationError.INVALID_USER_SUBSCRIPTION_ID, validationResponse);
        if(userSubscriptionModel!=null){
            PreConditions.notAfter(new Date(), userSubscriptionModel.getEndDate(), ValidationError.ALREADY_EXPIRED, validationResponse);
            PreConditions.mustBeFalse(userSubscriptionModel.getUser().isBlocked(), ValidationError.BLOCKED_USER, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreTurnOffAutoDebit(TurnOffAutoDebitRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        if(validationResponse.isValid()){
            PreConditions.notEmpty(request.getSecretKey(), ValidationError.INVALID_SECRET_KEY, validationResponse);
            PreConditions.notNull(request.getUser(), ValidationError.INVALID_USER, validationResponse);
            if(request.getUser()!=null){
                PreConditions.notEmpty(request.getUser().getMobile(), ValidationError.INVALID_MOBILE, validationResponse);
            }
            validationResponse = updateValid(validationResponse);
        }
        if(validationResponse.isValid()){
            validationResponse = validateEncryptionForTurnOffAutoDebit(request, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreBlockUnblockUser(BlockUnblockRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        PreConditions.notNull(request.getUser(), ValidationError.INVALID_USER, validationResponse);
        if(request.getUser()!=null){
            PreConditions.validMobile(request.getUser().getMobile(), ValidationError.INVALID_MOBILE, validationResponse);
            updateValid(validationResponse);
        }
        if(validationResponse.isValid()){
            validationResponse = validateEncryptionForBlockUnblockUser(request, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostBlockUnblockUser(BlockUnblockRequest request, UserModel userModel, ValidationResponse validationResponse) {
        PreConditions.notNull(userModel, ValidationError.INVALID_USER, validationResponse);
        if(userModel!=null){
            PreConditions.mustBeFalse(request.isBlockUser()==userModel.isBlocked(), request.isBlockUser()? ValidationError.ALREADY_BLOCKED_USER: ValidationError.ALREADY_UNBLOCKED_USER, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostTurnOffAutoDebit(TurnOffAutoDebitRequest request, List<UserSubscriptionModel> userSubscriptionModelList, ValidationResponse validationResponse) {
        PreConditions.notEmpty(userSubscriptionModelList, ValidationError.NO_SUBSCRIPTIONS_FOUND, validationResponse);
        if(CollectionUtils.isNotEmpty(userSubscriptionModelList)){
            PreConditions.mustBeFalse(userSubscriptionModelList.get(0).getUser().isBlocked(), ValidationError.BLOCKED_USER, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreExtendExpiry(ExtendExpiryRequest request) {
        ValidationResponse validationResponse = validatePreCancelSubscription(request, true);
        PreConditions.notNullPositiveCheck(request.getExtensionDays(), ValidationError.INVALID_EXTENSION_DAYS, validationResponse);
        if(request.getExtensionDays()!=null){
            PreConditions.notGreater(request.getExtensionDays(), GlobalConstants.MAX_SUBSCRIPTION_EXTENSION_DAYS, ValidationError.INVALID_EXTENSION_DAYS, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostExtendExpiry(ExtendExpiryRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        PreConditions.notNull(userSubscriptionModel, ValidationError.INVALID_USER_SUBSCRIPTION_ID, validationResponse);
        if(userSubscriptionModel!=null){
            PreConditions.notAfter(new Date(), TimeUtils.addDaysInDate(userSubscriptionModel.getEndDate(), request.getExtensionDays().intValue()), ValidationError.ALREADY_EXPIRED, validationResponse);
            PreConditions.mustBeFalse(userSubscriptionModel.getUser().isBlocked(), ValidationError.BLOCKED_USER, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreCheckEligibility(CheckEligibilityRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse = validateCredentials(request, validationResponse);
        PreConditions.notNull(request.getPlanId(), ValidationError.INVALID_PLAN_ID, validationResponse);
        PreConditions.notNull(request.getVariantId(), ValidationError.INVALID_VARIANT_ID, validationResponse);
        PreConditions.notNullEnumCheck(request.getPlanType(), PlanTypeEnum.names(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
        PreConditions.notNullEnumCheck(request.getBusiness(), BusinessEnum.names(), ValidationError.INVALID_BUSINESS, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostCheckEligibility(CheckEligibilityRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel lastModel, UserSubscriptionModel restrictedModel, ValidationResponse validationResponse) {
        PreConditions.notNull(variantModel, ValidationError.INVALID_SUBSCRIPTION_VARIANT, validationResponse);
        PreConditions.mustBeNull(restrictedModel, ValidationError.USER_PLAN_DOES_NOT_QUALIFY, validationResponse);
        PreConditions.mustBeEqual(request.getPlanType(), variantModel.getPlanType().name(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
        if(lastModel!=null){
            PreConditions.mustBeFalse(lastModel.getUser().isBlocked(), ValidationError.BLOCKED_USER, validationResponse);
            PreConditions.notGreater(lastModel.getSubscriptionVariant().getPlanType().getOrder(), PlanTypeEnum.valueOf(request.getPlanType()).getOrder(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
            PreConditions.notAfter(lastModel.getEndDate(), TimeUtils.addDaysInDate(new Date(), GlobalConstants.MAX_DAYS_DIFF_FOR_NEW_SUBSCRIPTION_PURCHASE), ValidationError.USER_PLAN_DOES_NOT_QUALIFY, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreValidVariant(CheckValidVariantRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        PreConditions.notNullPositiveCheck(request.getVariantId(), ValidationError.INVALID_VARIANT_ID, validationResponse);
        PreConditions.notNullPositiveCheck(request.getPlanId(), ValidationError.INVALID_PLAN_ID, validationResponse);
        PreConditions.notEmpty(request.getVariantName(), ValidationError.INVALID_VARIANT_NAME, validationResponse);
        PreConditions.mustBeEqual(request.getSecretKey(), properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY), ValidationError.INVALID_SECRET_KEY, validationResponse);
        PreConditions.notEmpty(request.getChecksum(), ValidationError.INVALID_CHECKSUM, validationResponse);
        updateValid(validationResponse);
        if(validationResponse.isValid()){
            validationResponse = validateEncryptionForValidVariant(request, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostValidVariant(CheckValidVariantRequest request, SubscriptionVariantModel model, ValidationResponse validationResponse) {
        PreConditions.notNull(model, ValidationError.INVALID_SUBSCRIPTION_VARIANT, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreCheckStatusViaApp(CheckStatusRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse = validateUser(request, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreCheckStatusViaServer(CheckStatusRequest request, boolean external) {
        ValidationResponse validationResponse = new ValidationResponse();
        PreConditions.notNull(request.getUser(), ValidationError.INVALID_USER, validationResponse);
        if(request.getUser()!=null) {
            PreConditions.validMobile(request.getUser().getMobile(), ValidationError.INVALID_MOBILE, validationResponse);
            validationResponse = validateEncryptionForCheckStatus(request, validationResponse, external);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostCheckStatus(CheckStatusRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        PreConditions.notNull(userSubscriptionModel, ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validateUser(GenericRequest request, ValidationResponse validationResponse) {
        validationResponse = validateCredentials(request, validationResponse);
        if(request.getUser()!=null) {
            PreConditions.validMobile(request.getUser().getMobile(), ValidationError.INVALID_MOBILE, validationResponse);
            if (StringUtils.isNotEmpty(request.getUser().getEmail())) {
                PreConditions.validEmail(request.getUser().getEmail(), ValidationError.INVALID_EMAIL, validationResponse);
            }
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validateCredentials(GenericRequest request, ValidationResponse validationResponse) {
        PreConditions.notNull(request.getUser(), ValidationError.USER_DETAILS_MISSING, validationResponse);
        if(request.getUser()!=null){
            PreConditions.notNull(request.getUser().getSsoId(), ValidationError.INVALID_SSO_ID, validationResponse);
            PreConditions.notNull(request.getUser().getTicketId(), ValidationError.INVALID_TICKET_ID, validationResponse);
            if(StringUtils.isNotEmpty(request.getUser().getSsoId()) && StringUtils.isNotEmpty(request.getUser().getTicketId()) && StringUtils.isNotEmpty(request.getUser().getMobile())){
                validateSSOLogin(request.getUser().getSsoId(), request.getUser().getTicketId(), request.getUser().getMobile(), validationResponse);
            }
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validateEncryptionForSubmitPurchase(SubmitPurchaseRequest request, ValidationResponse validationResponse) {
        PreConditions.notEmpty(request.getChecksum(), ValidationError.INVALID_ENCRYPTION, validationResponse);
        PreConditions.mustBeEqual(request.getSecretKey(), properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY), ValidationError.INVALID_SECRET_KEY, validationResponse);
        if(StringUtils.isNotEmpty(request.getChecksum()) && StringUtils.isNotEmpty(request.getSecretKey())) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(request.getSecretKey()).append(request.getUserSubscriptionId()).append(request.getVariantId()).append(request.getOrderId()).append(request.getPrice().doubleValue());
                String checksum = checksumService.calculateChecksumHmacSHA256(properties.getProperty(GlobalConstants.PAYMENTS_ENCRYPTION_KEY), sb.toString());
                PreConditions.mustBeEqual(checksum, request.getChecksum(), ValidationError.INVALID_ENCRYPTION, validationResponse);
            } catch (Exception e) {
                validationResponse.addValidationError(ValidationError.INVALID_ENCRYPTION);
            }
        }
        return updateValid(validationResponse);
    }

    public ValidationResponse validateEncryptionForOrder(GenerateOrderRequest request, ValidationResponse validationResponse) {
        PreConditions.mustBeEqual(request.getSecretKey(), properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY), ValidationError.INVALID_SECRET_KEY, validationResponse);
        PreConditions.notEmpty(request.getChecksum(), ValidationError.INVALID_ENCRYPTION, validationResponse);
        if(StringUtils.isNotEmpty(request.getChecksum()) && StringUtils.isNotEmpty(request.getSecretKey())) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(request.getSecretKey()).append(request.getUserSubscriptionId()).append(request.getPlanId()).append(request.getVariantId()).append(request.getPrice().doubleValue());
                String checksum = checksumService.calculateChecksumHmacSHA256(properties.getProperty(GlobalConstants.PAYMENTS_ENCRYPTION_KEY), sb.toString());
                PreConditions.mustBeEqual(checksum, request.getChecksum(), ValidationError.INVALID_ENCRYPTION, validationResponse);
            } catch (Exception e) {
                validationResponse.addValidationError(ValidationError.INVALID_ENCRYPTION);
            }
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validateEncryptionForCheckStatus(CheckStatusRequest request, ValidationResponse validationResponse, boolean external) {
        if(external) {
            PreConditions.notEmpty(request.getClientId(), ValidationError.INVALID_CLIENT_ID, validationResponse);
        }
        PreConditions.notEmpty(request.getSecretKey(), ValidationError.INVALID_CLIENT_SECRET_KEY, validationResponse);
        PreConditions.notEmpty(request.getChecksum(), ValidationError.INVALID_CHECKSUM, validationResponse);
        ExternalClientModel client = null;
        if(StringUtils.isNotEmpty(request.getClientId()) && StringUtils.isNotEmpty(request.getSecretKey()) && StringUtils.isNotEmpty(request.getChecksum())){
            if(external) {
                client = propertyService.getExternalClient(request.getClientId());
                PreConditions.notNull(client, ValidationError.INVALID_CLIENT_ID, validationResponse);
                if (client != null) {
                    PreConditions.mustBeEqual(request.getSecretKey(), client.getSecretKey(), ValidationError.INVALID_CLIENT_SECRET_KEY, validationResponse);
                }
            }else{
                PreConditions.mustBeEqual(request.getSecretKey(), properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY), ValidationError.INVALID_CLIENT_SECRET_KEY, validationResponse);
            }
        }
        if(validationResponse.getValidationErrorSet().isEmpty()) {
            try {
                StringBuilder sb = new StringBuilder();
                if(external){
                    sb.append(request.getClientId());
                }
                sb.append(request.getSecretKey()).append(request.getUser().getMobile());
                String checksum = checksumService.calculateChecksumHmacSHA256(external? client.getEncryptionKey(): properties.getProperty(GlobalConstants.PAYMENTS_ENCRYPTION_KEY), sb.toString());
                PreConditions.mustBeEqual(checksum, request.getChecksum(), ValidationError.INVALID_ENCRYPTION, validationResponse);
            } catch (Exception e) {
                validationResponse.addValidationError(ValidationError.INVALID_ENCRYPTION);
            }
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validateEncryptionForValidVariant(CheckValidVariantRequest request, ValidationResponse validationResponse) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(request.getSecretKey()).append(request.getPlanId()).append(request.getVariantId()).append(request.getVariantName());
            String checksum = checksumService.calculateChecksumHmacSHA256(properties.getProperty(GlobalConstants.PAYMENTS_ENCRYPTION_KEY), sb.toString());
            PreConditions.mustBeEqual(checksum, request.getChecksum(), ValidationError.INVALID_ENCRYPTION, validationResponse);
        } catch (Exception e) {
            validationResponse.addValidationError(ValidationError.INVALID_ENCRYPTION);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validateBlockedUser(UserModel userModel, ValidationResponse validationResponse) {
        PreConditions.notNull(userModel, ValidationError.INVALID_USER, validationResponse);
        if(userModel!=null){
            PreConditions.mustBeFalse(userModel.isBlocked(), ValidationError.BLOCKED_USER, validationResponse);
        }
        return updateValid(validationResponse);
    }

    private ValidationResponse validateEncryptionForCancelSubscription(CancelSubscriptionServerRequest request, ValidationResponse validationResponse) {
        PreConditions.mustBeEqual(request.getSecretKey(), properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY), ValidationError.INVALID_SECRET_KEY, validationResponse);
        PreConditions.notEmpty(request.getChecksum(), ValidationError.INVALID_CHECKSUM, validationResponse);
        validationResponse = updateValid(validationResponse);
        if(validationResponse.isValid()){
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(request.getSecretKey()).append(request.getUserSubscriptionId()).append(request.getVariantId()).append(request.getOrderId()).append(request.isRefund());
                if(request.getRefundAmount()!=null && request.getRefundAmount()>0){
                    sb.append(request.getRefundAmount());
                }
                String checksum = checksumService.calculateChecksumHmacSHA256(properties.getProperty(GlobalConstants.PAYMENTS_ENCRYPTION_KEY), sb.toString());
                PreConditions.mustBeEqual(checksum, request.getChecksum(), ValidationError.INVALID_ENCRYPTION, validationResponse);
            } catch (Exception e) {
                validationResponse.addValidationError(ValidationError.INVALID_ENCRYPTION);
            }
        }
        return updateValid(validationResponse);
    }

    private ValidationResponse validateEncryptionForTurnOffAutoDebit(TurnOffAutoDebitRequest request, ValidationResponse validationResponse) {
        PreConditions.mustBeEqual(request.getSecretKey(), properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY), ValidationError.INVALID_SECRET_KEY, validationResponse);
        PreConditions.notEmpty(request.getChecksum(), ValidationError.INVALID_CHECKSUM, validationResponse);
        validationResponse = updateValid(validationResponse);
        if(validationResponse.isValid()){
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(request.getSecretKey()).append(request.getUser().getMobile());
                String checksum = checksumService.calculateChecksumHmacSHA256(properties.getProperty(GlobalConstants.PAYMENTS_ENCRYPTION_KEY), sb.toString());
                PreConditions.mustBeEqual(checksum, request.getChecksum(), ValidationError.INVALID_ENCRYPTION, validationResponse);
            } catch (Exception e) {
                validationResponse.addValidationError(ValidationError.INVALID_ENCRYPTION);
            }
        }
        return updateValid(validationResponse);
    }

    private ValidationResponse validateEncryptionForBlockUnblockUser(BlockUnblockRequest request, ValidationResponse validationResponse) {
        PreConditions.mustBeEqual(request.getSecretKey(), properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY), ValidationError.INVALID_SECRET_KEY, validationResponse);
        PreConditions.notEmpty(request.getChecksum(), ValidationError.INVALID_CHECKSUM, validationResponse);
        validationResponse = updateValid(validationResponse);
        if(validationResponse.isValid()){
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(request.getSecretKey()).append(request.getUser().getMobile()).append(request.isBlockUser());
                String checksum = checksumService.calculateChecksumHmacSHA256(properties.getProperty(GlobalConstants.PAYMENTS_ENCRYPTION_KEY), sb.toString());
                PreConditions.mustBeEqual(checksum, request.getChecksum(), ValidationError.INVALID_ENCRYPTION, validationResponse);
            } catch (Exception e) {
                validationResponse.addValidationError(ValidationError.INVALID_ENCRYPTION);
            }
        }
        return updateValid(validationResponse);
    }

    private ValidationResponse validateEncryptionForInitPurchaseSubscription(InitPurchaseRequest request, ValidationResponse validationResponse) {
        PreConditions.mustBeEqual(request.getSecretKey(), properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY), ValidationError.INVALID_SECRET_KEY, validationResponse);
        PreConditions.notEmpty(request.getChecksum(), ValidationError.INVALID_CHECKSUM, validationResponse);
        PreConditions.notNull(request.getUser(), ValidationError.INVALID_USER, validationResponse);
        if(request.getUser()!=null){
            PreConditions.notEmpty(request.getUser().getMobile(), ValidationError.INVALID_MOBILE, validationResponse);
        }
        validationResponse = updateValid(validationResponse);
        if(validationResponse.isValid()){
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(request.getSecretKey()).append(request.getUser().getMobile()).append(request.getPlanId()).append(request.getVariantId()).append(request.getPrice())
                        .append(request.getDurationDays()).append(request.getPlanType()).append(request.getBusiness()).append(request.getChannel()).append(request.getPlatform());
                String checksum = checksumService.calculateChecksumHmacSHA256(properties.getProperty(GlobalConstants.PAYMENTS_ENCRYPTION_KEY), sb.toString());
                PreConditions.mustBeEqual(checksum, request.getChecksum(), ValidationError.INVALID_ENCRYPTION, validationResponse);
            } catch (Exception e) {
                validationResponse.addValidationError(ValidationError.INVALID_ENCRYPTION);
            }
        }
        return updateValid(validationResponse);
    }

    private ValidationResponse updateValid(ValidationResponse validationResponse){
        if(CollectionUtils.isEmpty(validationResponse.getValidationErrorSet())){
            validationResponse.setValid(true);
        }else{
            validationResponse.setValid(false);
        }
        return validationResponse;
    }

    public void test() {
        ValidationResponse validationResponse = new ValidationResponse();
        validateSSOLogin("9mh2ty8bdn6kdcxsi90urc2nz", "9464cb4968b745758082ed4c019c41b5", "9880252944", validationResponse);
        System.out.println(validationResponse);
    }

    private void validateSSOLogin(String ssoId, String ticketId, String mobile, ValidationResponse validationResponse) {
        int retryCount = GlobalConstants.API_RETRY_COUNT;
        LOG.info("In validateLogin with uid "+ssoId+", ticketId "+ticketId);
        RETRY_LOOP:
        while(retryCount>0) {
            try {
                Gson gson = GlobalConstants.gson;
                String ssoResponse = getSSOIdForTicketId(ticketId);
                LOG.info("got sso response for [validateLogin] with ssoId: " + ssoId + ", ticketId: " + ticketId + ", response: " + ssoResponse);
                SSOValidateResponse ssoValidateResponse = null;
                try {
                     ssoValidateResponse = gson.fromJson(ssoResponse, SSOValidateResponse.class);
                }catch (Exception e){
                    LOG.info("User Validation Failed From SSO Response [validateLogin] with ssoId: " + ssoId + ", ticketId: " + ticketId + ", response : " + ssoResponse);
                    validationResponse.addValidationError(ValidationError.INVALID_SSO_CREDENTIALS);
                    return;
                }
                if (ssoId.equals(ssoValidateResponse.getUserId()) && mobile.equals(ssoValidateResponse.getVerifiedMobile())) {
                    LOG.info("User Validated From SSO Response [validateLogin] with ssoId: " + ssoId + ", ticketId: " + ticketId + ", response:" + ssoResponse);
                } else {
                    LOG.info("User Validation Failed From SSO Response [validateLogin] with ssoId: " + ssoId + ", ticketId: " + ticketId + ", response: " + ssoResponse);
                    validationResponse.addValidationError(ValidationError.INVALID_SSO_CREDENTIALS);
                }
            } catch (Exception e) {
                retryCount--;
                if(retryCount>0){
                    retryCount--;
                    continue RETRY_LOOP;
                }
                LOG.error("User Validation Failed From SSO Response [validateLogin] with ssoId: " + ssoId + ", ticketId: " + ticketId
                        + ", exception: ", e);
                validationResponse.addValidationError(ValidationError.INVALID_SSO_CREDENTIALS);
            }
            updateValid(validationResponse);
            return;
        }
    }

    private String getSSOIdForTicketId(String ticketId) throws Exception {
        LOG.info("IN [getSSOIdForTicketId] with ticketId : "+ticketId);
        String ssoValidationUrl = properties.getProperty(GlobalConstants.SSO_VALIDATE_API_URL_KEY);
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNoneBlank(ticketId)) {
            params.put(GlobalConstants.TICKETID, ticketId);
        }
        params.put(GlobalConstants.SITEID, properties.getProperty(GlobalConstants.TP_SITE_ID_KEY));
        params.put(GlobalConstants.TYPE, GlobalConstants.SSO_VALIDATION_API_TYPE);
        params.put(GlobalConstants.CHANNEL, properties.getProperty(GlobalConstants.TP_CHANNEL_KEY));
        return restTemplateUtil.getRestTemplate().getForObject(ssoValidationUrl + restTemplateUtil.prepareParams(params), String.class);
    }
}
