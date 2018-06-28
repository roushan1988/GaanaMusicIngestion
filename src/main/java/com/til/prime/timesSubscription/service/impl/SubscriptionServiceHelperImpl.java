package com.til.prime.timesSubscription.service.impl;

import com.google.common.collect.Maps;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.convertor.ModelToDTOConvertorUtil;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.dto.internal.OtpStatus;
import com.til.prime.timesSubscription.dto.internal.RefundInternalResponse;
import com.til.prime.timesSubscription.dto.internal.UrlShorteningResponse;
import com.til.prime.timesSubscription.enums.*;
import com.til.prime.timesSubscription.model.*;
import com.til.prime.timesSubscription.service.ChecksumService;
import com.til.prime.timesSubscription.service.SubscriptionServiceHelper;
import com.til.prime.timesSubscription.service.UserStatusPublisherService;
import com.til.prime.timesSubscription.util.HttpConnectionUtils;
import com.til.prime.timesSubscription.util.ResponseUtil;
import com.til.prime.timesSubscription.util.TimeUtils;
import com.til.prime.timesSubscription.util.UniqueIdGeneratorUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

@Service
public class SubscriptionServiceHelperImpl implements SubscriptionServiceHelper {
    private static final Logger LOG = Logger.getLogger(SubscriptionServiceHelperImpl.class);
    @Autowired
    private HttpConnectionUtils httpConnectionUtils;
    @Resource(name = "config_properties")
    private Properties properties;
    @Autowired
    private ChecksumService checksumService;
    @Autowired
    private UserStatusPublisherService publisherService;

    @Override
    public UserSubscriptionModel generateInitPurchaseUserSubscription(InitPurchaseRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel lastUserSubscription, UserModel userModel, BigDecimal price, boolean crmRequest, boolean free){
        return generateInitPurchaseUserSubscription(request.getUser(), ChannelEnum.valueOf(request.getChannel()), PlatformEnum.valueOf(request.getPlatform()), PlanTypeEnum.valueOf(request.getPlanType()), request.getDurationDays(), variantModel, lastUserSubscription, userModel, price, crmRequest, free);
    }

    @Override
    public UserSubscriptionModel generateInitPurchaseUserSubscription(UserDTO userDTO, ChannelEnum channel, PlatformEnum platform, PlanTypeEnum planType, Long durationDays, SubscriptionVariantModel variantModel, UserSubscriptionModel lastUserSubscription, UserModel userModel, BigDecimal price, boolean crmRequest, boolean free){
        Date date = new Date();
        UserSubscriptionModel userSubscriptionModel = new UserSubscriptionModel();
        userSubscriptionModel.setUser(userModel);
        userSubscriptionModel.setTicketId(userDTO.getTicketId());
        userSubscriptionModel.setSubscriptionVariant(variantModel);
        userSubscriptionModel.setStartDate((lastUserSubscription==null || lastUserSubscription.getEndDate().before(date))? date: TimeUtils.addMillisInDate(lastUserSubscription.getEndDate(), 1));
        userSubscriptionModel.setEndDate(TimeUtils.addDaysInDate(userSubscriptionModel.getStartDate(), durationDays.intValue()));
        userSubscriptionModel.setPlanStatus(PlanStatusEnum.INIT);
        userSubscriptionModel.setBusiness(variantModel.getSubscriptionPlan().getBusiness());
        userSubscriptionModel.setChannel(channel);
        userSubscriptionModel.setPlatform(platform);
        userSubscriptionModel.setCreated(new Date());
        userSubscriptionModel.setStatus(StatusEnum.getStatusForUserSubscription(userSubscriptionModel, date));
        userSubscriptionModel.setStatusDate(new Date());
        if(price.compareTo(BigDecimal.ZERO)<=0 || (crmRequest&&free)){
            String orderId = UniqueIdGeneratorUtil.generateOrderId();
            userSubscriptionModel.setOrderId(orderId);
            userSubscriptionModel.setPaymentMethod(GlobalConstants.PAYMENT_METHOD_NA);
            userSubscriptionModel.setPaymentReference(GlobalConstants.PAYMENT_REFERENCE_NA);
            userSubscriptionModel.setOrderCompleted(true);
            userSubscriptionModel.setPlanStatus(PlanStatusEnum.getPlanStatus(userSubscriptionModel.getStatus(), planType, variantModel.getPrice(), lastUserSubscription,  false));
            userSubscriptionModel.setTransactionStatus(TransactionStatusEnum.SUBSCRIPTION_TRANS_SUCCESS);
        }else{
            userSubscriptionModel.setTransactionStatus(TransactionStatusEnum.SUBSCRIPTION_TRANS_INITIATED);
        }
        return userSubscriptionModel;
    }

