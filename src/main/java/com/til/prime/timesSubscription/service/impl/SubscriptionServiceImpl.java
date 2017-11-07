package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.constants.RedisConstants;
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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Resource
    private CacheManager cacheManager;
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
            BusinessEnum businessEnum = BusinessEnum.valueOf(request.getBusiness());
            List<SubscriptionPlanModel> subscriptionPlanModels = null;
            if(request.getPlanId()!=null) {
                subscriptionPlanModels = subscriptionPlanRepository.findByIdAndBusinessAndCountryAndDeleted(request.getPlanId(), BusinessEnum.valueOf(request.getBusiness()), CountryEnum.valueOf(request.getCountry()), false);
            }else{
                subscriptionPlanModels = subscriptionPlanRepository.findByBusinessAndCountryAndDeleted(BusinessEnum.valueOf(request.getBusiness()), CountryEnum.valueOf(request.getCountry()), false);
            }
            subscriptionPlans = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(subscriptionPlanModels)) {
                for (SubscriptionPlanModel subscriptionPlanModel : subscriptionPlanModels) {
                    Collections.sort(subscriptionPlanModel.getVariants());
                    if(request.getUser()!=null){
                        UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), businessEnum, true, false);
                        subscriptionPlans.add(ModelToDTOConvertorUtil.getSubscriptionPlanDTO(subscriptionPlanModel, lastUserSubscription));
                    }else{
                        subscriptionPlans.add(ModelToDTOConvertorUtil.getSubscriptionPlanDTO(subscriptionPlanModel, null));
                    }
                }
            }
        }
        response = subscriptionServiceHelper.preparePlanListResponse(response, subscriptionPlans, validationResponse);
        return response;
    }

    @Override
    @Transactional
    public InitPurchaseResponse initPurchasePlan(InitPurchaseRequest request) {
        return initPurchasePlan(request, false);
    }

    @Override
    public InitPurchaseResponse initPurchasePlan(InitPurchaseRequest request, boolean crmRequest) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreInitPurchasePlan(request, crmRequest);
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
                restrictedUsageUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getMobile(), business, planType, true, false);
            }
            lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), business, true, false);
            validationResponse = subscriptionValidationService.validatePostInitPurchasePlan(request, subscriptionVariantModel, restrictedUsageUserSubscription, lastUserSubscription, crmRequest, validationResponse);
        }
        if(validationResponse.isValid()) {
            UserModel userModel = getOrCreateUser(request);
            userSubscriptionModel = subscriptionServiceHelper.generateInitPurchaseUserSubscription(request, subscriptionVariantModel, lastUserSubscription, userModel, request.getPrice(), crmRequest);
//            if (userSubscriptionModel.isOrderCompleted()) {
//                userSubscriptionModel = subscriptionServiceHelper.updateSSOStatus(userSubscriptionModel);
//            }
            EventEnum eventEnum = EventEnum.getEventByInitPlanStatus(userSubscriptionModel.getPlanStatus());
            userSubscriptionModel = saveUserSubscription(userSubscriptionModel, true, request.getUser().getSsoId(), request.getUser().getTicketId(), eventEnum);
        }
        response = subscriptionServiceHelper.prepareInitPurchaseResponse(response, userSubscriptionModel, lastUserSubscription, validationResponse);
        return response;
    }

    @Override
    public GenerateOrderResponse generateOrder(GenerateOrderRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreGenerateOrder(request);
        SubscriptionVariantModel subscriptionVariantModel = null;
        UserSubscriptionModel userSubscriptionModel = null;
        UserSubscriptionModel newUserSubscriptionModel = null;
        UserSubscriptionModel restrictedUsageUserSubscription = null;
        UserSubscriptionModel lastUserSubscription = null;
        GenerateOrderResponse response = new GenerateOrderResponse();
        PlanTypeEnum planType = null;
        BusinessEnum business = null;
        boolean useNewSubscription = false;
        if(validationResponse.isValid()){
            planType = PlanTypeEnum.valueOf(request.getPlanType());
            business = BusinessEnum.valueOf(request.getBusiness());
            userSubscriptionModel = userSubscriptionRepository.findByIdAndDeleted(request.getUserSubscriptionId(), false);
            if(userSubscriptionModel!=null) {
                subscriptionVariantModel = userSubscriptionModel.getSubscriptionVariant();
            }
            if(PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planType)){
                restrictedUsageUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getMobile(), business, planType, true, false);
            }
            validationResponse = subscriptionValidationService.validatePostGenerateOrder(request, subscriptionVariantModel, userSubscriptionModel, restrictedUsageUserSubscription, validationResponse);
        }
        if(validationResponse.isValid()) {
            if(request.isRetryOnFailure() || request.isRenewal() || request.isDuplicate()){
                useNewSubscription = true;
                if(request.isRenewal()){
                    lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), business, true, false);
                    newUserSubscriptionModel = new UserSubscriptionModel(lastUserSubscription, request, request.isRenewal());
                }else{
                    newUserSubscriptionModel = new UserSubscriptionModel(userSubscriptionModel, request, request.isRenewal());
                }
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
            userSubscriptionModel = userSubscriptionRepository.findByOrderIdAndSubscriptionVariantIdAndDeleted(request.getOrderId(), request.getVariantId(), false);
            validationResponse = subscriptionValidationService.validatePostSubmitPurchasePlan(request, userSubscriptionModel, validationResponse);
        }
        if(validationResponse.isValid()){
            UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(userSubscriptionModel.getUser().getMobile(), userSubscriptionModel.getBusiness(), true, false);
            userSubscriptionModel = subscriptionServiceHelper.updateSubmitPurchaseUserSubscription(request, userSubscriptionModel, lastUserSubscription);
            userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, userSubscriptionModel.getUser().getSsoId(), userSubscriptionModel.getTicketId(), userSubscriptionModel.isOrderCompleted()? EventEnum.PAYMENT_SUCCESS: EventEnum.PAYMENT_FAILURE);
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
                            userSubscriptionRepository.findByUserMobileAndOrderCompleted(request.getUser().getMobile(),true):
                            userSubscriptionRepository.findByUserMobileAndOrderCompletedAndDeleted(request.getUser().getMobile(),true, false);
                }else{
                    userSubscriptionModelList = request.isIncludeDeleted()?
                            userSubscriptionRepository.findByUserMobileAndBusinessAndOrderCompleted(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true):
                            userSubscriptionRepository.findByUserMobileAndBusinessAndOrderCompletedAndDeleted(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true, false);
                }
            }else{
                Date currentDate = new Date();
                UserSubscriptionModel userSubscriptionModel = new UserSubscriptionModel();
                if(StringUtils.isEmpty(request.getBusiness())){
                    userSubscriptionModel = request.isIncludeDeleted()?
                            userSubscriptionRepository.findFirstByUserMobileAndOrderCompletedAndStartDateBeforeAndEndDateAfter(request.getUser().getMobile(),true, currentDate, currentDate):
                            userSubscriptionRepository.findFirstByUserMobileAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(request.getUser().getMobile(),true, currentDate, currentDate, false);
                }else{
                    userSubscriptionModel = request.isIncludeDeleted()?
                            userSubscriptionRepository.findFirstByUserMobileAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfter(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true, currentDate, currentDate):
                            userSubscriptionRepository.findFirstByUserMobileAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true, currentDate, currentDate, false);
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
    public CancelSubscriptionResponse cancelSubscription(CancelSubscriptionRequest request, boolean serverRequest){
        ValidationResponse validationResponse = subscriptionValidationService.validatePreCancelSubscription(request, serverRequest);
        UserSubscriptionModel userSubscriptionModel = null;
        CancelSubscriptionResponse response = new CancelSubscriptionResponse();
        if(validationResponse.isValid()){
            userSubscriptionModel = userSubscriptionRepository.findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(request.getUserSubscriptionId(), request.getOrderId(), request.getVariantId(), false);
            validationResponse = subscriptionValidationService.validatePostCancelSubscription(request, userSubscriptionModel, validationResponse);
        }
        BigDecimal refundAmount = null;
        if(validationResponse.isValid()){
            if(serverRequest && (((CancelSubscriptionServerRequest) request).isRefund())){
                if(((CancelSubscriptionServerRequest) request).getRefundAmount()!=null && ((CancelSubscriptionServerRequest) request).getRefundAmount()>0){
                    refundAmount = new BigDecimal(((CancelSubscriptionServerRequest) request).getRefundAmount());
                }else{
                    refundAmount = subscriptionServiceHelper.calculateRefundAmount(userSubscriptionModel);
                }
            }else {
                refundAmount = BigDecimal.ZERO;
            }
            boolean success = true;
            if(refundAmount.compareTo(BigDecimal.ZERO)>0){
                success = subscriptionServiceHelper.refundPayment(userSubscriptionModel.getOrderId(), refundAmount.doubleValue());
            }
            if(success){
                userSubscriptionModel.setIsDelete(true);
                userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, null, null, serverRequest? EventEnum.SUBSCRIPTION_SERVER_CANCELLATION: EventEnum.SUBSCRIPTION_APP_CANCELLATION);
            }else{
                validationResponse.getValidationErrorSet().add(ValidationError.PAYMENT_REFUND_ERROR);
            }
        }
        response = subscriptionServiceHelper.prepareCancelSubscriptionResponse(response, refundAmount, validationResponse);
        return response;
    }

    @Override
    public GenericResponse turnOffAutoDebit(TurnOffAutoDebitRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreTurnOffAutoDebit(request);
        List<UserSubscriptionModel> userSubscriptionModelList = null;
        GenericResponse response = new GenericResponse();
        if(validationResponse.isValid()){
            userSubscriptionModelList = userSubscriptionRepository.findByUserMobileAndStatusInAndOrderCompletedTrueAndDeletedFalse(request.getUser().getMobile(), StatusEnum.VALID_TURN_OFF_DEBIT_STATUS_SET);
            validationResponse = subscriptionValidationService.validatePostTurnOffAutoDebit(request, userSubscriptionModelList, validationResponse);
        }
        response = subscriptionServiceHelper.prepareTurnOffAutoDebitResponse(response, validationResponse);
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
    public GenericValidationResponse checkEligibility(CheckEligibilityRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreCheckEligibility(request);
        boolean validExecution = false;
        UserSubscriptionModel restrictedUserSubscription = null;
        UserSubscriptionModel lastUserSubscription = null;
        SubscriptionVariantModel subscriptionVariantModel = null;
        GenericValidationResponse response = new GenericValidationResponse();
        PlanTypeEnum planType = null;
        BusinessEnum business = null;
        if(validationResponse.isValid()){
            planType = PlanTypeEnum.valueOf(request.getPlanType());
            business = BusinessEnum.valueOf(request.getBusiness());
            subscriptionVariantModel = subscriptionVariantRepository.findByIdAndSubscriptionPlanIdAndDeleted(request.getVariantId(), request.getPlanId(), false);
            if(PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planType)){
                restrictedUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getMobile(), business, planType, true, false);
            }
            lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), business, true, false);
            validationResponse = subscriptionValidationService.validatePostCheckEligibility(request, subscriptionVariantModel, lastUserSubscription, restrictedUserSubscription, validationResponse);
            validExecution = true;
        }
        response = subscriptionServiceHelper.prepareCheckEligibilityResponse(response, validationResponse, validExecution);
        return response;
    }

    @Override
    public GenericValidationResponse checkValidVariant(CheckValidVariantRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreValidVariant(request);
        boolean validExecution = false;
        SubscriptionVariantModel subscriptionVariantModel = null;
        GenericValidationResponse response = new GenericValidationResponse();
        if(validationResponse.isValid()){
            subscriptionVariantModel = subscriptionVariantRepository.findByIdAndNameAndSubscriptionPlanIdAndDeleted(request.getVariantId(), request.getVariantName(), request.getPlanId(), false);
            validationResponse = subscriptionValidationService.validatePostValidVariant(request, subscriptionVariantModel, validationResponse);
            validExecution = true;
        }
        response = subscriptionServiceHelper.prepareValidVariantResponse(response, validationResponse, validExecution);
        return response;
    }

    @Override
    public CheckStatusResponse checkStatusViaApp(CheckStatusRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreCheckStatusViaApp(request);
        CheckStatusResponse response = new CheckStatusResponse();
        SubscriptionStatusDTO statusDTO = null;
        if(validationResponse.isValid()) {
            Cache.ValueWrapper vw = cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).get(request.getUser().getMobile());
            if(vw!=null){
                statusDTO = (SubscriptionStatusDTO) vw.get();
            }
        }
        if(statusDTO!=null){
            response = subscriptionServiceHelper.prepareCheckStatusResponse(response, false, statusDTO, validationResponse);
            return response;
        }
        UserSubscriptionModel userSubscriptionModel = null;
        if(validationResponse.isValid()){
            userSubscriptionModel = userSubscriptionRepository.findByUserMobileAndStatusAndDeletedAndOrderCompletedTrue(request.getUser().getMobile(), StatusEnum.ACTIVE, false);
            validationResponse = subscriptionValidationService.validatePostCheckStatus(request, userSubscriptionModel, validationResponse);
        }
        if(validationResponse.isValid()){
            updateUserStatus(userSubscriptionModel);
        }
        response = subscriptionServiceHelper.prepareCheckStatusResponse(response, false, statusDTO, validationResponse);
        return response;
    }

    @Override
    public CheckStatusResponse checkStatusViaServer(CheckStatusRequest request, boolean external) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreCheckStatusViaServer(request, external);
        CheckStatusResponse response = new CheckStatusResponse();
        SubscriptionStatusDTO statusDTO = null;
        if(validationResponse.isValid()) {
            Cache.ValueWrapper vw = cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).get(request.getUser().getMobile());
            if(vw!=null){
                statusDTO = (SubscriptionStatusDTO) vw.get();
            }
        }
        response = subscriptionServiceHelper.prepareCheckStatusResponse(response, external, statusDTO, validationResponse);
        return response;
    }

    @Override
    @Transactional
    public UserModel getOrCreateUser(GenericRequest request) {
        UserModel userModel = userRepository.findByMobile(request.getUser().getSsoId());
        if(userModel==null){
            userModel = subscriptionServiceHelper.getUser(request);
            userRepository.save(userModel);
        }
        return userModel;
    }

    @Override
    @Transactional
    public UserSubscriptionModel saveUserSubscription(UserSubscriptionModel userSubscriptionModel, boolean retryForOrderId, String ssoId, String ticketId, EventEnum event){
        int retryCount = retryForOrderId? GlobalConstants.DB_RETRY_COUNT : GlobalConstants.SINGLE_TRY;
        retryLoop:
        while (retryCount > 0) {
            try {
                userSubscriptionModel = userSubscriptionRepository.save(userSubscriptionModel);
                UserSubscriptionAuditModel auditModel = subscriptionServiceHelper.getUserSubscriptionAuditModel(userSubscriptionModel, event);
                auditModel = userSubscriptionAuditRepository.save(auditModel);
                updateUserStatus(userSubscriptionModel);
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

    private void updateUserStatus(UserSubscriptionModel userSubscriptionModel){
        if(userSubscriptionModel.getStatus()==StatusEnum.FUTURE){
            return;
        }
        String mobile = userSubscriptionModel.getUser().getMobile();
        cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).put(mobile, getSubscriptionStatusDTO(userSubscriptionModel));
    }

    private SubscriptionStatusDTO getSubscriptionStatusDTO(UserSubscriptionModel userSubscriptionModel){
        StatusEnum status = userSubscriptionModel.getStatus();
        SubscriptionStatusDTO statusDTO = new SubscriptionStatusDTO();
        statusDTO.setUserId(userSubscriptionModel.getUser().getId());
        statusDTO.setStartDate(userSubscriptionModel.getStartDate());
        statusDTO.setEndDate(userSubscriptionModel.getEndDate());
        if(status==StatusEnum.EXPIRED){
            if(userSubscriptionModel.getPlanStatus()==PlanStatusEnum.SUBSCRIPTION || userSubscriptionModel.getPlanStatus()==PlanStatusEnum.SUBSCRIPTION_AUTO_RENEWAL){
                statusDTO.setPlanStatus(PlanStatusEnum.SUBSCRIPTION_EXPIRED.getCode());
            }else if(userSubscriptionModel.getPlanStatus()==PlanStatusEnum.FREE_TRIAL){
                statusDTO.setPlanStatus(PlanStatusEnum.FREE_TRAIL_EXPIRED.getCode());
            }else if(userSubscriptionModel.getPlanStatus()==PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT){
                statusDTO.setPlanStatus(PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT_EXPIRED.getCode());
            }
        }else{
            statusDTO.setPlanStatus(userSubscriptionModel.getPlanStatus().getCode());
        }
        if(userSubscriptionModel.isDeleted()){
            statusDTO.setPlanStatus(PlanStatusEnum.SUBSCRIPTION_CANCELLED.getCode());
        }
        statusDTO.setAutoRenewal(userSubscriptionModel.isAutoRenewal());
        return statusDTO;
    }
}
