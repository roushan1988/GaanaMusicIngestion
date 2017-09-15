package com.til.prime.timesSubscription.service.impl;

import com.google.gson.Gson;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.enums.*;
import com.til.prime.timesSubscription.model.SubscriptionVariantModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class SubscriptionValidationServiceImpl implements SubscriptionValidationService {

    private static final Logger LOG = Logger.getLogger(SubscriptionValidationServiceImpl.class);
    @Autowired
    private RestTemplateUtil restTemplateUtil;
    @Resource(name = "config_properties")
    private Properties properties;

    @Override
    public ValidationResponse validateAllPlans(PlanListRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        PreConditions.notNullEnumCheck(request.getBusiness(), BusinessEnum.names(), ValidationError.INVALID_BUSINESS, validationResponse);
        PreConditions.notNullEnumCheck(request.getCountry(), CountryEnum.names(), ValidationError.INVALID_COUNTRY, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreInitPurchasePlan(InitPurchaseRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse = validateUser(request, validationResponse);
        PreConditions.notNull(request.getPlanId(), ValidationError.INVALID_PLAN_ID, validationResponse);
        PreConditions.notNull(request.getVariantId(), ValidationError.INVALID_VARIANT_ID, validationResponse);
        PreConditions.notNull(request.getPrice(), ValidationError.INVALID_PRICE, validationResponse);
        PreConditions.notNull(request.getDurationDays(), ValidationError.INVALID_DURATION_DAYS, validationResponse);
        PreConditions.notNullEnumCheck(request.getPlanType(), PlanTypeEnum.names(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
        PreConditions.notNullEnumCheck(request.getBusiness(), BusinessEnum.names(), ValidationError.INVALID_BUSINESS, validationResponse);
        PreConditions.notNullEnumCheck(request.getChannel(), ChannelEnum.names(), ValidationError.INVALID_CHANNEL, validationResponse);
        PreConditions.notNullEnumCheck(request.getPlatform(), PlatformEnum.names(), ValidationError.INVALID_PLATFORM, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostInitPurchasePlan(InitPurchaseRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel restrictedModel, UserSubscriptionModel lastUserSubscription, ValidationResponse validationResponse) {
        PreConditions.notNull(variantModel, ValidationError.INVALID_SUBSCRIPTION_VARIANT, validationResponse);
        PreConditions.mustBeNull(restrictedModel, ValidationError.USER_PLAN_DOES_NOT_QUALIFY, validationResponse);
        if(variantModel!=null) {
            PreConditions.mustBeEqual(request.getPlanType(), variantModel.getPlanType().name(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
        }
        if(lastUserSubscription!=null){
            PreConditions.notGreater(lastUserSubscription.getSubscriptionVariant().getPlanType().getOrder(), PlanTypeEnum.valueOf(request.getPlanType()).getOrder(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
            PreConditions.notAfter(lastUserSubscription.getEndDate(), TimeUtils.addDaysInDate(new Date(), GlobalConstants.MAX_DAYS_DIFF_FOR_NEW_SUBSCRIPTION_PURCHASE), ValidationError.USER_PLAN_DOES_NOT_QUALIFY, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreGenerateOrder(GenerateOrderRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse = validateCredentials(request, validationResponse);
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
            if (request.isRetryOnFailure()) {
                PreConditions.notNull(userSubscriptionModel.getOrderId(), ValidationError.ORDER_NOT_GENERATED, validationResponse);
            } else {
                PreConditions.mustBeNull(userSubscriptionModel.getOrderId(), ValidationError.ORDER_ALREADY_GENERATED, validationResponse);
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
        validationResponse = validateCredentials(request, validationResponse);
        PreConditions.mustBeEqual(request.getSecretKey(), GlobalConstants.PAYMENTS_SECRET_KEY, ValidationError.INVALID_SECRET_KEY, validationResponse);
        PreConditions.notNull(request.getUserSubscriptionId(), ValidationError.INVALID_USER_SUBSCRIPTION_ID, validationResponse);
        PreConditions.notEmpty(request.getOrderId(), ValidationError.INVALID_ORDER_ID, validationResponse);
        PreConditions.notNull(request.getVariantId(), ValidationError.INVALID_VARIANT_ID, validationResponse);
        PreConditions.notEmpty(request.getPaymentMethod(), ValidationError.INVALID_PAYMENT_DETAILS, validationResponse);
        PreConditions.notEmpty(request.getPaymentReference(), ValidationError.INVALID_PAYMENT_DETAILS, validationResponse);
        PreConditions.notNull(request.getPrice(), ValidationError.INVALID_PRICE, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostSubmitPurchasePlan(SubmitPurchaseRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        PreConditions.notNull(userSubscriptionModel, ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
        if(userSubscriptionModel!=null){
            PreConditions.mustBeFalse(userSubscriptionModel.isOrderCompleted(), ValidationError.INVALID_USER_SUBSCRIPTION_DETAILS, validationResponse);
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
    public ValidationResponse validatePreCancelSubscription(CancelSubscriptionRequest request) {
        ValidationResponse validationResponse = new ValidationResponse();
        PreConditions.mustBeEqual(request.getSecretKey(), GlobalConstants.PAYMENTS_SECRET_KEY, ValidationError.INVALID_SECRET_KEY, validationResponse);
        PreConditions.notNull(request.getUserSubscriptionId(), ValidationError.INVALID_USER_SUBSCRIPTION_ID, validationResponse);
        PreConditions.notEmpty(request.getOrderId(), ValidationError.INVALID_ORDER_ID, validationResponse);
        PreConditions.notNull(request.getVariantId(), ValidationError.INVALID_SUBSCRIPTION_VARIANT, validationResponse);
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePostCancelSubscription(CancelSubscriptionRequest request, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        PreConditions.notNull(userSubscriptionModel, ValidationError.INVALID_USER_SUBSCRIPTION_ID, validationResponse);
        if(userSubscriptionModel!=null){
            PreConditions.notAfter(new Date(), userSubscriptionModel.getEndDate(), ValidationError.ALREADY_EXPIRED, validationResponse);
        }
        return updateValid(validationResponse);
    }

    @Override
    public ValidationResponse validatePreExtendExpiry(ExtendExpiryRequest request) {
        ValidationResponse validationResponse = validatePreCancelSubscription(request);
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
            PreConditions.notGreater(lastModel.getSubscriptionVariant().getPlanType().getOrder(), PlanTypeEnum.valueOf(request.getPlanType()).getOrder(), ValidationError.INVALID_PLAN_TYPE, validationResponse);
            PreConditions.notAfter(lastModel.getEndDate(), TimeUtils.addDaysInDate(new Date(), GlobalConstants.MAX_DAYS_DIFF_FOR_NEW_SUBSCRIPTION_PURCHASE), ValidationError.USER_PLAN_DOES_NOT_QUALIFY, validationResponse);
        }
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
            if(StringUtils.isNotEmpty(request.getUser().getSsoId()) && StringUtils.isNotEmpty(request.getUser().getTicketId())){
                validateSSOLogin(request.getUser().getSsoId(), request.getUser().getTicketId(), validationResponse);
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
        validateSSOLogin("gauidPqjeMGkikuHy6j4x0tnL87bV4WASTWy8_vYtyrNLvgI", "0ac3fc29d04e48679387fc68151592f6", validationResponse);
        System.out.println(validationResponse);
    }

    private void validateSSOLogin(String ssoId, String ticketId, ValidationResponse validationResponse) {
        int retryCount = GlobalConstants.API_RETRY_COUNT;
        LOG.info("In validateLogin with uid "+ssoId+", ticketId "+ticketId);
        RETRY_LOOP:
        while(retryCount>0) {
            try {
                Gson gson = new Gson();
                String ssoResponse = getSSOIdForTicketId(ticketId);
                LOG.info("got sso response for [validateLogin] with ssoId" + ssoId + ", ticketId " + ticketId + ", response " + ssoResponse);
                SSOValidateResponse ssoValidateResponse = gson.fromJson(ssoResponse, SSOValidateResponse.class);
                if (ssoValidateResponse.getGassoid().equals(ssoId)) {
                    LOG.info("User Validated From SSO Response [validateLogin] with ssoId " + ssoId + ", ticketId " + ticketId + ", response :" + ssoResponse);
                } else {
                    LOG.info("User Validation Failed From SSO Response [validateLogin] with ssoId " + ssoId + ", ticketId " + ticketId + ", response : " + ssoResponse);
                    validationResponse.getValidationErrorSet().add(ValidationError.INVALID_SSO_CREDENTIALS);
                }
            } catch (Exception e) {
                retryCount--;
                if(retryCount>0){
                    retryCount--;
                    continue RETRY_LOOP;
                }
                LOG.error("User Validation Failed From SSO Response [validateLogin] with ssoId " + ssoId + ", ticketId " + ticketId
                        + ", exception: ", e);
                validationResponse.getValidationErrorSet().add(ValidationError.INVALID_SSO_CREDENTIALS);
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
