package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.constants.RedisConstants;
import com.til.prime.timesSubscription.convertor.ModelToDTOConvertorUtil;
import com.til.prime.timesSubscription.dao.*;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.dto.internal.RefundInternalResponse;
import com.til.prime.timesSubscription.enums.*;
import com.til.prime.timesSubscription.model.*;
import com.til.prime.timesSubscription.service.*;
import com.til.prime.timesSubscription.util.TimeUtils;
import com.til.prime.timesSubscription.util.UniqueIdGeneratorUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
	
    private static final Logger LOG = Logger.getLogger(SubscriptionServiceImpl.class);

    @Resource
    private CacheManager cacheManager;
    @Autowired
    private SubscriptionValidationService subscriptionValidationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuditRepository userAuditRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private SubscriptionVariantRepository subscriptionVariantRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private UserSubscriptionAuditRepository userSubscriptionAuditRepository;
    @Autowired
    private BackendSubscriptionUserRepository backendSubscriptionUserRepository;
    @Autowired
    private BackendSubscriptionUserAuditRepository backendSubscriptionUserAuditRepository;
    @Autowired
    private SubscriptionServiceHelper subscriptionServiceHelper;
    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private PropertyService propertyService;
    @Resource(name = "config_properties")
    private Properties properties;

    @Override
    public PlanListResponse getAllPlans(PlanListRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreAllPlans(request);
        PlanListResponse response = new PlanListResponse();
        List<SubscriptionPlanDTO> subscriptionPlans = null;
        if(validationResponse.isValid() && request.getUser()!=null){
            UserModel userModel = userRepository.findByMobileAndDeletedFalse(request.getUser().getMobile());
            validationResponse = subscriptionValidationService.validatePostAllPlans(userModel, validationResponse);
        }
        if(validationResponse.isValid()){
            if(request.getUser()!=null) {
                UserModel userModel = getOrCreateUserWithMobileCheck(request, validationResponse);
                List<SubscriptionPlanModel> subscriptionPlanModels = null;
                boolean flag = false;
                if (request.getPlanId() != null) {
                    subscriptionPlanModels = subscriptionPlanRepository.findByIdAndBusinessAndCountryAndDeleted(request.getPlanId(), request.getBusiness(), request.getCountry(), false);
                    flag = true;
                } else {
                    subscriptionPlanModels = propertyService.getAllPlanModels(request.getBusiness(), request.getCountry());
                }
                subscriptionPlans = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(subscriptionPlanModels)) {
                    for (SubscriptionPlanModel subscriptionPlanModel : subscriptionPlanModels) {
                        if(flag){
                            Collections.sort(subscriptionPlanModel.getVariants());
                        }
                        UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), request.getBusiness(), true, false);
                        subscriptionPlans.add(ModelToDTOConvertorUtil.getSubscriptionPlanDTO(subscriptionPlanModel, lastUserSubscription));
                    }
                }
            }else{
                if (request.getPlanId() != null) {
                    List<SubscriptionPlanModel> subscriptionPlanModels = subscriptionPlanRepository.findByIdAndBusinessAndCountryAndDeleted(request.getPlanId(), request.getBusiness(), request.getCountry(), false);
                    subscriptionPlans = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(subscriptionPlanModels)) {
                        for (SubscriptionPlanModel subscriptionPlanModel : subscriptionPlanModels) {
                            Collections.sort(subscriptionPlanModel.getVariants());
                            subscriptionPlans.add(ModelToDTOConvertorUtil.getSubscriptionPlanDTO(subscriptionPlanModel, null));
                        }
                    }
                } else {
                    subscriptionPlans = propertyService.getAllPlans(request.getBusiness(), request.getCountry());
                }
            }
        }
        response = subscriptionServiceHelper.preparePlanListResponse(response, subscriptionPlans, validationResponse);
        return response;
    }

    @Override
    @Transactional
    public InitPurchaseResponse initPurchasePlan(InitPurchaseRequest request) {
        return initPurchasePlan(request, false, false);
    }

    @Override
    public InitPurchaseResponse initPurchasePlan(InitPurchaseRequest request, boolean crmRequest, boolean isFree) {
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
                restrictedUsageUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getMobile(), business, planType, true, false);
            }
            lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), business, true, false);
            validationResponse = subscriptionValidationService.validatePostInitPurchasePlan(request, subscriptionVariantModel, restrictedUsageUserSubscription, lastUserSubscription, crmRequest, validationResponse);
        }
        if(validationResponse.isValid()) {
            UserModel userModel = getOrCreateUserWithMobileCheck(request, validationResponse);
            if(validationResponse.isValid()){
                userSubscriptionModel = subscriptionServiceHelper.generateInitPurchaseUserSubscription(request, subscriptionVariantModel, lastUserSubscription, userModel, request.getPrice(), crmRequest, isFree);
                EventEnum eventEnum = EventEnum.getEventByInitPlanStatus(userSubscriptionModel.getPlanStatus());
                userSubscriptionModel = saveUserSubscription(userSubscriptionModel, true, request.getUser().getSsoId(), request.getUser().getTicketId(), eventEnum);
                    if (userSubscriptionModel.isOrderCompleted()) {
                        if (PlanStatusEnum.FREE_TRIAL.equals(userSubscriptionModel.getPlanStatus())) {

                            communicationService.sendFreeTrialSubscriptionSuccessCommunication(userSubscriptionModel);
                        } else {
                            communicationService.sendPaidSubscriptionSuccessCommunication(userSubscriptionModel);
                        }
                    } else {
                    if(crmRequest){
                        boolean success = subscriptionServiceHelper.renewSubscription(userSubscriptionModel);
                        if(success){
                            UserSubscriptionModel userSubscriptionModel2 = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrueOrderById(
                                    userSubscriptionModel.getUser().getMobile(), StatusEnum.ACTIVE, TimeUtils.addMillisInDate(userSubscriptionModel.getEndDate(), -2000));
                            if(userSubscriptionModel2!=null){
                                UserSubscriptionAuditModel auditModel = subscriptionServiceHelper.getUserSubscriptionAuditModel(userSubscriptionModel2, EventEnum.SUBSCRIPTION_AUTO_RENEWAL);
                                userSubscriptionAuditRepository.save(auditModel);
                            }
                        }
                    }
                }
            }
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
                restrictedUsageUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getMobile(), business, planType, true, false);
            }
            validationResponse = subscriptionValidationService.validatePostGenerateOrder(request, subscriptionVariantModel, userSubscriptionModel, restrictedUsageUserSubscription, validationResponse);
        }
        if(validationResponse.isValid()) {
            if(userSubscriptionModel!=null && StringUtils.isNotEmpty(userSubscriptionModel.getOrderId())){
                request.setDuplicate(true);
            }
            if(request.isRetryOnFailure() || request.isRenewal() || request.isDuplicate()){
                useNewSubscription = true;
                if(request.isRenewal()){
                    lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), business, true, false);
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
        if (validationResponse.isValid()) {
            UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(userSubscriptionModel.getUser().getMobile(), userSubscriptionModel.getBusiness(), true, false);
            userSubscriptionModel = subscriptionServiceHelper.updateSubmitPurchaseUserSubscription(request, userSubscriptionModel, lastUserSubscription);
            userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, userSubscriptionModel.getUser().getSsoId(), userSubscriptionModel.getTicketId(), userSubscriptionModel.isOrderCompleted() ? EventEnum.PAYMENT_SUCCESS : EventEnum.PAYMENT_FAILURE);
            if (userSubscriptionModel.getStatus() == StatusEnum.ACTIVE) {
                updateUserStatus(userSubscriptionModel, userSubscriptionModel.getUser());
            }
            if (userSubscriptionModel.isOrderCompleted()) {
                if (PlanStatusEnum.SUBSCRIPTION_AUTO_RENEWAL.equals(userSubscriptionModel.getPlanStatus())) {
                    communicationService.sendSubscriptionRenewedCommunication(userSubscriptionModel);
                } else if (PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT.equals(userSubscriptionModel.getPlanStatus()) || PlanStatusEnum.SUBSCRIPTION.equals(userSubscriptionModel.getPlanStatus())) {
                    communicationService.sendPaidSubscriptionSuccessCommunication(userSubscriptionModel);
                }
            }
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
                            userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndOrderCompleted(request.getUser().getMobile(),true):
                            userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndOrderCompletedAndDeleted(request.getUser().getMobile(),true, false);
                }else{
                    userSubscriptionModelList = request.isIncludeDeleted()?
                            userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompleted(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true):
                            userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeleted(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true, false);
                }
            }else{
                Date currentDate = new Date();
                UserSubscriptionModel userSubscriptionModel = new UserSubscriptionModel();
                if(StringUtils.isEmpty(request.getBusiness())){
                    userSubscriptionModel = request.isIncludeDeleted()?
                            userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndOrderCompletedAndStartDateBeforeAndEndDateAfter(request.getUser().getMobile(),true, currentDate, currentDate):
                            userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(request.getUser().getMobile(),true, currentDate, currentDate, false);
                }else{
                    userSubscriptionModel = request.isIncludeDeleted()?
                            userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfter(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true, currentDate, currentDate):
                            userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true, currentDate, currentDate, false);
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
        BigDecimal refundedAmount = null;
        if(validationResponse.isValid()){
            BigDecimal refundAmount = null;
            boolean forceAmount = false;
            if(serverRequest && (((CancelSubscriptionServerRequest) request).isRefund())){
                if(((CancelSubscriptionServerRequest) request).getRefundAmount()!=null && ((CancelSubscriptionServerRequest) request).getRefundAmount()>0){
                    refundAmount = new BigDecimal(((CancelSubscriptionServerRequest) request).getRefundAmount());
                    forceAmount = true;
                }else{
                    refundAmount = subscriptionServiceHelper.calculateRefundAmount(userSubscriptionModel);
                }
            }else {
                refundAmount = BigDecimal.ZERO;
            }
            RefundInternalResponse refundResponse = null;
            if(refundAmount.compareTo(BigDecimal.ZERO)>0){
                refundResponse = subscriptionServiceHelper.refundPayment(userSubscriptionModel.getOrderId(), refundAmount.doubleValue(), forceAmount);
            }
            if(refundResponse.isSuccess()){
                userSubscriptionModel.setIsDelete(true);
                userSubscriptionModel.setStatus(StatusEnum.CANCELLED);
                if(refundResponse.getRefundedAmount()!=null){
                    refundedAmount = new BigDecimal(refundResponse.getRefundedAmount());
                    refundedAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                }
                userSubscriptionModel.setRefundedAmount(refundedAmount);
                userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, null, null, serverRequest? EventEnum.SUBSCRIPTION_SERVER_CANCELLATION: EventEnum.SUBSCRIPTION_APP_CANCELLATION);
                updateUserStatus(userSubscriptionModel, userSubscriptionModel.getUser());
                communicationService.sendSubscriptionCancellationCommunication(userSubscriptionModel);
            }else{
                validationResponse.addValidationError(ValidationError.PAYMENT_REFUND_ERROR);
            }
        }
        response = subscriptionServiceHelper.prepareCancelSubscriptionResponse(response, refundedAmount, validationResponse);
        return response;
    }

    @Override
    public GenericResponse turnOffAutoDebit(TurnOffAutoDebitRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreTurnOffAutoDebit(request);
        List<UserSubscriptionModel> userSubscriptionModelList = null;
        GenericResponse response = new GenericResponse();
        if(validationResponse.isValid()){
            userSubscriptionModelList = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusInAndOrderCompletedTrueAndAutoRenewalTrueAndDeletedFalse(request.getUser().getMobile(), StatusEnum.VALID_TURN_OFF_DEBIT_STATUS_SET);
            validationResponse = subscriptionValidationService.validatePostTurnOffAutoDebit(request, userSubscriptionModelList, validationResponse);
        }
        if(validationResponse.isValid()){
            for(UserSubscriptionModel userSubscriptionModel: userSubscriptionModelList){
                userSubscriptionModel.setAutoRenewal(false);
                userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, null, null, EventEnum.SUBSCRIPTION_TURN_OFF_AUTO_DEBIT);
                if(userSubscriptionModel.getStatus()==StatusEnum.ACTIVE){
                    updateUserStatus(userSubscriptionModel, userSubscriptionModel.getUser());
                }
            }
        }
        response = subscriptionServiceHelper.prepareTurnOffAutoDebitResponse(response, validationResponse);
        return response;
    }

    @Override
    public GenericResponse blockUnblockUser(BlockUnblockRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreBlockUnblockUser(request);
        UserModel userModel = null;
        GenericResponse response = new GenericResponse();
        if(validationResponse.isValid()){
            userModel = userRepository.findByMobileAndDeletedFalse(request.getUser().getMobile());
            validationResponse = subscriptionValidationService.validatePostBlockUnblockUser(request, userModel, validationResponse);
        }
        if(validationResponse.isValid()){
            userModel.setBlocked(request.isBlockUser());
            userModel = saveUserModel(userModel, request.isBlockUser()? EventEnum.USER_BLOCK: EventEnum.USER_UNBLOCK);
            UserSubscriptionModel userSubscriptionModel = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusAndOrderCompletedTrue(request.getUser().getMobile(), StatusEnum.ACTIVE);
            updateUserStatus(userSubscriptionModel, userModel);
        }
        response = subscriptionServiceHelper.prepareBlockUnblockResponse(response, validationResponse);
        return response;
    }

    @Override
    public ExtendExpiryResponse extendExpiry(ExtendExpiryRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreExtendExpiry(request);
        UserSubscriptionModel userSubscriptionModel = null;
        UserSubscriptionModel lastUserSubscription = null;
        ExtendExpiryResponse response = new ExtendExpiryResponse();
        if(validationResponse.isValid()){
            userSubscriptionModel = userSubscriptionRepository.findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(request.getUserSubscriptionId(), request.getOrderId(), request.getVariantId(), false);
            lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), BusinessEnum.TIMES_PRIME, true, false);
            validationResponse = subscriptionValidationService.validatePostExtendExpiry(request, userSubscriptionModel, lastUserSubscription, validationResponse);
        }
        if(validationResponse.isValid()){
            userSubscriptionModel = subscriptionServiceHelper.extendSubscription(userSubscriptionModel, request.getExtensionDays());
            userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, request.getUser().getSsoId(), request.getUser().getTicketId(), EventEnum.SUBSCRIPTION_TRIAL_EXTENSION);
            if(userSubscriptionModel.getStatus()==StatusEnum.ACTIVE){
                updateUserStatus(userSubscriptionModel, userSubscriptionModel.getUser());
            }
            communicationService.sendSubscriptionExpiryExtensionCommunication(userSubscriptionModel);
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
                restrictedUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getMobile(), business, planType, true, false);
            }
            lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), business, true, false);
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
            statusDTO = getSubscriptionStatusWithValidation(request, true, validationResponse);
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
            statusDTO = getSubscriptionStatusWithValidation(request, !external, validationResponse);
        }
        response = subscriptionServiceHelper.prepareCheckStatusResponse(response, external, statusDTO, validationResponse);
        return response;
    }

    private SubscriptionStatusDTO getSubscriptionStatusWithValidation(CheckStatusRequest request, boolean updateCache, ValidationResponse validationResponse){
        SubscriptionStatusDTO statusDTO = null;
        Cache.ValueWrapper vw = cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).get(request.getUser().getMobile());
        if(vw!=null){
            statusDTO = (SubscriptionStatusDTO) vw.get();
        }
        if(statusDTO == null && updateCache){ //cache update step
            UserSubscriptionModel userSubscriptionModel = null;
            userSubscriptionModel = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndStatusInAndDeletedFalseAndOrderCompletedTrueOrderByIdDesc(request.getUser().getMobile(), StatusEnum.VALID_USER_STATUS_HISTORY_SET);
            subscriptionValidationService.validatePostCheckStatus(request, userSubscriptionModel, validationResponse);
            if(validationResponse.isValid()){
                updateUserStatus(userSubscriptionModel);
                vw = cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).get(request.getUser().getMobile());
                if(vw!=null){
                    statusDTO = (SubscriptionStatusDTO) vw.get();
                }
            }
        }
        return statusDTO;
    }

    @Override
    public BackendSubscriptionResponse backendSubscriptionViaServer(BackendSubscriptionRequest request) {
        BackendSubscriptionResponse response = new BackendSubscriptionResponse();
        BackendSubscriptionValidationResponse validationResponse = subscriptionValidationService.validateBackendSubscriptionRequest(request);
        List<BackendActivationUserDTO> successList = new ArrayList<>();
        List<BackendActivationUserDTO> failureList = new ArrayList<>();
        if(validationResponse.getValidationErrors().isEmpty()){
            failureList.addAll(validationResponse.getFailureList());
            request.getUserList().removeAll(failureList);
            if(CollectionUtils.isNotEmpty(request.getUserList())){
                for(BackendActivationUserDTO dto: request.getUserList()){
                    BackendSubscriptionUserModel model = backendSubscriptionUserRepository.findByMobileAndDeletedFalse(dto.getMobile());
                    if(model==null || (model!=null && model.isCompleted() && dto.isForceActivation())){
                        UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(dto.getMobile(), BusinessEnum.TIMES_PRIME, true, false);
                        if(lastUserSubscription!=null && lastUserSubscription.getStatus()==StatusEnum.FUTURE){
                            dto.setMessage(ValidationError.FUTURE_SUBSCRIPTION_EXISTS_FOR_USER.getErrorMessage());
                            failureList.add(dto);
                        }else{
                            model = subscriptionServiceHelper.getBackendSubscriptionUser(model, dto);
                            saveBackendSubscriptionUser(model, EventEnum.BACKEND_USER_SUBSCRIPTION_CREATION);
                            successList.add(dto);
                        }
                    }else{
                        dto.setMessage(ValidationError.RECORD_ALREADY_EXISTS_AND_NOT_ACTIVATED.getErrorMessage());
                        failureList.add(dto);
                    }
                }
            }
        }
        response = subscriptionServiceHelper.prepareBackendSubscriptionResponse(response, validationResponse.getValidationErrors(), successList, failureList);
        return response;
    }

    @Override
    @Transactional
    public BackendSubscriptionActivationResponse backendSubscriptionActivation(BackendSubscriptionActivationRequest request) {
        BackendSubscriptionActivationResponse response = new BackendSubscriptionActivationResponse();
        ValidationResponse validationResponse = subscriptionValidationService.validatePreBackendSubscriptionActivation(request);
        BackendSubscriptionUserModel backendUser = null;
        if(validationResponse.isValid()){
            backendUser = backendSubscriptionUserRepository.findByMobileAndCompletedFalseAndDeletedFalse(request.getUser().getMobile());
            validationResponse = subscriptionValidationService.validatePostBackendSubscriptionActivation(request, backendUser, validationResponse);
        }
        UserSubscriptionModel userSubscriptionModel = null;
        if(validationResponse.isValid()){
            UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), request.getBusiness(), true, false);
            if(lastUserSubscription==null){
                request.getUser().setFirstName(request.getUser().getFirstName());
                request.getUser().setLastName(request.getUser().getLastName());
                request.getUser().setEmail(StringUtils.isEmpty(request.getUser().getEmail())? backendUser.getEmail(): request.getUser().getEmail());
                SubscriptionVariantModel variantModel = propertyService.getBackendFreeTrialVariant(BusinessEnum.TIMES_PRIME, CountryEnum.IN);
                UserModel userModel = getOrCreateUserWithMobileCheck(request, validationResponse);
                userSubscriptionModel = subscriptionServiceHelper.generateInitPurchaseUserSubscription(request.getUser(), request.getChannel(), request.getPlatform(), PlanTypeEnum.TRIAL, variantModel.getDurationDays(), variantModel, null, userModel, variantModel.getPrice(), false, true);
                EventEnum eventEnum = EventEnum.getEventForBackendActivation(userSubscriptionModel.getPlanStatus());
                userSubscriptionModel = saveUserSubscription(userSubscriptionModel, true, request.getUser().getSsoId(), request.getUser().getTicketId(), eventEnum);
                if (userSubscriptionModel.isOrderCompleted()) {
                    if (PlanStatusEnum.FREE_TRIAL.equals(userSubscriptionModel.getPlanStatus())) {
                        communicationService.sendFreeTrialSubscriptionSuccessCommunication(userSubscriptionModel);
                    } else {
                        communicationService.sendPaidSubscriptionSuccessCommunication(userSubscriptionModel);
                    }
                }
            }else if(lastUserSubscription.getStatus()==StatusEnum.FUTURE){
                validationResponse.addValidationError(ValidationError.FUTURE_SUBSCRIPTION_EXISTS_FOR_USER);
            }else{
                //extend
                lastUserSubscription = subscriptionServiceHelper.extendSubscription(lastUserSubscription, backendUser.getDurationDays());
                userSubscriptionModel = saveUserSubscription(lastUserSubscription, false, request.getUser().getSsoId(), request.getUser().getTicketId(), EventEnum.SUBSCRIPTION_TRIAL_EXTENSION);
                backendUser.setCompleted(true);
                EventEnum eventEnum = EventEnum.getEventForBackendActivation(userSubscriptionModel.getPlanStatus());
                saveBackendSubscriptionUser(backendUser, eventEnum);
                if(userSubscriptionModel.getStatus()==StatusEnum.ACTIVE){
                    updateUserStatus(userSubscriptionModel, userSubscriptionModel.getUser());
                }
                communicationService.sendSubscriptionExpiryExtensionCommunication(userSubscriptionModel);
            }
        }
        response = subscriptionServiceHelper.prepareBackendSubscriptionActivationResponse(response, userSubscriptionModel, validationResponse);
        return response;
    }

    @Override
    @Transactional
    public UserModel getOrCreateUserWithMobileCheck(GenericRequest request, ValidationResponse validationResponse) {
        UserModel userModel = userRepository.findByMobileAndDeletedFalse(request.getUser().getMobile());
        if(userModel==null){
            userModel = subscriptionServiceHelper.getUser(request);
            userModel = saveUserModel(userModel, EventEnum.NORMAL_USER_CREATION);
        }else{
            if(request.getUser().getSsoId().equals(userModel.getSsoId())){
                subscriptionValidationService.validateBlockedUser(userModel, validationResponse);
            }else {
                List<UserSubscriptionModel> relevantUserSubscriptions = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusInAndOrderCompletedTrueAndDeletedFalse(userModel.getMobile(), Arrays.asList(StatusEnum.ACTIVE, StatusEnum.FUTURE));
                userModel.setIsDelete(true);
                userModel = saveUserModel(userModel, EventEnum.USER_SUSPENSION);
                updateUserDetailsInCache(userModel);
                if(StringUtils.isNotEmpty(userModel.getEmail())) {
                    if(CollectionUtils.isNotEmpty(relevantUserSubscriptions)) {
                        communicationService.sendUserSuspensionCommunication(userModel, relevantUserSubscriptions);
                    }
                }
                userModel = subscriptionServiceHelper.getUser(request);
                userModel = saveUserModel(userModel, EventEnum.USER_CREATION_WITH_EXISTING_MOBILE);
                if(CollectionUtils.isNotEmpty(relevantUserSubscriptions)) {
                    for(UserSubscriptionModel model: relevantUserSubscriptions){
                        model.setUser(userModel);
                        saveUserSubscription(model, false, request.getUser().getSsoId(), request.getUser().getTicketId(), EventEnum.USER_SUBSCRIPTION_SWITCH);
                    }
                }
                updateUserDetailsInCache(userModel);
                communicationService.sendUserCreationWithExistingMobileCommunication(userModel, relevantUserSubscriptions);
            }
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
                    userSubscriptionModel.setOrderId(UniqueIdGeneratorUtil.generateOrderId(GlobalConstants.ORDER_ID_LENGTH));
                    continue retryLoop;
                }
                throw e;
            }
        }
        return userSubscriptionModel;
    }


    public void updateUserStatus(UserSubscriptionModel userSubscriptionModel){
        updateUserStatus(userSubscriptionModel, null);
    }

    @Override
    @Transactional
    public void updateUserStatus(UserSubscriptionModel userSubscriptionModel, UserModel userModel){
        SubscriptionStatusDTO statusDTO = getSubscriptionStatusDTO(userSubscriptionModel, userModel);
        if(userSubscriptionModel.getStatus()==StatusEnum.FUTURE){
            return;
        }
        String mobile = userSubscriptionModel.getUser().getMobile();
        cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).put(mobile, statusDTO);
    }

    private void updateUserDetailsInCache(UserModel userModel){
        Cache.ValueWrapper vw = cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).get(userModel.getMobile());
        SubscriptionStatusDTO statusDTO = null;
        if(vw!=null){
            statusDTO = (SubscriptionStatusDTO) vw.get();
        }
        if(statusDTO!=null){
            statusDTO.setBlocked(userModel.isBlocked());
            statusDTO.setEmail(userModel.getEmail());
            cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).put(userModel.getMobile(), statusDTO);
        }
    }

    private SubscriptionStatusDTO getSubscriptionStatusDTO(UserSubscriptionModel userSubscriptionModel, UserModel userModel){
        SubscriptionStatusDTO statusDTO = new SubscriptionStatusDTO();
        if(userSubscriptionModel==null){
            if(userModel==null){
                throw new RuntimeException("User must be present");
            }
            statusDTO.setBlocked(userModel.isBlocked());
            statusDTO.setPlanStatus(userModel.isBlocked()? PlanStatusEnum.BLOCKED.getCode(): PlanStatusEnum.INIT.getCode());
            return statusDTO;
        }
        StatusEnum status = userSubscriptionModel.getStatus();
        statusDTO.setStartDate(userSubscriptionModel.getStartDate());
        statusDTO.setEndDate(userSubscriptionModel.getEndDate());
        statusDTO.setBlocked(userSubscriptionModel.getUser().isBlocked());
        if (status == StatusEnum.EXPIRED) {
            if (userSubscriptionModel.getPlanStatus() == PlanStatusEnum.SUBSCRIPTION || userSubscriptionModel.getPlanStatus() == PlanStatusEnum.SUBSCRIPTION_AUTO_RENEWAL) {
                statusDTO.setPlanStatus(PlanStatusEnum.SUBSCRIPTION_EXPIRED.getCode());
            } else if (userSubscriptionModel.getPlanStatus() == PlanStatusEnum.FREE_TRIAL) {
                statusDTO.setPlanStatus(PlanStatusEnum.FREE_TRAIL_EXPIRED.getCode());
            } else if (userSubscriptionModel.getPlanStatus() == PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT) {
                statusDTO.setPlanStatus(PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT_EXPIRED.getCode());
            }
        } else {
            statusDTO.setPlanStatus(userSubscriptionModel.getPlanStatus().getCode());
        }
        if(userSubscriptionModel.isDeleted()){
            statusDTO.setPlanStatus(PlanStatusEnum.SUBSCRIPTION_CANCELLED.getCode());
        }
        if(statusDTO.isBlocked()){
            statusDTO.setPlanStatus(PlanStatusEnum.BLOCKED.getCode());
        }
        statusDTO.setAutoRenewal(userSubscriptionModel.isAutoRenewal());
        statusDTO.setFirstName(userModel.getFirstName());
        return statusDTO;
    }

    @Override
    @Transactional
    public UserModel saveUserModel(UserModel userModel, EventEnum eventEnum){
        userModel = userRepository.save(userModel);
        UserAuditModel userAuditModel = subscriptionServiceHelper.getUserAudit(userModel, eventEnum);
        userAuditRepository.save(userAuditModel);
        return userModel;
    }

    @Override
    public String updateCacheForMobile(UpdateCacheForMobileRequest request) {
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreUpdateCacheForMobile(request);
        UserSubscriptionModel userSubscriptionModel = null;
        
        if(validationResponse.isValid()){
        	userSubscriptionModel = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusAndDeletedAndOrderCompletedTrue(request.getUser().getMobile(), StatusEnum.ACTIVE, false);
        	validationResponse = subscriptionValidationService.validatePostUpdateCacheForMobile(userSubscriptionModel, validationResponse);
        }
        if(validationResponse.isValid()){
            updateUserStatus(userSubscriptionModel);
            return "SUCCESS";
        }else{
        	return "FAILED";
        }
        
    }
    
    @Override
    public CustomerSearchDTOs customerSearchCRM(CustomerSearchRequest request) {
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreCustomerSearchCRM(request);
        
        if(validationResponse.isValid()){
        	List<CustomerSearchDTO> customerSearchDTOList = new ArrayList<CustomerSearchDTO>();
        	CustomerSearchDTOs customerSearchDTOs = new CustomerSearchDTOs();
        	
        	UserModel userModel = new UserModel();
        	List<UserModel> userModelList = new ArrayList<UserModel>();
        	
            UserDTO user = request.getUser();
            String ssoId = user.getSsoId();
            String email = user.getEmail();
            String mobile = user.getMobile();
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            boolean ssoIdExists = false;
            boolean emailExists = false; 
            boolean mobileExists = false; 
            boolean nameExists = false; 
            
            if(StringUtils.isNotEmpty(ssoId)){
            	ssoIdExists=true;
            }
            
            if(StringUtils.isNotEmpty(email)){
            	emailExists=true;
            }
            
            if(StringUtils.isNotEmpty(mobile)){
            	mobileExists=true;
            }
            
            if(StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)){
            	nameExists=true;
            }
            
            if(ssoIdExists){
            	userModel = userRepository.findBySsoIdAndDeletedFalse(ssoId);
            	userModelList.add(userModel);
            }else if(emailExists){
            	userModel = userRepository.findByEmailAndDeletedFalse(email);
            	userModelList.add(userModel);
            }else if(mobileExists){
            	userModel = userRepository.findByMobileAndDeletedFalse(mobile);
            	userModelList.add(userModel);
            }else if(nameExists){
            	Page<UserModel> page = userRepository.findByFirstNameAndLastNameAndDeletedFalse(firstName, lastName, new PageRequest(request.getPage(), request.getPageSize()));
                if(page!=null){
                    userModelList = page.getContent();
                }
            }

            Iterator itr = userModelList.iterator();
            while(itr.hasNext()){
            	UserModel um = (UserModel)itr.next();
            	if(um!=null){
            		customerSearchDTOList.add(convertUserModelToCustomerSearchDTO(um));	
            	}
            }
            
            customerSearchDTOs.setCustomerSearchDTOs(customerSearchDTOList);
            return customerSearchDTOs;
        }else{
        	return null;
        }
        
    }
    
    private CustomerSearchDTO convertUserSubscriptionModelToCustomerSearchDTO(UserSubscriptionModel userSubscriptionModel){
    	CustomerSearchDTO customerSearchDTO = new CustomerSearchDTO();
    	UserModel user = userSubscriptionModel.getUser();
    	customerSearchDTO.setSsoId(user.getSsoId());
    	customerSearchDTO.setMobile(user.getMobile());
    	customerSearchDTO.setName((user.getFirstName()+" "+user.getLastName()).trim());
    	customerSearchDTO.setEmail(userSubscriptionModel.getUser().getEmail());
    	
    	customerSearchDTO.setExpiryDate(userSubscriptionModel.getEndDate());
    	customerSearchDTO.setDateOfActivation(userSubscriptionModel.getStartDate());
    	customerSearchDTO.setCurrentStatus(userSubscriptionModel.getStatus().name());
    	
    	//customerSearchDTO.setOrderId(userSubscriptionModel.getOrderId());
    	//customerSearchDTO.setOrderAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().toString());

    	return customerSearchDTO;
    }
 
    @Override
    public CustomerCRM customerDetailsCRM(CustomerSearchRequest request) {
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreCustomerDetailsCRM(request);
    	if(validationResponse.isValid()){

        	CustomerCRM customerCRM = new CustomerCRM();
        	
        	
        	UserModel userModel = new UserModel(); 
            UserDTO user = request.getUser();
            String ssoId = user.getSsoId();
            boolean ssoIdExists = false; 

            if(ssoId!=null && !StringUtils.isEmpty(ssoId)){
            	ssoIdExists=true;
            }
            
            if(ssoIdExists){
            	userModel = userRepository.findBySsoIdAndDeletedFalse(ssoId);
            	//userSubscriptionModelList = userSubscriptionRepository.findByUserMobileAndOrderCompletedAndDeleted(userModel.getMobile(), true, false);
            	if(userModel!=null){
            		customerCRM = convertUserToCustomerCRM(userModel);	
            	}
            	return customerCRM;
            	
            }else{
            	return null;
            }

            
            
            
        }else{
        	return null;
        }
        
    }

    private CustomerSearchDTO convertUserModelToCustomerSearchDTO(UserModel userModel){
    	
    	CustomerSearchDTO customerSearchDTO = new CustomerSearchDTO();
    	customerSearchDTO.setSsoId(userModel.getSsoId());
    	customerSearchDTO.setMobile(userModel.getMobile());
        customerSearchDTO.setName((userModel.getFirstName()+" "+userModel.getLastName()).trim());
    	customerSearchDTO.setEmail(userModel.getEmail());

    	
    	
    	if(userModel.getCreated()!=null){
        	customerSearchDTO.setDateOfActivation(userModel.getCreated());
    	}
    	
    	UserSubscriptionModel userSubscriptionModel = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusAndUserDeleted(userModel.getMobile(), StatusEnum.ACTIVE, false);
    	if(userSubscriptionModel!=null){
        	customerSearchDTO.setCurrentStatus(userSubscriptionModel.getStatus().name());
        	if(userSubscriptionModel.getEndDate()!=null){
        		customerSearchDTO.setExpiryDate(userSubscriptionModel.getEndDate());
        	}
    	}else{
    		
    		Page<UserSubscriptionModel> userSubscriptionModels = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusAndUserDeletedOrderByEndDateDesc(userModel.getMobile(), StatusEnum.EXPIRED, false, new PageRequest(0,1));
    		List<UserSubscriptionModel> userSubscriptionModelList = userSubscriptionModels.getContent();
            userSubscriptionModel = userSubscriptionModelList.get(0);
            if(userSubscriptionModel!=null){
            	customerSearchDTO.setCurrentStatus(userSubscriptionModel.getStatus().name());
            	if(userSubscriptionModel.getEndDate()!=null){
            		customerSearchDTO.setExpiryDate(userSubscriptionModel.getEndDate());
            	}
        	}
    	}
    	
    	//customerSearchDTO.setOrderId(userSubscriptionModel.getOrderId());
    	//customerSearchDTO.setOrderAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().toString());

    	return customerSearchDTO;
    }
    
    
    private CustomerCRM convertUserToCustomerCRM(UserModel userModel){
    	
    	CustomerCRM customerCRM = new CustomerCRM();
    	OrderSearchResultsCRM orderSearchResultsCRM = new OrderSearchResultsCRM();
    	OrderSearchResultsCRM autoDebitOrders = new OrderSearchResultsCRM();
    	List<OrderSearchResultCRM> orderSearchResultCRMList=new ArrayList<OrderSearchResultCRM>();
    	List<OrderSearchResultCRM> autoDebitOrdersList=new ArrayList<OrderSearchResultCRM>();
    	
    	boolean activeStatus = false;
    	
    	customerCRM.setSsoId(userModel.getSsoId());
    	customerCRM.setMobileNumber(userModel.getMobile());
    	customerCRM.setName((userModel.getFirstName()+" "+userModel.getLastName()).trim());
    	customerCRM.setEmailId(userModel.getEmail());
    	if(userModel.getCreated()!=null){
    		customerCRM.setActivationDate(userModel.getCreated());
    	}
    	
    	if(userModel.isBlocked()){
    		customerCRM.setBlockedStatus(true);	
    		//customerCRM.setBlockedDate(userModel.getBlockedDate());
    		if(userModel.getUpdated()!=null){
        		customerCRM.setBlockedDate(userModel.getUpdated());    			
    		}
    	}else{
    		customerCRM.setBlockedStatus(false);	
    	}
    	
    	List<UserSubscriptionModel> userSubscriptionModelList = userModel.getUserSubscriptions();
    	for(UserSubscriptionModel userSubscriptionModel: userSubscriptionModelList){
    		if(userSubscriptionModel.isOrderCompleted() && !userSubscriptionModel.isDeleted()){
	        	OrderSearchResultCRM orderSearchResultCRM = new OrderSearchResultCRM();
	        	
	            
	            if(userSubscriptionModel.getSubscriptionVariant()!=null && userSubscriptionModel.getSubscriptionVariant().getPrice()!=null){
	                orderSearchResultCRM.setAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().doubleValue());            	
	            }
	
	            if(userSubscriptionModel.getStatus()!=null){
	                orderSearchResultCRM.setCurrentStatus(userSubscriptionModel.getStatus().toString());
	            }
	
	            if(userSubscriptionModel.getEndDate()!=null){
	                orderSearchResultCRM.setExpiryDate(userSubscriptionModel.getEndDate());            	
	            }
	
	            if(userSubscriptionModel.getCreated()!=null){
	                orderSearchResultCRM.setOrderDate(userSubscriptionModel.getCreated());            	
	            }
	
	            orderSearchResultCRM.setOrderDetail("");
	            orderSearchResultCRM.setOrderId(userSubscriptionModel.getOrderId());
	            
	            if(userSubscriptionModel.getStatus()==StatusEnum.ACTIVE){
	            	customerCRM.setActiveSubscriptionExists(true);
	            	customerCRM.setSubscriptionStatus(StatusEnum.ACTIVE.toString());
	            	OrderDetailsCRM activeSubscriptionDetails = new OrderDetailsCRM();
	            	activeSubscriptionDetails.setBusiness(userSubscriptionModel.getBusiness().toString());
	            	activeSubscriptionDetails.setChannel(userSubscriptionModel.getChannel().toString());
	            	activeSubscriptionDetails.setPlatform(userSubscriptionModel.getPlatform().toString());
	            	activeSubscriptionDetails.setUserSubscriptionId(userSubscriptionModel.getId().toString());
	            	activeSubscriptionDetails.setOrderId(userSubscriptionModel.getOrderId());
	            	
	            	if(userSubscriptionModel.getSubscriptionVariant()!=null){
		            	activeSubscriptionDetails.setPlanDurationDays(userSubscriptionModel.getSubscriptionVariant().getDurationDays().toString());
		            	activeSubscriptionDetails.setVariantId(userSubscriptionModel.getSubscriptionVariant().getId().toString());
		            	activeSubscriptionDetails.setSubscriptionPlan(userSubscriptionModel.getSubscriptionVariant().getSubscriptionPlan().getName());
		            	activeSubscriptionDetails.setPlanType(userSubscriptionModel.getSubscriptionVariant().getPlanType().toString());
		            	activeSubscriptionDetails.setPlanID(userSubscriptionModel.getSubscriptionVariant().getSubscriptionPlan().getId().toString());
		            	activeSubscriptionDetails.setPlanPrice(userSubscriptionModel.getSubscriptionVariant().getPrice().toString());
	            	}
	            	
	            	customerCRM.setActiveSubscriptionDetails(activeSubscriptionDetails);
	            	if(userSubscriptionModel.getEndDate()!=null){
	            		customerCRM.setExpiryDate(userSubscriptionModel.getEndDate());
	            		activeStatus = true;
	            	}
	                if(userSubscriptionModel.isAutoRenewal()){
	            		customerCRM.setAutoRenewalStatus(true);
	            		autoDebitOrdersList.add(orderSearchResultCRM);
	    	            orderSearchResultCRM.setAutoRenewal(true);
	            		customerCRM.setRenewalMode(userSubscriptionModel.getPaymentMethod());
	                }else{
	                	customerCRM.setAutoRenewalStatus(false);
	                }
	            }
	            
	            if(userSubscriptionModel.getStatus()==StatusEnum.FUTURE){
	            	customerCRM.setFutureSubscriptionExists(true);
	                if(userSubscriptionModel.isAutoRenewal()){
	            		customerCRM.setAutoRenewalStatus(true);
	            		autoDebitOrdersList.add(orderSearchResultCRM);
	    	            orderSearchResultCRM.setAutoRenewal(true);;
	            		customerCRM.setRenewalMode(userSubscriptionModel.getPaymentMethod());
	                }else{
	                	customerCRM.setAutoRenewalStatus(false);
	                }
	            }
	            
	            orderSearchResultCRMList.add(orderSearchResultCRM);    			
    		}
        }
    	
    	if(!activeStatus){
    		
    		Page<UserSubscriptionModel> userSubscriptionModels = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusAndUserDeletedOrderByEndDateDesc(userModel.getMobile(), StatusEnum.EXPIRED, false, new PageRequest(0,1));
    		List<UserSubscriptionModel> userSubscriptionModelSubList = userSubscriptionModels.getContent();
    		UserSubscriptionModel userSubscriptionModel = userSubscriptionModelSubList.get(0);
            if(userSubscriptionModel!=null){
            	customerCRM.setSubscriptionStatus(StatusEnum.EXPIRED.toString());
            	if(userSubscriptionModel.getEndDate()!=null){
            		customerCRM.setExpiryDate(userSubscriptionModel.getEndDate());
            	}
        	}
    	}
    	
    	
    	orderSearchResultsCRM.setOrderSearchResultsCRM(orderSearchResultCRMList);
    	customerCRM.setOrderSearchResultsCRM(orderSearchResultsCRM);

    	autoDebitOrders.setOrderSearchResultsCRM(autoDebitOrdersList);
    	customerCRM.setAutoDebitOrders(autoDebitOrders);
    	
    	//customerSearchDTO.setOrderId(userSubscriptionModel.getOrderId());
    	//customerSearchDTO.setOrderAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().toString());

    	return customerCRM;
    }
    
    
    @Override
    public OrderDetailsCRM getOrderDetailsCRM(OrderDetailsRequest request) {
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreOrderDetailsCRM(request);
    	if(validationResponse.isValid()){

    		OrderDetailsCRM orderDetailsCRM = new OrderDetailsCRM();
        	
        	
    		String orderId = request.getOrderId();

    			UserSubscriptionModel userSubscriptionModel = userSubscriptionRepository.findByOrderIdAndDeleted(orderId, false);
            	//userSubscriptionModelList = userSubscriptionRepository.findByUserMobileAndOrderCompletedAndDeleted(userModel.getMobile(), true, false);
            	if(userSubscriptionModel!=null){
            		orderDetailsCRM = convertUserSubscriptionModelToOrderDetailsCRM(userSubscriptionModel);
            	}
                return orderDetailsCRM;
            
        }else{
        	return null;
        }
        
    }
    
    private OrderDetailsCRM convertUserSubscriptionModelToOrderDetailsCRM(UserSubscriptionModel userSubscriptionModel){
    	OrderDetailsCRM orderDetailsCRM = new OrderDetailsCRM();
    	try{
    		UserModel user = userSubscriptionModel.getUser();
    		orderDetailsCRM.setOrderId(userSubscriptionModel.getOrderId());

    		orderDetailsCRM.setUserSubscriptionId(userSubscriptionModel.getId().toString());
    		if(user!=null){
        		orderDetailsCRM.setSsoId(user.getSsoId());
        		orderDetailsCRM.setName((user.getFirstName()+" "+user.getLastName()).trim());
        		orderDetailsCRM.setMobileNumber(user.getMobile());
        		orderDetailsCRM.setEmailId(user.getEmail());
    		}
        	if(userSubscriptionModel.getSubscriptionVariant()!=null){
            	orderDetailsCRM.setSubscriptionPlan(userSubscriptionModel.getSubscriptionVariant().getDurationDays().toString() + " DAY " + userSubscriptionModel.getSubscriptionVariant().getPlanType().toString());
            	if(userSubscriptionModel.getSubscriptionVariant().getPrice()!=null){
                	orderDetailsCRM.setAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().doubleValue());            		
                	orderDetailsCRM.setBilledAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().toString());
                	orderDetailsCRM.setRenewalAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().toString());
            	}
            	
            	if(userSubscriptionModel.getSubscriptionVariant().getId()!=null){
                	orderDetailsCRM.setVariantId(userSubscriptionModel.getSubscriptionVariant().getId().toString());
            	}
        	}
        	if(userSubscriptionModel.getCreated()!=null){
        		orderDetailsCRM.setOrderDate(userSubscriptionModel.getCreated());	
        	}
        	if(userSubscriptionModel.getEndDate()!=null){
            	orderDetailsCRM.setRenewalDate(userSubscriptionModel.getEndDate());
            	orderDetailsCRM.setExpiryDate(userSubscriptionModel.getEndDate());
        	}

        	if(userSubscriptionModel.isAutoRenewal()){
            	orderDetailsCRM.setAutoRenewal(true);
            }else{
            	orderDetailsCRM.setAutoRenewal(false);
            }        	
            
            Date date = new Date();
            if(userSubscriptionModel.getStatus()==StatusEnum.ACTIVE &&
            		userSubscriptionModel.getEndDate().after(date)){
                orderDetailsCRM.setCurrentStatus("SUBSCRIBED");
            }else if(userSubscriptionModel.getStatus()==StatusEnum.FUTURE){
                orderDetailsCRM.setCurrentStatus("FUTURE SUBSCRIPTION");
            }else{
            	 orderDetailsCRM.setCurrentStatus("EXPIRED");
            }
            
            return orderDetailsCRM;

    	}catch(Exception e){
            LOG.error("Exception in convertUserSubscriptionModelToOrderDetailsCRM: ", e);
            return null;
    	}
    	
    }
    
    @Override
    public OrderSearchResultsCRM orderSearchCRM(OrderSearchRequest request) {
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreOrderSearchCRM(request);
    	if(validationResponse.isValid()){

    		OrderSearchResultsCRM orderSearchResultsCRM = new OrderSearchResultsCRM();
    		List<UserSubscriptionModel> userSubscriptionModelList = null;
        	
        	
    		String subscriptionStatus = request.getSubscriptionStatus();
            String orderId= request.getOrderId();
            Date fromDate = request.getFromDate();
            Date toDate = request.getToDate();

            

            if(orderId==null && subscriptionStatus==null){
    			userSubscriptionModelList = userSubscriptionRepository.findByCreatedBetweenAndDeletedFalseAndOrderCompletedTrue(fromDate, toDate);	

            }
                        
            if(subscriptionStatus!=null){
        		if(subscriptionStatus.equalsIgnoreCase(StatusEnum.ACTIVE.toString())){
        			if(fromDate!=null || toDate!=null){
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndCreatedBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum.ACTIVE, fromDate!=null? fromDate: new Date(), toDate!=null? toDate: new Date());
        			}else{
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndDeletedFalseAndOrderCompletedTrue(StatusEnum.ACTIVE);	
        			}
        		}else if(subscriptionStatus.equalsIgnoreCase(StatusEnum.FUTURE.toString())){
        			if(fromDate!=null || toDate!=null){
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndCreatedBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum.FUTURE, fromDate!=null? fromDate: new Date(), toDate!=null? toDate: new Date());
        			}else{
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndDeletedFalseAndOrderCompletedTrue(StatusEnum.FUTURE);	
        			}
        		}else if(subscriptionStatus.equalsIgnoreCase(StatusEnum.EXPIRED.toString())){
        			if(fromDate!=null || toDate!=null){
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndCreatedBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum.EXPIRED, fromDate!=null? fromDate: new Date(), toDate!=null? toDate: new Date());
            		}else{
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndDeletedFalseAndOrderCompletedTrue(StatusEnum.EXPIRED);        			            			
            		}
        		}

            }
            
            if(orderId!=null){
    			userSubscriptionModelList = userSubscriptionRepository.findByOrderIdAndDeletedFalseAndOrderCompletedTrue(orderId);	
            }

    		List<OrderSearchResultCRM> orderSearchResultCRMList = new ArrayList<OrderSearchResultCRM>(); 
            //userSubscriptionModelList = userSubscriptionRepository.findByUserMobileAndOrderCompletedAndDeleted(userModel.getMobile(), true, false);
            for(UserSubscriptionModel userSubscriptionModel: userSubscriptionModelList){
            	OrderSearchResultCRM orderSearchResultCRM = convertUserSubscriptionModelToOrderSearchCRM(userSubscriptionModel);
            	orderSearchResultCRMList.add(orderSearchResultCRM);
            }
            orderSearchResultsCRM.setOrderSearchResultsCRM(orderSearchResultCRMList);
            
            return orderSearchResultsCRM;
            
        }else{
        	return null;
        }
        
    }
    
    
    private OrderSearchResultCRM convertUserSubscriptionModelToOrderSearchCRM(UserSubscriptionModel userSubscriptionModel){
    	OrderSearchResultCRM orderSearchResultCRM = new OrderSearchResultCRM();
    	try{
    		
    		orderSearchResultCRM.setAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().doubleValue());
    		orderSearchResultCRM.setCurrentStatus(userSubscriptionModel.getStatus().toString());
    		orderSearchResultCRM.setExpiryDate(userSubscriptionModel.getEndDate());
    		orderSearchResultCRM.setOrderDate(userSubscriptionModel.getCreated());
    		orderSearchResultCRM.setOrderDetail("");
    		orderSearchResultCRM.setOrderId(userSubscriptionModel.getOrderId());
    		
            
            return orderSearchResultCRM;

    	}catch(Exception e){
            LOG.error("Exception in convertUserSubscriptionModelToOrderDetailsCRM: ", e);
            return null;
    	}
    	
    }
    
    @Override
    @Transactional
    public BackendSubscriptionUserModel saveBackendSubscriptionUser(BackendSubscriptionUserModel user, EventEnum eventEnum){
        int retryCount = GlobalConstants.DB_RETRY_COUNT;
        retryLoop:
        while (retryCount > 0) {
            try {
                user = backendSubscriptionUserRepository.save(user);
                BackendSubscriptionUserAuditModel userAudit = subscriptionServiceHelper.getBackendSubscriptionUserAudit(user, eventEnum);
                backendSubscriptionUserAuditRepository.save(userAudit);
                break retryLoop;
            } catch (Exception e) {
                retryCount--;
                if(retryCount>0){
                    user.setCode(UniqueIdGeneratorUtil.generateCode(user.getMobile(), GlobalConstants.BACKEND_ACTIVATION_CODE_LENGTH));
                    String params = new StringBuilder("mobile=").append(user.getMobile()).append("&code=").append(user.getCode()).toString();
                    String encodedParams = new String(Base64.encodeBase64(params.getBytes(Charset.forName(GlobalConstants.UTF_8))));
                    StringBuilder url = new StringBuilder(properties.getProperty(GlobalConstants.PRIME_BACKEND_ACTIVATION_URL_KEY)).append("?q=").append(encodedParams);
                    String shortenedUrl = subscriptionServiceHelper.shortenUrl(url.toString());
                    user.setShortenedUrl(shortenedUrl);
                    continue retryLoop;
                }
                throw e;
            }
        }
        if(eventEnum == EventEnum.BACKEND_USER_SUBSCRIPTION_CREATION){
            communicationService.sendBackendActivationPendingCommunication(user);
        }
        return user;
    }
}
