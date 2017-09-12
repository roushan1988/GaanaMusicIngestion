package com.til.prime.timesSubscription.service.impl;

import com.google.common.collect.Maps;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.convertor.ModelToDTOConvertorUtil;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.enums.*;
import com.til.prime.timesSubscription.model.SubscriptionVariantModel;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionAuditModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.service.SubscriptionServiceHelper;
import com.til.prime.timesSubscription.util.HttpConnectionUtils;
import com.til.prime.timesSubscription.util.OrderIdGeneratorUtil;
import com.til.prime.timesSubscription.util.ResponseUtil;
import com.til.prime.timesSubscription.util.TimeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class SubscriptionServiceHelperImpl implements SubscriptionServiceHelper {

    @Autowired
    private HttpConnectionUtils httpConnectionUtils;
    @Resource(name = "config_properties")
    private Properties properties;

    public UserSubscriptionModel generateInitPurchaseUserSubscription(InitPurchaseRequest request, SubscriptionVariantModel variantModel, UserSubscriptionModel lastUserSubscription, UserModel userModel, BigDecimal price){
        PlanTypeEnum planType = PlanTypeEnum.valueOf(request.getPlanType());
        UserSubscriptionModel userSubscriptionModel = new UserSubscriptionModel();
        userSubscriptionModel.setUser(userModel);
        userSubscriptionModel.setTicketId(request.getUser().getTicketId());
        userSubscriptionModel.setSubscriptionVariant(variantModel);
        userSubscriptionModel.setStartDate(lastUserSubscription==null? new Date(): TimeUtils.addMillisInDate(lastUserSubscription.getEndDate(), 1));
        userSubscriptionModel.setEndDate(TimeUtils.addDaysInDate(userSubscriptionModel.getStartDate(), request.getDurationDays().intValue()));
        userSubscriptionModel.setPlanStatus(PlanStatusEnum.INIT);
        userSubscriptionModel.setBusiness(variantModel.getSubscriptionPlan().getBusiness());
        userSubscriptionModel.setChannel(ChannelEnum.valueOf(request.getChannel()));
        userSubscriptionModel.setPlatform(PlatformEnum.valueOf(request.getPlatform()));
        userSubscriptionModel.setCreated(new Date());
        if(price.compareTo(BigDecimal.ZERO)<=0){
            String orderId = OrderIdGeneratorUtil.generateOrderId(request.getUser().getSsoId(), request.getUser().getTicketId(), GlobalConstants.ORDER_ID_LENGTH);
            userSubscriptionModel.setOrderId(orderId);
            userSubscriptionModel.setPaymentMethod(GlobalConstants.PAYMENT_METHOD_NA);
            userSubscriptionModel.setPaymentReference(GlobalConstants.PAYMENT_REFERENCE_NA);
            userSubscriptionModel.setOrderCompleted(true);
            userSubscriptionModel.setPlanStatus(PlanStatusEnum.getPlanStatus(planType, variantModel.getPrice(), lastUserSubscription,  false));
            userSubscriptionModel.setTransactionStatus(TransactionStatusEnum.SUBSCRIPTION_TRANS_SUCCESS);
        }else{
            userSubscriptionModel.setTransactionStatus(TransactionStatusEnum.SUBSCRIPTION_TRANS_INITIATED);
        }
        return userSubscriptionModel;
    }

    @Override
    public UserSubscriptionModel updateGenerateOrderUserSubscription(GenerateOrderRequest request, UserSubscriptionModel userSubscriptionModel) {
        String orderId = OrderIdGeneratorUtil.generateOrderId(request.getUser().getSsoId(), request.getUser().getTicketId(), GlobalConstants.ORDER_ID_LENGTH);
        userSubscriptionModel.setOrderId(orderId);
        userSubscriptionModel.setPaymentMethod(request.getPaymentMethod());
        return userSubscriptionModel;
    }

    @Override
    public UserSubscriptionModel updateSubmitPurchaseUserSubscription(SubmitPurchaseRequest request, UserSubscriptionModel userSubscriptionModel, UserSubscriptionModel lastUserSubscription) {
        SubscriptionVariantModel variantModel = userSubscriptionModel.getSubscriptionVariant();
        userSubscriptionModel.setPlanStatus(PlanStatusEnum.getPlanStatus(variantModel.getPlanType(), variantModel.getPrice(), lastUserSubscription,  request.isAutoRenewal()));
        userSubscriptionModel.setPaymentReference(request.getPaymentReference());
        userSubscriptionModel.setTransactionStatus(request.isPaymentSuccess()? TransactionStatusEnum.SUBSCRIPTION_TRANS_SUCCESS: TransactionStatusEnum.SUBSCRIPTION_TRANS_FAILED);
        userSubscriptionModel.setOrderCompleted(request.isPaymentSuccess());
        userSubscriptionModel.setAutoRenewal(request.isAutoRenewal());
        if (userSubscriptionModel.isOrderCompleted()) {
            userSubscriptionModel = updateSSOStatus(userSubscriptionModel);
        }
        return userSubscriptionModel;
    }

    public UserSubscriptionModel updateSSOStatus(UserSubscriptionModel userSubscriptionModel){
        boolean success = communicateSSO(userSubscriptionModel);
        userSubscriptionModel.setSsoCommunicated(success);
        return userSubscriptionModel;
    }

    @Override
    public PlanListResponse preparePlanListResponse(PlanListResponse response, List<SubscriptionPlanDTO> subscriptionPlans, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response.setPlanDTOList(subscriptionPlans);
            response = (PlanListResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (PlanListResponse) ResponseUtil.createFailureResponse(response, validationResponse.getValidationErrorSet());
        }
        return response;
    }

    @Override
    public InitPurchaseResponse prepareInitPurchaseResponse(InitPurchaseResponse response, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            SubscriptionVariantModel variantModel = userSubscriptionModel.getSubscriptionVariant();
            response.setUserSubscriptionId(userSubscriptionModel.getId());
            response.setOrderId(userSubscriptionModel.getOrderId());
            response.setPlanId(variantModel.getSubscriptionPlan().getId());
            response.setVariantId(variantModel.getId());
            response.setPaymentRequired(!userSubscriptionModel.isOrderCompleted());
            response.setPaymentAmount(userSubscriptionModel.getSubscriptionVariant().getPrice());
            response = (InitPurchaseResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (InitPurchaseResponse) ResponseUtil.createFailureResponse(response, validationResponse.getValidationErrorSet());
        }
        return response;
    }

    @Override
    public GenerateOrderResponse prepareGenerateOrderResponse(GenerateOrderResponse response, UserSubscriptionModel userSubscriptionModel, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            SubscriptionVariantModel variantModel = userSubscriptionModel.getSubscriptionVariant();
            response.setUserSubscriptionId(userSubscriptionModel.getId());
            response.setOrderId(userSubscriptionModel.getOrderId());
            response.setPlanId(variantModel.getSubscriptionPlan().getId());
            response.setVariantId(variantModel.getId());
            response = (GenerateOrderResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (GenerateOrderResponse) ResponseUtil.createFailureResponse(response, validationResponse.getValidationErrorSet());
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
            response = (SubmitPurchaseResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (SubmitPurchaseResponse) ResponseUtil.createFailureResponse(response, validationResponse.getValidationErrorSet());
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
            response = (PurchaseHistoryResponse) ResponseUtil.createFailureResponse(response, validationResponse.getValidationErrorSet());
        }
        return response;
    }

    @Override
    public CancelSubscriptionResponse prepareCancelSubscriptionResponse(CancelSubscriptionResponse response, BigDecimal refundAmount, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response.setRefundAmount(refundAmount);
            response = (CancelSubscriptionResponse) ResponseUtil.createSuccessResponse(response);
        }else{
            response = (CancelSubscriptionResponse) ResponseUtil.createFailureResponse(response, validationResponse.getValidationErrorSet());
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
        auditModel.setStartDate(userSubscriptionModel.getStartDate());
        auditModel.setEndDate(userSubscriptionModel.getEndDate());
        auditModel.setPlanStatus(userSubscriptionModel.getPlanStatus());
        auditModel.setTransactionStatus(userSubscriptionModel.getTransactionStatus());
        auditModel.setBusiness(userSubscriptionModel.getBusiness());
        auditModel.setChannel(userSubscriptionModel.getChannel());
        auditModel.setPlatform(userSubscriptionModel.getPlatform());
        auditModel.setAutoRenewal(userSubscriptionModel.isAutoRenewal());
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
        return price.multiply(new BigDecimal((time-startTime)/(endTime-startTime)));
    }

    @Override
    public UserSubscriptionModel extendTrial(UserSubscriptionModel userSubscriptionModel, Long extensionDays) {
        Date newEndDate = TimeUtils.addDaysInDate(userSubscriptionModel.getEndDate(), extensionDays.intValue());
        userSubscriptionModel.setEndDate(newEndDate);
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
            response = (ExtendExpiryResponse) ResponseUtil.createFailureResponse(response, validationResponse.getValidationErrorSet());
        }
        return response;
    }

    @Override
    public GenericResponse prepareCheckEligibilityResponse(GenericResponse response, ValidationResponse validationResponse) {
        if(validationResponse.isValid()){
            response = ResponseUtil.createSuccessResponse(response);
        }else{
            response = ResponseUtil.createFailureResponse(response, validationResponse.getValidationErrorSet());
        }
        return response;
    }

    @Override
    public UserModel getUser(GenericRequest request) {
        UserModel userModel = new UserModel();
        userModel.setSsoId(request.getUser().getSsoId());
        userModel.setName(request.getUser().getName());
        userModel.setMobile(request.getUser().getMobile());
        userModel.setEmail(request.getUser().getEmail());
        userModel.setCity(request.getUser().getCity());
        userModel.setCreated(new Date());
        return userModel;
    }

    private final boolean communicateSSO(UserSubscriptionModel userSubscriptionModel){
        int retryCount = GlobalConstants.API_RETRY_COUNT;
        RETRY_LOOP:
        while (retryCount>0){
            try{
                Map<String, Object> headers = Maps.newHashMap();
                headers.put(GlobalConstants.CHANNEL, properties.getProperty(GlobalConstants.TP_CHANNEL_KEY));
                headers.put(GlobalConstants.SSOID, userSubscriptionModel.getUser().getSsoId());
                headers.put(GlobalConstants.STATUS, userSubscriptionModel.getPlanStatus().getCode());
                headers.put(GlobalConstants.PLATFORM, userSubscriptionModel.getPlatform().getSsoChannel());
                String response = httpConnectionUtils.requestWithHeader(Maps.newHashMap(), headers, properties.getProperty(GlobalConstants.SSO_UPDATE_PROFILE_URL_KEY), GlobalConstants.CONTENT_TYPE_JSON, GlobalConstants.POST);
                SSOProfileUpdateResponse parsedResponse = GlobalConstants.gson.fromJson(response, SSOProfileUpdateResponse.class);
                if(parsedResponse.getCode()==200 && GlobalConstants.SUCCESS.equals(parsedResponse.getStatus()) && GlobalConstants.OK.equals(parsedResponse.getMessage())){
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

}