    @Override
    public UserSubscriptionModel updateSubmitPurchaseUserSubscription(SubmitPurchaseRequest request, UserSubscriptionModel userSubscriptionModel, UserSubscriptionModel lastUserSubscription) {
        SubscriptionVariantModel variantModel = userSubscriptionModel.getSubscriptionVariant();
        userSubscriptionModel.setPlanStatus(PlanStatusEnum.getPlanStatus(userSubscriptionModel.getStatus(), variantModel.getPlanType(), variantModel.getPrice(), lastUserSubscription,  request.isAutoRenewalJob()));
        userSubscriptionModel.setPaymentReference(request.getPaymentReference());
        userSubscriptionModel.setTransactionStatus(request.isPaymentSuccess()? TransactionStatusEnum.SUBSCRIPTION_TRANS_SUCCESS: TransactionStatusEnum.SUBSCRIPTION_TRANS_FAILED);
        userSubscriptionModel.setOrderCompleted(request.isPaymentSuccess());
        userSubscriptionModel.setAutoRenewal(request.isAutoRenewal());
        return userSubscriptionModel;
    }

    @Override
    public UserSubscriptionModel updateGenerateOrderUserSubscription(GenerateOrderRequest request, UserSubscriptionModel userSubscriptionModel) {
        String orderId = UniqueIdGeneratorUtil.generateOrderId();
        userSubscriptionModel.setOrderId(orderId);
        userSubscriptionModel.setPaymentMethod(request.getPaymentMethod());
        return userSubscriptionModel;
    }

    @Override
    public UserSubscriptionModel updateSSOStatus(UserSubscriptionModel userSubscriptionModel){
        boolean success = communicateSSO(userSubscriptionModel);
        userSubscriptionModel.setSsoCommunicated(success);
        return userSubscriptionModel;
    }

    @Override
    public UserSubscriptionModel publishUserStatus(UserSubscriptionModel userSubscriptionModel) {
        boolean success = publisherService.publishUserStatus(getPublishedUserStatusDTO(userSubscriptionModel));
        userSubscriptionModel.setStatusPublished(success);
        return userSubscriptionModel;
    }

    private PublishedUserStatusDTO getPublishedUserStatusDTO(UserSubscriptionModel userSubscriptionModel){
        PublishedUserStatusDTO statusDTO = new PublishedUserStatusDTO();
        statusDTO.setMobile(userSubscriptionModel.getUser().getMobile());
        statusDTO.setSsoId(userSubscriptionModel.getUser().getSsoId());
        statusDTO.setPlanStatus(userSubscriptionModel.getPlanStatus().getCode());
        statusDTO.setExpiry(userSubscriptionModel.getEndDate());
        statusDTO.setEmail(userSubscriptionModel.getUser().getEmail());
        statusDTO.setAutoRenewal(userSubscriptionModel.isAutoRenewal());
        statusDTO.setTimesPrimeUser(PlanStatusEnum.validTimesPrimeUser(statusDTO.getPlanStatus()));
        return statusDTO;
    }

