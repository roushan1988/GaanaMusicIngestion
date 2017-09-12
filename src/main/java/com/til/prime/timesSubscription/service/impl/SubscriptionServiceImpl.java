package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.convertor.ModelToDTOConvertorUtil;
import com.til.prime.timesSubscription.dao.*;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.enums.*;
import com.til.prime.timesSubscription.model.*;
import com.til.prime.timesSubscription.service.SubscriptionService;
import com.til.prime.timesSubscription.service.SubscriptionServiceHelper;
import com.til.prime.timesSubscription.service.SubscriptionValidationService;
import com.til.prime.timesSubscription.util.OrderIdGeneratorUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionValidationService subscriptionValidationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private SubscriptionVariantRepository subscriptionVariantRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private UserSubscriptionAuditRepository userSubscriptionAuditRepository;
    @Autowired
    private SubscriptionServiceHelper subscriptionServiceHelper;

    @Override
    public PlanListResponse getAllPlans(PlanListRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validateAllPlans(request);
        PlanListResponse response = new PlanListResponse();
        List<SubscriptionPlanDTO> subscriptionPlans = null;
        if(validationResponse.isValid()){
            List<SubscriptionPlanModel> subscriptionPlanModels = subscriptionPlanRepository.findByBusinessAndCountryAndDeleted
                    (BusinessEnum.valueOf(request.getBusiness()), CountryEnum.valueOf(request.getCountry()), false);
            subscriptionPlans = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(subscriptionPlanModels)) {
                for (SubscriptionPlanModel subscriptionPlanModel : subscriptionPlanModels) {
                    subscriptionPlans.add(ModelToDTOConvertorUtil.getSubscriptionPlanDTO(subscriptionPlanModel));
                }
            }
        }
        response = subscriptionServiceHelper.preparePlanListResponse(response, subscriptionPlans, validationResponse);
        return response;
    }

    @Override
    @Transactional
    public InitPurchaseResponse initPurchasePlan(InitPurchaseRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreInitPurchasePlan(request);
        SubscriptionVariantModel subscriptionVariantModel = null;
        UserSubscriptionModel userSubscriptionModel = null;
        UserSubscriptionModel restrictedUsageUserSubscription = null;
        UserSubscriptionModel lastUserSubscription = null;
        InitPurchaseResponse response = new InitPurchaseResponse();
        PlanTypeEnum planType = null;
        BusinessEnum business = null;
        if(validationResponse.isValid()){
            planType = PlanTypeEnum.valueOf(request.getPlanType());
            business = BusinessEnum.valueOf(request.getBusiness());
            subscriptionVariantModel = subscriptionVariantRepository.findByIdAndSubscriptionPlanIdAndPriceAndDurationDaysAndDeleted(
                    request.getVariantId(), request.getPlanId(), request.getPrice(), request.getDurationDays(), false);
            if(PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planType)){
                restrictedUsageUserSubscription = userSubscriptionRepository.findFirstByUserSsoIdAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getSsoId(), business, planType, true, false);
            }
            lastUserSubscription = userSubscriptionRepository.findFirstByUserSsoIdAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getSsoId(), business, true, false);
            validationResponse = subscriptionValidationService.validatePostInitPurchasePlan(request, subscriptionVariantModel, restrictedUsageUserSubscription, lastUserSubscription, validationResponse);
        }
        if(validationResponse.isValid()) {
            UserModel userModel = getOrCreateUser(request);
            userSubscriptionModel = subscriptionServiceHelper.generateInitPurchaseUserSubscription(request, subscriptionVariantModel, lastUserSubscription, userModel, request.getPrice());
            if (userSubscriptionModel.isOrderCompleted()) {
                userSubscriptionModel = subscriptionServiceHelper.updateSSOStatus(userSubscriptionModel);
            }
            EventEnum eventEnum = EventEnum.getEventByInitPlanStatus(userSubscriptionModel.getPlanStatus());
            userSubscriptionModel = saveUserSubscription(userSubscriptionModel, true, request.getUser().getSsoId(), request.getUser().getTicketId(), eventEnum);
        }
        response = subscriptionServiceHelper.prepareInitPurchaseResponse(response, userSubscriptionModel, validationResponse);
        return response;
    }

    @Override
    public GenerateOrderResponse generateOrder(GenerateOrderRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreInitGenerateOrder(request);
        SubscriptionVariantModel subscriptionVariantModel = null;
        UserSubscriptionModel userSubscriptionModel = null;
        UserSubscriptionModel newUserSubscriptionModel = null;
        UserSubscriptionModel restrictedUsageUserSubscription = null;
        GenerateOrderResponse response = new GenerateOrderResponse();
        PlanTypeEnum planType = null;
        BusinessEnum business = null;
        boolean useNewSubscription = false;
        if(validationResponse.isValid()){
            planType = PlanTypeEnum.valueOf(request.getPlanType());
            business = BusinessEnum.valueOf(request.getBusiness());
            userSubscriptionModel = userSubscriptionRepository.findById(request.getUserSubscriptionId());
            if(userSubscriptionModel!=null) {
                subscriptionVariantModel = userSubscriptionModel.getSubscriptionVariant();
            }
            if(PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planType)){
                restrictedUsageUserSubscription = userSubscriptionRepository.findFirstByUserSsoIdAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getSsoId(), business, planType, true, false);
            }
            validationResponse = subscriptionValidationService.validatePostInitGenerateOrder(request, subscriptionVariantModel, userSubscriptionModel, restrictedUsageUserSubscription, validationResponse);
        }
        if(validationResponse.isValid()) {
            if(request.isRetryOnFailure()){
                useNewSubscription = true;
                newUserSubscriptionModel = new UserSubscriptionModel(userSubscriptionModel, request);
            }
            if(useNewSubscription){
                newUserSubscriptionModel = subscriptionServiceHelper.updateGenerateOrderUserSubscription(request, newUserSubscriptionModel);
                newUserSubscriptionModel = saveUserSubscription(newUserSubscriptionModel, true, request.getUser().getSsoId(), request.getUser().getTicketId(), EventEnum.ORDER_ID_GENERATION);
            }else{
                userSubscriptionModel = subscriptionServiceHelper.updateGenerateOrderUserSubscription(request, userSubscriptionModel);
                userSubscriptionModel = saveUserSubscription(userSubscriptionModel, true, request.getUser().getSsoId(), request.getUser().getTicketId(), EventEnum.ORDER_ID_GENERATION);
            }
        }
        response = subscriptionServiceHelper.prepareGenerateOrderResponse(response, useNewSubscription? newUserSubscriptionModel: userSubscriptionModel, validationResponse);
        return response;
    }

    @Override
    @Transactional
    public SubmitPurchaseResponse submitPurchasePlan(SubmitPurchaseRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreSubmitPurchasePlan(request);
        UserSubscriptionModel userSubscriptionModel = null;
        SubmitPurchaseResponse response = new SubmitPurchaseResponse();
        if(validationResponse.isValid()){
            userSubscriptionModel = userSubscriptionRepository.findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(
                    request.getUserSubscriptionId(), request.getOrderId(), request.getVariantId(), false);
            validationResponse = subscriptionValidationService.validatePostSubmitPurchasePlan(request, userSubscriptionModel, validationResponse);
        }
        if(validationResponse.isValid()){
            UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserSsoIdAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getSsoId(), userSubscriptionModel.getBusiness(), true, false);
            userSubscriptionModel = subscriptionServiceHelper.updateSubmitPurchaseUserSubscription(request, userSubscriptionModel, lastUserSubscription);
            userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, request.getUser().getSsoId(), request.getUser().getTicketId(), userSubscriptionModel.isOrderCompleted()? EventEnum.PAYMENT_SUCCESS: EventEnum.PAYMENT_FAILURE);
        }
        response = subscriptionServiceHelper.prepareSubmitPurchaseResponse(response, userSubscriptionModel, validationResponse);
        return response;
    }

    @Override
    @Transactional
    public PurchaseHistoryResponse getPurchaseHistory(PurchaseHistoryRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePurchaseHistory(request);
        PurchaseHistoryResponse response = new PurchaseHistoryResponse();
        List<UserSubscriptionDTO> userSubscriptionDTOList = null;
        List<UserSubscriptionModel> userSubscriptionModelList = null;
        if(validationResponse.isValid()){
            if(!request.isCurrentSubscription()){
                if(StringUtils.isEmpty(request.getBusiness())){
                    userSubscriptionModelList = request.isIncludeDeleted()?
                            userSubscriptionRepository.findByUserSsoIdAndOrderCompleted(request.getUser().getSsoId(),true):
                            userSubscriptionRepository.findByUserSsoIdAndOrderCompletedAndDeleted(request.getUser().getSsoId(),true, false);
                }else{
                    userSubscriptionModelList = request.isIncludeDeleted()?
                            userSubscriptionRepository.findByUserSsoIdAndBusinessAndOrderCompleted(request.getUser().getSsoId(), BusinessEnum.valueOf(request.getBusiness()), true):
                            userSubscriptionRepository.findByUserSsoIdAndBusinessAndOrderCompletedAndDeleted(request.getUser().getSsoId(), BusinessEnum.valueOf(request.getBusiness()), true, false);
                }
            }else{
                Date currentDate = new Date();
                UserSubscriptionModel userSubscriptionModel = new UserSubscriptionModel();
                if(StringUtils.isEmpty(request.getBusiness())){
                    userSubscriptionModel = request.isIncludeDeleted()?
                            userSubscriptionRepository.findFirstByUserSsoIdAndOrderCompletedAndStartDateBeforeAndEndDateAfter(request.getUser().getSsoId(),true, currentDate, currentDate):
                            userSubscriptionRepository.findFirstByUserSsoIdAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(request.getUser().getSsoId(),true, currentDate, currentDate, false);
                }else{
                    userSubscriptionModel = request.isIncludeDeleted()?
                            userSubscriptionRepository.findFirstByUserSsoIdAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfter(request.getUser().getSsoId(), BusinessEnum.valueOf(request.getBusiness()), true, currentDate, currentDate):
                            userSubscriptionRepository.findFirstByUserSsoIdAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(request.getUser().getSsoId(), BusinessEnum.valueOf(request.getBusiness()), true, currentDate, currentDate, false);
                }
                userSubscriptionModelList = new ArrayList<>();
                userSubscriptionModelList.add(userSubscriptionModel);
            }
            userSubscriptionDTOList = subscriptionServiceHelper.generateUserSubscriptionDTOList(userSubscriptionModelList);
        }
        response = subscriptionServiceHelper.preparePurchaseHistoryResponse(response, userSubscriptionDTOList, validationResponse);
        return response;
    }

    @Override
    public CancelSubscriptionResponse cancelSubscription(CancelSubscriptionRequest request){
        ValidationResponse validationResponse = subscriptionValidationService.validatePreCancelSubscription(request);
        UserSubscriptionModel userSubscriptionModel = null;
        CancelSubscriptionResponse response = new CancelSubscriptionResponse();
        if(validationResponse.isValid()){
            userSubscriptionModel = userSubscriptionRepository.findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(request.getUserSubscriptionId(), request.getOrderId(), request.getVariantId(), false);
            validationResponse = subscriptionValidationService.validatePostCancelSubscription(request, userSubscriptionModel, validationResponse);
        }
        BigDecimal refundAmount = null;
        if(validationResponse.isValid()){
            userSubscriptionModel.setIsDelete(true);
            userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, request.getUser().getSsoId(), request.getUser().getTicketId(), EventEnum.SUBSCRIPTION_CANCELLATION);
            refundAmount = subscriptionServiceHelper.calculateRefundAmount(userSubscriptionModel);
        }
        response = subscriptionServiceHelper.prepareCancelSubscriptionResponse(response, refundAmount, validationResponse);
        return response;
    }

    @Override
    public ExtendExpiryResponse extendExpiry(ExtendExpiryRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreExtendExpiry(request);
        UserSubscriptionModel userSubscriptionModel = null;
        ExtendExpiryResponse response = new ExtendExpiryResponse();
        if(validationResponse.isValid()){
            userSubscriptionModel = userSubscriptionRepository.findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(request.getUserSubscriptionId(), request.getOrderId(), request.getVariantId(), false);
            validationResponse = subscriptionValidationService.validatePostExtendExpiry(request, userSubscriptionModel, validationResponse);
        }
        if(validationResponse.isValid()){
            userSubscriptionModel = subscriptionServiceHelper.extendTrial(userSubscriptionModel, request.getExtensionDays());
            userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, request.getUser().getSsoId(), request.getUser().getTicketId(), EventEnum.SUBSCRIPTION_TRIAL_EXTENSION);
        }
        response = subscriptionServiceHelper.prepareExtendExpiryResponse(response, userSubscriptionModel, validationResponse);
        return response;
    }

    @Override
    public GenericResponse checkEligibility(CheckEligibilityRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreCheckEligibility(request);
        UserSubscriptionModel restrictedUserSubscription = null;
        UserSubscriptionModel lastUserSubscription = null;
        SubscriptionVariantModel subscriptionVariantModel = null;
        GenericResponse response = new GenericResponse();
        PlanTypeEnum planType = null;
        BusinessEnum business = null;
        if(validationResponse.isValid()){
            planType = PlanTypeEnum.valueOf(request.getPlanType());
            business = BusinessEnum.valueOf(request.getBusiness());
            subscriptionVariantModel = subscriptionVariantRepository.findByIdAndSubscriptionPlanIdAndDeleted(request.getVariantId(), request.getPlanId(), false);
            if(PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planType)){
                restrictedUserSubscription = userSubscriptionRepository.findFirstByUserSsoIdAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getSsoId(), business, planType, true, false);
            }
            lastUserSubscription = userSubscriptionRepository.findFirstByUserSsoIdAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getSsoId(), business, true, false);
            validationResponse = subscriptionValidationService.validatePostCheckEligibility(request, subscriptionVariantModel, lastUserSubscription, restrictedUserSubscription, validationResponse);
        }
        response = subscriptionServiceHelper.prepareCheckEligibilityResponse(response, validationResponse);
        return response;
    }

    @Override
    public UserModel getOrCreateUser(GenericRequest request) {
        UserModel userModel = userRepository.findBySsoId(request.getUser().getSsoId());
        if(userModel==null){
            userModel = subscriptionServiceHelper.getUser(request);
            userRepository.save(userModel);
        }
        return userModel;
    }

    @Override
    public UserSubscriptionModel saveUserSubscription(UserSubscriptionModel userSubscriptionModel, boolean retryForOrderId, String ssoId, String ticketId, EventEnum event){
        int retryCount = retryForOrderId? GlobalConstants.DB_RETRY_COUNT : GlobalConstants.SINGLE_TRY;
        retryLoop:
        while (retryCount > 0) {
            try {
                userSubscriptionModel = userSubscriptionRepository.save(userSubscriptionModel);
                UserSubscriptionAuditModel auditModel = subscriptionServiceHelper.getUserSubscriptionAuditModel(userSubscriptionModel, event);
                auditModel = userSubscriptionAuditRepository.save(auditModel);
                break retryLoop;
            } catch (Exception e) {
                retryCount--;
                if(retryCount>0){
                    userSubscriptionModel.setOrderId(OrderIdGeneratorUtil.generateOrderId(ssoId, ticketId, GlobalConstants.ORDER_ID_LENGTH));
                    continue retryLoop;
                }
                throw e;
            }
        }
        return userSubscriptionModel;
    }
}