    @Override
    public PlanListResponse preparePlanListResponse(PlanListResponse response, List<SubscriptionPlanDTO> subscriptionPlans, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response.setPlanDTOList(Arrays.asList(subscriptionPlans.get(0)));
            response = (PlanListResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (PlanListResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public InitPurchaseResponse prepareInitPurchaseResponse(InitPurchaseResponse response, UserSubscriptionModel userSubscriptionModel, UserSubscriptionModel lastUserSubscription, ValidationResponse validationResponse) {
        if(lastUserSubscription!=null){
            response.setDaysLeft(TimeUtils.getDifferenceInDays(new Date(), lastUserSubscription.getEndDate()).intValue());
        }
        response.setType(WebViewTypeEnum.TOAST.getName());
        if(validationResponse.isValid()){
            SubscriptionVariantModel variantModel = userSubscriptionModel.getSubscriptionVariant();
            response.setUserSubscriptionId(userSubscriptionModel.getId());
            response.setOrderId(userSubscriptionModel.getOrderId());
            response.setPlanId(variantModel.getSubscriptionPlan().getId());
            response.setVariantId(variantModel.getId());
            response.setPaymentRequired(!userSubscriptionModel.isOrderCompleted());
            response.setPaymentAmount(userSubscriptionModel.getSubscriptionVariant().getPrice());
            response.setPrimeId(userSubscriptionModel.getUser().getPrimeId());
            response = (InitPurchaseResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (InitPurchaseResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public GenerateOrderResponse prepareGenerateOrderResponse(GenerateOrderResponse response, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        if(userSubscriptionModel!=null){
            SubscriptionVariantModel variantModel = userSubscriptionModel.getSubscriptionVariant();
            response.setUserSubscriptionId(userSubscriptionModel.getId());
            response.setOrderId(userSubscriptionModel.getOrderId());
            response.setPlanId(variantModel.getSubscriptionPlan().getId());
            response.setVariantId(variantModel.getId());
            response.setStartDate(userSubscriptionModel.getStartDate());
            response.setEndDate(userSubscriptionModel.getEndDate());
        }
        if(validationResponse.isValid()){
            response = (GenerateOrderResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (GenerateOrderResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public SubmitPurchaseResponse prepareSubmitPurchaseResponse(SubmitPurchaseResponse response, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            SubscriptionVariantModel variantModel = userSubscriptionModel.getSubscriptionVariant();
            response.setVariantId(variantModel.getId());
            response.setOrderId(userSubscriptionModel.getOrderId());
            response.setPlanId(variantModel.getSubscriptionPlan().getId());
            response.setPlanStatus(userSubscriptionModel.getPlanStatus().name());
            response.setTransactionStatus(userSubscriptionModel.getTransactionStatus().name());
            response.setOrderCompleted(userSubscriptionModel.isOrderCompleted());
            response.setAutoRenewal(userSubscriptionModel.isAutoRenewal());
            response = (SubmitPurchaseResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (SubmitPurchaseResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public List<UserSubscriptionDTO> generateUserSubscriptionDTOList(List<UserSubscriptionModel> userSubscriptionModelList) {
        List<UserSubscriptionDTO> userSubscriptionDTOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(userSubscriptionModelList)) {
            for (UserSubscriptionModel model : userSubscriptionModelList) {
                UserSubscriptionDTO dto = ModelToDTOConvertorUtil.getUserSubscriptionDTO(model);
                userSubscriptionDTOList.add(dto);
            }
        }
        return userSubscriptionDTOList;
    }

    @Override
    public PurchaseHistoryResponse preparePurchaseHistoryResponse(PurchaseHistoryResponse response, List<UserSubscriptionDTO> userSubscriptionDTOList, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response.setUserSubscriptionDTOList(userSubscriptionDTOList);
            response = (PurchaseHistoryResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (PurchaseHistoryResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public CancelSubscriptionResponse prepareCancelSubscriptionResponse(CancelSubscriptionResponse response, BigDecimal refundAmount, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response.setRefundAmount(refundAmount);
            response = (CancelSubscriptionResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (CancelSubscriptionResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public GenericResponse prepareTurnOffAutoDebitResponse(GenericResponse response, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response = ResponseUtil.createSuccessResponse(response);
        }else{
            response = ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public GenericResponse prepareBlockUnblockResponse(GenericResponse response, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response = ResponseUtil.createSuccessResponse(response);
        }else{
            response = ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public BackendSubscriptionResponse prepareBackendSubscriptionResponse(BackendSubscriptionResponse response, Set<ValidationError> validationErrors, List<BackendActivationUserDTO> successList, List<BackendActivationUserDTO> failureList) {
        response.setSuccessList(successList);
        response.setFailureList(failureList);
        if(CollectionUtils.isEmpty(validationErrors) && CollectionUtils.isNotEmpty(response.getSuccessList())){
            response = (BackendSubscriptionResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response.getValidationErrors().addAll(validationErrors);
            response = ResponseUtil.createFailureResponse(response);
        }
        return response;
    }

    @Override
    public BackendSubscriptionActivationResponse prepareBackendSubscriptionActivationResponse(BackendSubscriptionActivationResponse response, UserSubscriptionModel userSubscription, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            SubscriptionVariantModel variantModel = userSubscription.getSubscriptionVariant();
            response.setUserSubscriptionId(userSubscription.getId());
            response.setOrderId(userSubscription.getOrderId());
            response.setPlanId(variantModel.getSubscriptionPlan().getId());
            response.setVariantId(variantModel.getId());
            response.setStartDate(userSubscription.getStartDate());
            response.setEndDate(userSubscription.getEndDate());
            response.setVariantId(variantModel.getId());
            response = (BackendSubscriptionActivationResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (BackendSubscriptionActivationResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public UserSubscriptionAuditModel getUserSubscriptionAuditModel(UserSubscriptionModel userSubscriptionModel, EventEnum event) {
        UserSubscriptionAuditModel auditModel = new UserSubscriptionAuditModel();
        auditModel.setUserSubscriptionId(userSubscriptionModel.getId());
        auditModel.setCreated(new Date());
        auditModel.setSubscriptionVariantId(userSubscriptionModel.getSubscriptionVariant().getId());
        auditModel.setSubscriptionPlanId(userSubscriptionModel.getSubscriptionVariant().getSubscriptionPlan().getId());
        auditModel.setEvent(event);
        auditModel.setUserId(userSubscriptionModel.getUser().getId());
        auditModel.setSsoId(userSubscriptionModel.getUser().getSsoId());
        auditModel.setTicketId(userSubscriptionModel.getTicketId());
        auditModel.setOrderId(userSubscriptionModel.getOrderId());
        auditModel.setPaymentMethod(userSubscriptionModel.getPaymentMethod());
        auditModel.setPaymentReference(userSubscriptionModel.getPaymentReference());
        auditModel.setOrderCompleted(userSubscriptionModel.isOrderCompleted());
        auditModel.setSsoCommunicated(userSubscriptionModel.isSsoCommunicated());
        auditModel.setStatusPublished(userSubscriptionModel.isStatusPublished());
        auditModel.setStartDate(userSubscriptionModel.getStartDate());
        auditModel.setEndDate(userSubscriptionModel.getEndDate());
        auditModel.setPlanStatus(userSubscriptionModel.getPlanStatus());
        auditModel.setStatus(userSubscriptionModel.getStatus());
        auditModel.setStatusDate(userSubscriptionModel.getStatusDate());
        auditModel.setTransactionStatus(userSubscriptionModel.getTransactionStatus());
        auditModel.setBusiness(userSubscriptionModel.getBusiness());
        auditModel.setChannel(userSubscriptionModel.getChannel());
        auditModel.setPlatform(userSubscriptionModel.getPlatform());
        auditModel.setAutoRenewal(userSubscriptionModel.isAutoRenewal());
        auditModel.setRefundedAmount(userSubscriptionModel.getRefundedAmount());
        return auditModel;
    }

    @Override
    public BigDecimal calculateRefundAmount(UserSubscriptionModel userSubscriptionModel) {
        BigDecimal price = userSubscriptionModel.getSubscriptionVariant().getPrice();
        if(price.compareTo(BigDecimal.ZERO)==0){
            return BigDecimal.ZERO;
        }
        Long time = new Date().getTime();
        Long startTime = userSubscriptionModel.getStartDate().getTime();
        Long endTime = userSubscriptionModel.getEndDate().getTime();
        if(time<startTime){
            return price;
        }
        return price.multiply(new BigDecimal((time-startTime)/(endTime-startTime)));
    }

    @Override
    public BigDecimal calculateRefundAmount(List<UserSubscriptionModel> userSubscriptionModelList) {
        BigDecimal price = BigDecimal.ZERO;
        for(UserSubscriptionModel userSubscriptionModel: userSubscriptionModelList){
            price.add(calculateRefundAmount(userSubscriptionModel));
        }
        return price;
    }

    @Override
    public UserSubscriptionModel extendSubscription(UserSubscriptionModel userSubscriptionModel, Long extensionDays) {
        Date newEndDate = TimeUtils.addDaysInDate(userSubscriptionModel.getEndDate(), extensionDays.intValue());
        userSubscriptionModel.setEndDate(newEndDate);
        userSubscriptionModel.setStatus(StatusEnum.getStatusForUserSubscription(userSubscriptionModel, null));
        userSubscriptionModel.setStatusDate(new Date());
        userSubscriptionModel.setPlanStatus(PlanStatusEnum.getPlanStatus(userSubscriptionModel.getStatus(), userSubscriptionModel.getSubscriptionVariant().getPlanType(), userSubscriptionModel.getSubscriptionVariant().getPrice(), null,  false));
        return userSubscriptionModel;
    }

    @Override
    public ExtendExpiryResponse prepareExtendExpiryResponse(ExtendExpiryResponse response, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            SubscriptionVariantModel variantModel = userSubscriptionModel.getSubscriptionVariant();
            response.setUserSubscriptionId(userSubscriptionModel.getId());
            response.setPlanId(variantModel.getSubscriptionPlan().getId());
            response.setVariantId(variantModel.getId());
            response.setOrderId(userSubscriptionModel.getOrderId());
            response.setStartDate(userSubscriptionModel.getStartDate());
            response.setEndDate(userSubscriptionModel.getEndDate());
            response = (ExtendExpiryResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (ExtendExpiryResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public GenericValidationResponse prepareCheckEligibilityResponse(GenericValidationResponse response, ValidationResponse validationResponse, boolean validExecution) {
        if(validationResponse.isValid()){
            response.setValid(true);
        }
        if(validExecution){
            response = (GenericValidationResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (GenericValidationResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public GenericValidationResponse prepareValidVariantResponse(GenericValidationResponse response, ValidationResponse validationResponse, boolean validExecution) {
        if(validationResponse.isValid()){
            response.setValid(true);
        }
        if(validExecution){
            response = (GenericValidationResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (GenericValidationResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public CheckStatusResponse prepareCheckStatusResponse(CheckStatusResponse response, boolean external, SubscriptionStatusDTO subscriptionStatusDTO, ValidationResponse validationResponse) {
        response.setTimesPrimeUser(subscriptionStatusDTO!=null && PlanStatusEnum.validTimesPrimeUser(subscriptionStatusDTO.getPlanStatus()));
        response.setSubscriptionStatusDTO(subscriptionStatusDTO);
        if(external && subscriptionStatusDTO!=null){
            subscriptionStatusDTO.setPrimeId(null);
        }
        if(validationResponse.isValid()){
            response = (CheckStatusResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (CheckStatusResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public UserModel getUser(GenericRequest request) {
        UserModel userModel = new UserModel();
        userModel.setSsoId(request.getUser().getSsoId());
        userModel.setFirstName(request.getUser().getFirstName());
        userModel.setLastName(request.getUser().getLastName());
        userModel.setMobile(request.getUser().getMobile());
        userModel.setEmail(request.getUser().getEmail());
        userModel.setCity(request.getUser().getCity());
        userModel.setPrimeId(UniqueIdGeneratorUtil.generatePrimeId());
        userModel.setCreated(new Date());
        return userModel;
    }

    @Override
    public UserAuditModel getUserAudit(UserModel userModel, EventEnum eventEnum) {
        UserAuditModel userAuditModel = new UserAuditModel();
        userAuditModel.setUserId(userModel.getId());
        userAuditModel.setSsoId(userModel.getSsoId());
        userAuditModel.setFirstName(userModel.getFirstName());
        userAuditModel.setLastName(userModel.getLastName());
        userAuditModel.setMobile(userModel.getMobile());
        userAuditModel.setPrimeId(userModel.getPrimeId());
        userAuditModel.setEmail(userModel.getEmail());
        userAuditModel.setCity(userModel.getCity());
        userAuditModel.setEvent(eventEnum);
        userAuditModel.setBlocked(userModel.isBlocked());
        userAuditModel.setCreated(new Date());
        return userAuditModel;
    }

    @Override
    public BackendSubscriptionUserModel getBackendSubscriptionUser(BackendSubscriptionUserModel user, BackendActivationUserDTO dto) {
        if(user==null){
            user = new BackendSubscriptionUserModel();
        }
        user.setMobile(dto.getMobile());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setCode(UniqueIdGeneratorUtil.generateCode(dto.getMobile(), GlobalConstants.BACKEND_ACTIVATION_CODE_LENGTH));
        String params = new StringBuilder("mobile=").append(dto.getMobile()).append("&code=").append(user.getCode()).toString();
        String encodedParams = new String(Base64.encodeBase64(params.getBytes(Charset.forName(GlobalConstants.UTF_8))));
        StringBuilder url = new StringBuilder(properties.getProperty(GlobalConstants.PRIME_BACKEND_ACTIVATION_URL_KEY)).append("?q=").append(encodedParams);
        String shortenedUrl = shortenUrl(url.toString());
        user.setShortenedUrl(shortenedUrl);
        user.setDurationDays(dto.getDurationDays());
        user.setCreated(new Date());
        return user;
    }

    @Override
    public BackendSubscriptionUserAuditModel getBackendSubscriptionUserAudit(BackendSubscriptionUserModel user, EventEnum event) {
        BackendSubscriptionUserAuditModel userAudit = new BackendSubscriptionUserAuditModel();
        userAudit.setBackendSubscriptionUserId(user.getId());
        userAudit.setMobile(user.getMobile());
        userAudit.setEmail(user.getEmail());
        userAudit.setFirstName(user.getFirstName());
        userAudit.setLastName(user.getLastName());
        userAudit.setEvent(event);
        userAudit.setCode(user.getCode());
        userAudit.setShortenedUrl(user.getShortenedUrl());
        userAudit.setDurationDays(user.getDurationDays());
        userAudit.setCompleted(user.isCompleted());
        userAudit.setCreated(new Date());
        return userAudit;
    }

    @Override
    public final boolean renewSubscription(UserSubscriptionModel userSubscriptionModel){
        try{
            RenewSubscriptionRequest renewSubscriptionRequest = new RenewSubscriptionRequest();
            renewSubscriptionRequest.setUserSubscriptionId(userSubscriptionModel.getId());
            renewSubscriptionRequest.setOrderId(userSubscriptionModel.getOrderId());
            renewSubscriptionRequest.setPlatform(PlatformEnum.JOB.name());
            renewSubscriptionRequest.setPrice(userSubscriptionModel.getSubscriptionVariant().getPrice().doubleValue());
            renewSubscriptionRequest.setJob(true);
            renewSubscriptionRequest.setPlanType(PlanTypeEnum.PAYMENT.name());
            renewSubscriptionRequest.setSecretKey(properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY));
            updateSubscriptionChecksumForRenewSubscription(renewSubscriptionRequest);
            PaymentsGenericResponse response = httpConnectionUtils.requestForObject(renewSubscriptionRequest, properties.getProperty(GlobalConstants.PAYMENTS_RENEW_SUBSCRIPTION_URL_KEY), PaymentsGenericResponse.class, GlobalConstants.POST);
            return inferPaymentsResponse(response);
        }catch (Exception e){
            LOG.error("Exception in api call to payments for renewal of subscription with userSubscriptionId: "+userSubscriptionModel.getId()+", orderId: "+userSubscriptionModel.getOrderId(), e);
        }
        return false;
    }

    @Override
    public final RefundInternalResponse refundPayment(String orderId, Double refundAmount, boolean forceAmount){
        if (Double.compare(refundAmount, 0d)==0) {
            return new RefundInternalResponse(true, refundAmount);
        }
        try{
            PaymentsRefundRequest refundRequest = new PaymentsRefundRequest();
            refundRequest.setAmount(refundAmount);
            refundRequest.setOrderId(orderId);
            refundRequest.setForceAmount(forceAmount);
            refundRequest.setSecretKey(properties.getProperty(GlobalConstants.PAYMENTS_SECRET_KEY));
            updateChecksumForRefund(refundRequest);
            PaymentsRefundResponse response = httpConnectionUtils.requestForObject(refundRequest, properties.getProperty(GlobalConstants.PAYMENTS_REFUND_URL_KEY), PaymentsRefundResponse.class, GlobalConstants.POST);
            return inferPaymentsRefundResponse(response);
        }catch (Exception e){
            LOG.error("Exception in api call to payments for refund with orderId: "+orderId+", refundAmount: "+refundAmount, e);
        }
        return new RefundInternalResponse(false, null);
    }

    @Override
    public String shortenUrl(String longUrl) {
        int retryCount = GlobalConstants.API_RETRY_COUNT;
        RETRY_LOOP:
        while(retryCount>0){
            try{
                String response = httpConnectionUtils.request("", properties.getProperty(GlobalConstants.URL_SHORTENING_API_KEY)+URLEncoder.encode(longUrl, GlobalConstants.UTF_8), GlobalConstants.CONTENT_TYPE_JSON, GlobalConstants.GET);
                UrlShorteningResponse shorteningResponse = GlobalConstants.gson.fromJson(response, UrlShorteningResponse.class);
                return shorteningResponse.getData().getUrl();
            }catch (Exception e){
                retryCount--;
                continue RETRY_LOOP;
            }
        }
        return null;
    }

    @Override
    public OtpVerificationResponse prepareOtpVerificationResponse(OtpVerificationResponse response, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response = (OtpVerificationResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            Set<ValidationError> inlineErrors = ValidationError.OTP_INLINE_CODES;
            inlineErrors.retainAll(validationResponse.getValidationErrorSet());
            if(CollectionUtils.isNotEmpty(inlineErrors)){
                response.setType(WebViewTypeEnum.INLINE.getName());
            }else{
                response.setType(WebViewTypeEnum.TOAST.getName());
            }
            response = (OtpVerificationResponse) ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public GenericResponse prepareGenericResponse(GenericResponse response, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response = ResponseUtil.createSuccessResponse(response);
        }else{
            response = ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }

    @Override
    public OtpStatus sendOtp(String mobile, boolean resend){
        int retryCount = GlobalConstants.API_RETRY_COUNT;
        String url = resend? properties.getProperty(GlobalConstants.SSO_RESEND_OTP_URL_KEY) : properties.getProperty(GlobalConstants.SSO_SEND_OTP_URL_KEY);
        RETRY_LOOP:
        while (retryCount>0){
            try{
                Map<String, String> headers = Maps.newHashMap();
                headers.put(GlobalConstants.CONTENT_TYPE, GlobalConstants.CONTENT_TYPE_JSON);
                headers.put(GlobalConstants.SSO_API_KEY, properties.getProperty(GlobalConstants.SSO_OTP_API_KEY_PROPERTY));
                Map<String, String> data = Maps.newHashMap();
                data.put(GlobalConstants.SSO_MOBILE_KEY, mobile);
                SSOGenericResponse response = httpConnectionUtils.requestWithHeaders(data, headers, url, SSOGenericResponse.class, GlobalConstants.POST);
                if(response.getCode()==200 && GlobalConstants.SUCCESS.equals(response.getStatus()) && GlobalConstants.OK.equals(response.getMessage())){
                    return new OtpStatus(true);
                }
                return new OtpStatus(false, response.getMessage());
            }catch (Exception e){
                retryCount--;
                if(retryCount>0){
                    continue RETRY_LOOP;
                }
            }
        }
        return new OtpStatus(false, ValidationError.OTP_SENDING_ERROR.getErrorMessage());
    }

    @Override
    public OtpStatus verifyOtp(String mobile, String otp){
        int retryCount = GlobalConstants.API_RETRY_COUNT;
        RETRY_LOOP:
        while (retryCount>0){
            try{
                Map<String, String> headers = Maps.newHashMap();
                headers.put(GlobalConstants.CONTENT_TYPE, GlobalConstants.CONTENT_TYPE_JSON);
                headers.put(GlobalConstants.SSO_API_KEY, properties.getProperty(GlobalConstants.SSO_OTP_API_KEY_PROPERTY));
                Map<String, String> data = Maps.newHashMap();
                data.put(GlobalConstants.SSO_MOBILE_KEY, mobile);
                data.put(GlobalConstants.SSO_OTP_KEY, otp);
                SSOGenericResponse response = httpConnectionUtils.requestWithHeaders(data, headers, properties.getProperty(GlobalConstants.SSO_VERIFY_OTP_URL_KEY), SSOGenericResponse.class, GlobalConstants.POST);
                if(response.getCode()==200 && GlobalConstants.SUCCESS.equals(response.getStatus()) && GlobalConstants.OK.equals(response.getMessage())){
                    return new OtpStatus(true);
                }
                return new OtpStatus(false, response.getMessage());
            }catch (Exception e){
                retryCount--;
                if(retryCount>0){
                    continue RETRY_LOOP;
                }
            }
        }
        return new OtpStatus(false, ValidationError.OTP_VERIFICATION_ERROR.getErrorMessage());
    }

    private void updateSubscriptionChecksumForRenewSubscription(RenewSubscriptionRequest request) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(request.getSecretKey()).append(GlobalConstants.PAYMENTS_REQ_DELIM).append(request.getUserSubscriptionId())
                    .append(GlobalConstants.PAYMENTS_REQ_DELIM).append(request.getOrderId()).append(GlobalConstants.PAYMENTS_REQ_DELIM)
                    .append(request.getPrice()).append(GlobalConstants.PAYMENTS_REQ_DELIM).append(request.getPlatform())
                    .append(GlobalConstants.PAYMENTS_REQ_DELIM).append(request.isJob());
            String checksum = checksumService.calculateChecksumHmacSHA256(properties.getProperty(GlobalConstants.PAYMENTS_ENCRYPTION_KEY), sb.toString());
            request.setChecksum(checksum);
        } catch (Exception e) {
            LOG.error("error while calculating checksum for subscription call for submit payments: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private void updateChecksumForRefund(PaymentsRefundRequest refundRequest){
        try{
            StringBuilder checksumString = new StringBuilder(refundRequest.getOrderId()).append(GlobalConstants.PAYMENTS_REQ_DELIM)
                    .append(refundRequest.getAmount()).append(GlobalConstants.PAYMENTS_REQ_DELIM).append(refundRequest.isForceAmount()).append(GlobalConstants.PAYMENTS_REQ_DELIM).append(refundRequest.getSecretKey());
            String checksum = checksumService.calculateChecksumHmacSHA256(properties.getProperty(GlobalConstants.PAYMENTS_ENCRYPTION_KEY), checksumString.toString());
            refundRequest.setRequestHash(checksum);
        }catch (Exception e){
            LOG.error("error while calculating checksum for subscription call for refund payments: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean inferPaymentsResponse(PaymentsGenericResponse response){
        if(response.getStatus().equals(GlobalConstants.SUCCESS) && response.getStatusCode()==GlobalConstants.PAYMENTS_SUCCESS_STATUS_CODE){
            return true;
        }else{
            return false;
        }
    }

    private RefundInternalResponse inferPaymentsRefundResponse(PaymentsRefundResponse response){
        if(response.getStatus().equals(GlobalConstants.SUCCESS) && response.getStatusCode()==GlobalConstants.PAYMENTS_SUCCESS_STATUS_CODE){
            return new RefundInternalResponse(true, response.getRefundedAmount());
        }
        return new RefundInternalResponse(false, null);
    }

    private final boolean communicateSSO(UserSubscriptionModel userSubscriptionModel){
        int retryCount = GlobalConstants.API_RETRY_COUNT;
        RETRY_LOOP:
        while (retryCount>0){
            try{
                Map<String, String> headers = Maps.newHashMap();
                headers.put(GlobalConstants.CONTENT_TYPE, GlobalConstants.CONTENT_TYPE_JSON);
                headers.put(GlobalConstants.CHANNEL, properties.getProperty(GlobalConstants.TP_CHANNEL_KEY));
                headers.put(GlobalConstants.SSOID, userSubscriptionModel.getUser().getSsoId());
                headers.put(GlobalConstants.STATUS, Integer.toString(userSubscriptionModel.getPlanStatus().getCode()));
                headers.put(GlobalConstants.PLATFORM, userSubscriptionModel.getPlatform().getSsoChannel());
                SSOGenericResponse response = httpConnectionUtils.requestWithHeaders(Maps.newHashMap(), headers, properties.getProperty(GlobalConstants.SSO_UPDATE_PROFILE_URL_KEY), SSOGenericResponse.class, GlobalConstants.POST);
                if(response.getCode()==200 && GlobalConstants.SUCCESS.equals(response.getStatus()) && GlobalConstants.OK.equals(response.getMessage())){
                    return true;
                }
                return false;
            }catch (Exception e){
                retryCount--;
                if(retryCount>0){
                    continue RETRY_LOOP;
                }
            }
        }
        return false;
    }

    @Override
    public OrderSearchCRMResponse prepareOrderSearchResponse(OrderSearchCRMResponse orderSearchCRMResponse, OrderSearchResultsCRM orderSearchResultsCRM, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
        	orderSearchCRMResponse.setOrderSearchResultsCRM(orderSearchResultsCRM);
        	orderSearchCRMResponse = (OrderSearchCRMResponse) ResponseUtil.createSuccessResponse(orderSearchCRMResponse);
        }else{
        	orderSearchCRMResponse = (OrderSearchCRMResponse) ResponseUtil.createFailureResponse(orderSearchCRMResponse, validationResponse, validationResponse.getMaxCategory());
        }
        return orderSearchCRMResponse;
    }
    
    @Override
    public PropertyDataGetResponseCRM preparePropertyDataGetResponse(PropertyDataGetResponseCRM propertyDataGetResponseCRM, Map<PropertyEnum, String> propertyMap, ValidationResponse validationResponse){
        if(validationResponse.isValid()){
        	propertyDataGetResponseCRM.setSubscription_property_data(propertyMap);
        	propertyDataGetResponseCRM = (PropertyDataGetResponseCRM) ResponseUtil.createSuccessResponse(propertyDataGetResponseCRM);
        }else{
        	propertyDataGetResponseCRM = (PropertyDataGetResponseCRM) ResponseUtil.createFailureResponse(propertyDataGetResponseCRM, validationResponse, validationResponse.getMaxCategory());
        }
        return propertyDataGetResponseCRM;
    }

    @Override
    public GenericResponse prepareUpdateCacheResponse(GenericResponse response, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response = ResponseUtil.createSuccessResponse(response);
        }else{
            response = ResponseUtil.createFailureResponse(response, validationResponse, validationResponse.getMaxCategory());
        }
        return response;
    }
    
    @Override
    public OrderDetailsCRMResponse prepareOrderDetailsResponse(OrderDetailsCRMResponse orderDetailsCRMResponse, OrderDetailsCRM orderDetailsCRM, ValidationResponse validationResponse){
        if(validationResponse.isValid()){
        	orderDetailsCRMResponse.setOrderDetailsCRM(orderDetailsCRM);
        	orderDetailsCRMResponse = (OrderDetailsCRMResponse) ResponseUtil.createSuccessResponse(orderDetailsCRMResponse);
        }else{
        	orderDetailsCRMResponse = (OrderDetailsCRMResponse) ResponseUtil.createFailureResponse(orderDetailsCRMResponse, validationResponse, validationResponse.getMaxCategory());
        }
        return orderDetailsCRMResponse;
    }

    @Override    
    public CustomerDetailsCRMResponse prepareCustomerDetailsResponse(CustomerDetailsCRMResponse customerDetailsCRMResponse, CustomerCRM customerCRM, ValidationResponse validationResponse){
        if(validationResponse.isValid()){
        	customerDetailsCRMResponse.setCustomerCRM(customerCRM);
        	customerDetailsCRMResponse = (CustomerDetailsCRMResponse) ResponseUtil.createSuccessResponse(customerDetailsCRMResponse);
        }else{
        	customerDetailsCRMResponse = (CustomerDetailsCRMResponse) ResponseUtil.createFailureResponse(customerDetailsCRMResponse, validationResponse, validationResponse.getMaxCategory());
        }
        return customerDetailsCRMResponse;
    }
    
    
    @Override    
    public CustomerSearchCRMResponse prepareCustomerSearchCRMResponse(CustomerSearchCRMResponse customerSearchCRMResponse, CustomerSearchDTOs customerSearchDTOs, ValidationResponse validationResponse){
        if(validationResponse.isValid()){
        	customerSearchCRMResponse.setCustomerSearchDTOs(customerSearchDTOs);
        	customerSearchCRMResponse = (CustomerSearchCRMResponse) ResponseUtil.createSuccessResponse(customerSearchCRMResponse);
        }else{
        	customerSearchCRMResponse = (CustomerSearchCRMResponse) ResponseUtil.createFailureResponse(customerSearchCRMResponse, validationResponse, validationResponse.getMaxCategory());
        }
        return customerSearchCRMResponse;
    }

    
    
}
