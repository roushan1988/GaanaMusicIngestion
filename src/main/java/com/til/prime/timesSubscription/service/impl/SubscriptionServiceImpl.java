package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.constants.RedisConstants;
import com.til.prime.timesSubscription.convertor.ModelToDTOConvertorUtil;
import com.til.prime.timesSubscription.dao.*;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.dto.internal.OtpStatus;
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
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public PlanListResponse getAllPlans(PlanListRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreAllPlans(request);
        PlanListResponse response = new PlanListResponse();
        List<SubscriptionPlanDTO> subscriptionPlans = null;
        if (validationResponse.isValid() && request.getUser() != null) {
            UserModel userModel = userRepository.findByMobileAndDeletedFalse(request.getUser().getMobile());
            validationResponse = subscriptionValidationService.validatePostAllPlans(userModel, validationResponse);
        }
        if (validationResponse.isValid()) {
            if (request.getUser() != null) {
                UserModel userModel = getOrCreateUserWithMobileCheck(request, validationResponse);
                if(validationResponse.isValid()){
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
                            if (flag) {
                                Collections.sort(subscriptionPlanModel.getVariants());
                            }
                            UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), request.getBusiness(), true, false);
                            subscriptionPlans.add(ModelToDTOConvertorUtil.getSubscriptionPlanDTO(subscriptionPlanModel, lastUserSubscription));
                        }
                    }
                }
            } else {
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
        if (validationResponse.isValid()) {
            planType = PlanTypeEnum.valueOf(request.getPlanType());
            business = BusinessEnum.valueOf(request.getBusiness());
            subscriptionVariantModel = subscriptionVariantRepository.findByIdAndSubscriptionPlanIdAndPriceAndDurationDaysAndDeleted(
                    request.getVariantId(), request.getPlanId(), request.getPrice(), request.getDurationDays(), false);
            if (PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planType)) {
                restrictedUsageUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getMobile(), business, planType, true, false);
            }
            lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndStatusInAndOrderCompletedTrueAndDeletedFalseOrderByIdDesc(request.getUser().getMobile(), business, StatusEnum.VALID_INIT_STATUS_SET);
            validationResponse = subscriptionValidationService.validatePostInitPurchasePlan(request, subscriptionVariantModel, restrictedUsageUserSubscription, lastUserSubscription, crmRequest, validationResponse);
        }
        if (validationResponse.isValid()) {
            UserModel userModel = getOrCreateUserWithMobileCheck(request, validationResponse);
            if (validationResponse.isValid()) {
                userSubscriptionModel = subscriptionServiceHelper.generateInitPurchaseUserSubscription(request, subscriptionVariantModel, lastUserSubscription, userModel, request.getPrice(), crmRequest, isFree);
                EventEnum eventEnum = EventEnum.getEventByInitPlanStatus(userSubscriptionModel.getPlanStatus());
                userSubscriptionModel = saveUserSubscription(userSubscriptionModel, true, eventEnum, userSubscriptionModel.isOrderCompleted(), userSubscriptionModel.isOrderCompleted());
                if (userSubscriptionModel.isOrderCompleted()) {
                    if (PlanStatusEnum.FREE_TRIAL.equals(userSubscriptionModel.getPlanStatus())) {
                        communicationService.sendFreeTrialSubscriptionSuccessCommunication(userSubscriptionModel);
                    } else {
                        communicationService.sendPaidSubscriptionSuccessCommunication(userSubscriptionModel);
                    }
                } else {
                    if (crmRequest) {
                        boolean success = subscriptionServiceHelper.renewSubscription(userSubscriptionModel);
                        if (success) {
                            UserSubscriptionModel userSubscriptionModel2 = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrueOrderById(
                                    userSubscriptionModel.getUser().getMobile(), StatusEnum.ACTIVE, TimeUtils.addMillisInDate(userSubscriptionModel.getEndDate(), -2000));
                            if (userSubscriptionModel2 != null) {
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
        if (validationResponse.isValid()) {
            planType = PlanTypeEnum.valueOf(request.getPlanType());
            business = BusinessEnum.valueOf(request.getBusiness());
            userSubscriptionModel = userSubscriptionRepository.findByIdAndDeleted(request.getUserSubscriptionId(), false);
            if (userSubscriptionModel != null) {
                subscriptionVariantModel = userSubscriptionModel.getSubscriptionVariant();
            }
            if (PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planType)) {
                restrictedUsageUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getMobile(), business, planType, true, false);
            }
            validationResponse = subscriptionValidationService.validatePostGenerateOrder(request, subscriptionVariantModel, userSubscriptionModel, restrictedUsageUserSubscription, validationResponse);
        }
        if (validationResponse.isValid()) {
            if (userSubscriptionModel != null && StringUtils.isNotEmpty(userSubscriptionModel.getOrderId())) {
                request.setDuplicate(true);
            }
            if (request.isRetryOnFailure() || request.isRenewal() || request.isDuplicate()) {
                useNewSubscription = true;
                if (request.isRenewal()) {
                    lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), business, true, false);
                    newUserSubscriptionModel = new UserSubscriptionModel(lastUserSubscription, request, request.isRenewal());
                } else {
                    newUserSubscriptionModel = new UserSubscriptionModel(userSubscriptionModel, request, request.isRenewal());
                }
            }
            if (useNewSubscription) {
                newUserSubscriptionModel = subscriptionServiceHelper.updateGenerateOrderUserSubscription(request, newUserSubscriptionModel);
                newUserSubscriptionModel = saveUserSubscription(newUserSubscriptionModel, true, EventEnum.ORDER_ID_GENERATION, false, false);

            } else {
                userSubscriptionModel = subscriptionServiceHelper.updateGenerateOrderUserSubscription(request, userSubscriptionModel);
                userSubscriptionModel = saveUserSubscription(userSubscriptionModel, true, EventEnum.ORDER_ID_GENERATION, false, false);
            }
        }
        response = subscriptionServiceHelper.prepareGenerateOrderResponse(response, useNewSubscription ? newUserSubscriptionModel : userSubscriptionModel, validationResponse);
        return response;
    }

    @Override
    @Transactional
    public SubmitPurchaseResponse submitPurchasePlan(SubmitPurchaseRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreSubmitPurchasePlan(request);
        UserSubscriptionModel userSubscriptionModel = null;
        SubmitPurchaseResponse response = new SubmitPurchaseResponse();
        if (validationResponse.isValid()) {
            userSubscriptionModel = userSubscriptionRepository.findByOrderIdAndSubscriptionVariantIdAndDeleted(request.getOrderId(), request.getVariantId(), false);
            UserSubscriptionModel conflictingUserSubscription = null;
            if(userSubscriptionModel!=null) {
                conflictingUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndDeletedFalseAndOrderCompletedTrueAndEndDateAfterAndStatusIn(userSubscriptionModel.getUser().getMobile(), userSubscriptionModel.getStartDate(), StatusEnum.VALID_INIT_STATUS_SET);
            }
            validationResponse = subscriptionValidationService.validatePostSubmitPurchasePlan(request, userSubscriptionModel, conflictingUserSubscription, validationResponse);
        }
        if (validationResponse.isValid()) {
            UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndStatusInAndOrderCompletedTrueAndDeletedFalseOrderByIdDesc(userSubscriptionModel.getUser().getMobile(), userSubscriptionModel.getBusiness(), StatusEnum.VALID_INIT_STATUS_SET);
            userSubscriptionModel = subscriptionServiceHelper.updateSubmitPurchaseUserSubscription(request, userSubscriptionModel, lastUserSubscription);
            userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, userSubscriptionModel.isOrderCompleted() ? EventEnum.PAYMENT_SUCCESS : EventEnum.PAYMENT_FAILURE, true, true);
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
        if (validationResponse.isValid()) {
            if (!request.isCurrentSubscription()) {
                if (StringUtils.isEmpty(request.getBusiness())) {
                    userSubscriptionModelList = request.isIncludeDeleted() ?
                            userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndOrderCompleted(request.getUser().getMobile(), true) :
                            userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndOrderCompletedAndDeleted(request.getUser().getMobile(), true, false);
                } else {
                    userSubscriptionModelList = request.isIncludeDeleted() ?
                            userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompleted(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true) :
                            userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeleted(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true, false);
                }
            } else {
                Date currentDate = new Date();
                UserSubscriptionModel userSubscriptionModel = new UserSubscriptionModel();
                if (StringUtils.isEmpty(request.getBusiness())) {
                    userSubscriptionModel = request.isIncludeDeleted() ?
                            userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndOrderCompletedAndStartDateBeforeAndEndDateAfter(request.getUser().getMobile(), true, currentDate, currentDate) :
                            userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(request.getUser().getMobile(), true, currentDate, currentDate, false);
                } else {
                    userSubscriptionModel = request.isIncludeDeleted() ?
                            userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfter(request.getUser().getMobile(), BusinessEnum.valueOf(request.getBusiness()), true, currentDate, currentDate) :
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
    public CancelSubscriptionResponse cancelSubscription(CancelSubscriptionRequest request, boolean serverRequest) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreCancelSubscription(request, serverRequest);
        UserSubscriptionModel userSubscriptionModel = null;
        UserSubscriptionModel lastRelevantSubscription = null;
        CancelSubscriptionResponse response = new CancelSubscriptionResponse();
        if (validationResponse.isValid()) {
            userSubscriptionModel = userSubscriptionRepository.findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(request.getUserSubscriptionId(), request.getOrderId(), request.getVariantId(), false);
            if(userSubscriptionModel!=null) {
                lastRelevantSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndStatusInAndOrderCompletedTrueAndDeletedFalseOrderByIdDesc(userSubscriptionModel.getUser().getMobile(), userSubscriptionModel.getBusiness(), StatusEnum.VALID_CANCEL_STATUS_SET);
            }
            validationResponse = subscriptionValidationService.validatePostCancelSubscription(request, userSubscriptionModel, lastRelevantSubscription, validationResponse);
        }
        BigDecimal refundedAmount = null;
        if (validationResponse.isValid()) {
            BigDecimal refundAmount = null;
            boolean forceAmount = false;
            if (serverRequest && (((CancelSubscriptionServerRequest) request).isRefund())) {
                if (((CancelSubscriptionServerRequest) request).getRefundAmount() != null && ((CancelSubscriptionServerRequest) request).getRefundAmount() > 0) {
                    refundAmount = new BigDecimal(((CancelSubscriptionServerRequest) request).getRefundAmount());
                    forceAmount = true;
                } else {
                    refundAmount = subscriptionServiceHelper.calculateRefundAmount(userSubscriptionModel);
                }
            } else {
                refundAmount = BigDecimal.ZERO;
            }
            RefundInternalResponse refundResponse = subscriptionServiceHelper.refundPayment(userSubscriptionModel.getOrderId(), refundAmount.doubleValue(), forceAmount);
            if (refundResponse.isSuccess()) {
                StatusEnum previousStatus = userSubscriptionModel.getStatus();
                if (previousStatus == StatusEnum.ACTIVE) {
                    userSubscriptionModel.setStatus(StatusEnum.ACTIVE_CANCELLED);
                    userSubscriptionModel.setEndDate(new Date());
                } else {
                    userSubscriptionModel.setStatus(StatusEnum.CANCELLED);
                }
                userSubscriptionModel.setStatusDate(new Date());
                userSubscriptionModel.setPlanStatus(PlanStatusEnum.getPlanStatus(userSubscriptionModel.getStatus(), userSubscriptionModel.getSubscriptionVariant().getPlanType(), userSubscriptionModel.getSubscriptionVariant().getPrice(), null,  false));
                userSubscriptionModel.setSsoCommunicated(false);
                userSubscriptionModel.setStatusPublished(false);
                if (refundResponse.getRefundedAmount() != null) {
                    refundedAmount = new BigDecimal(refundResponse.getRefundedAmount());
                    refundedAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                }
                userSubscriptionModel.setRefundedAmount(refundedAmount);
                EventEnum eventEnum = (serverRequest ? EventEnum.SUBSCRIPTION_SERVER_CANCELLATION : EventEnum.SUBSCRIPTION_APP_CANCELLATION);
                if(previousStatus==StatusEnum.ACTIVE && eventEnum==EventEnum.SUBSCRIPTION_SERVER_CANCELLATION){
                    eventEnum = EventEnum.SUBSCRIPTION_SERVER_ACTIVE_CANCELLATION;
                }
                if(previousStatus==StatusEnum.ACTIVE && eventEnum==EventEnum.SUBSCRIPTION_APP_CANCELLATION){
                    eventEnum = EventEnum.SUBSCRIPTION_APP_ACTIVE_CANCELLATION;
                }
                userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, eventEnum, true, true);
                communicationService.sendSubscriptionCancellationCommunication(userSubscriptionModel);
            } else {
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
        if (validationResponse.isValid()) {
            userSubscriptionModelList = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusInAndOrderCompletedTrueAndAutoRenewalTrueAndDeletedFalse(request.getUser().getMobile(), StatusEnum.VALID_TURN_OFF_DEBIT_STATUS_SET);
            validationResponse = subscriptionValidationService.validatePostTurnOffAutoDebit(request, userSubscriptionModelList, validationResponse);
        }
        if (validationResponse.isValid()) {
            for (UserSubscriptionModel userSubscriptionModel : userSubscriptionModelList) {
                userSubscriptionModel.setAutoRenewal(false);
                userSubscriptionModel.setStatusPublished(false);
                userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, EventEnum.SUBSCRIPTION_TURN_OFF_AUTO_DEBIT, true, !userSubscriptionModel.isSsoCommunicated());
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
        if (validationResponse.isValid()) {
            userModel = userRepository.findByMobileAndDeletedFalse(request.getUser().getMobile());
            validationResponse = subscriptionValidationService.validatePostBlockUnblockUser(request, userModel, validationResponse);
        }
        if (validationResponse.isValid()) {
            userModel.setBlocked(request.isBlockUser());
            userModel = saveUserModel(userModel, request.isBlockUser() ? EventEnum.USER_BLOCK : EventEnum.USER_UNBLOCK, false);
            clearUserStatusCache(userModel.getMobile());
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
        if (validationResponse.isValid()) {
            userSubscriptionModel = userSubscriptionRepository.findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(request.getUserSubscriptionId(), request.getOrderId(), request.getVariantId(), false);
            lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndStatusInAndOrderCompletedTrueAndDeletedFalseOrderByIdDesc(request.getUser().getMobile(), BusinessEnum.TIMES_PRIME, StatusEnum.VALID_USABLE_SET);
            validationResponse = subscriptionValidationService.validatePostExtendExpiry(request, userSubscriptionModel, lastUserSubscription, validationResponse);
        }
        if (validationResponse.isValid()) {
            userSubscriptionModel = subscriptionServiceHelper.extendSubscription(userSubscriptionModel, request.getExtensionDays());
            userSubscriptionModel = saveUserSubscription(userSubscriptionModel, false, EventEnum.SUBSCRIPTION_TRIAL_EXTENSION, true, !userSubscriptionModel.isSsoCommunicated());
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
        if (validationResponse.isValid()) {
            planType = PlanTypeEnum.valueOf(request.getPlanType());
            business = BusinessEnum.valueOf(request.getBusiness());
            subscriptionVariantModel = subscriptionVariantRepository.findByIdAndSubscriptionPlanIdAndDeleted(request.getVariantId(), request.getPlanId(), false);
            if (PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planType)) {
                restrictedUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(
                        request.getUser().getMobile(), business, planType, true, false);
            }
            lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndStatusInAndOrderCompletedTrueAndDeletedFalseOrderByIdDesc(request.getUser().getMobile(), business, StatusEnum.VALID_INIT_STATUS_SET);
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
        if (validationResponse.isValid()) {
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
        if (validationResponse.isValid()) {
            try {
                statusDTO = getUserStatusCacheWithUpdateByMobile(request.getUser().getMobile());
            }catch (Exception e){
                validationResponse.addValidationError(ValidationError.valueOf(e.getMessage()));
            }
        }
        response = subscriptionServiceHelper.prepareCheckStatusResponse(response, false, statusDTO, validationResponse);
        return response;
    }

    @Override
    public CheckStatusResponse checkStatusViaServer(CheckStatusRequest request, boolean external) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreCheckStatusViaServer(request, external);
        CheckStatusResponse response = new CheckStatusResponse();
        SubscriptionStatusDTO statusDTO = null;
        if (validationResponse.isValid()) {
            try {
                statusDTO = getUserStatusCacheWithUpdateByMobile(request.getUser().getMobile());
            }catch (Exception e){
                validationResponse.addValidationError(ValidationError.valueOf(e.getMessage()));
            }
        }
        response = subscriptionServiceHelper.prepareCheckStatusResponse(response, external, statusDTO, validationResponse);
        return response;
    }

    @Override
    public BackendSubscriptionResponse backendSubscriptionViaServer(BackendSubscriptionRequest request) {
        BackendSubscriptionResponse response = new BackendSubscriptionResponse();
        BackendSubscriptionValidationResponse validationResponse = subscriptionValidationService.validateBackendSubscriptionRequest(request);
        List<BackendActivationUserDTO> successList = new ArrayList<>();
        List<BackendActivationUserDTO> failureList = new ArrayList<>();
        if (validationResponse.getValidationErrors().isEmpty()) {
            failureList.addAll(validationResponse.getFailureList());
            request.getUserList().removeAll(failureList);
            if (CollectionUtils.isNotEmpty(request.getUserList())) {
                for (BackendActivationUserDTO dto : request.getUserList()) {
                    BackendSubscriptionUserModel model = backendSubscriptionUserRepository.findByMobileAndDeletedFalse(dto.getMobile());
                    if (model == null || (model != null && model.isCompleted() && dto.isForceActivation())) {
                        UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(dto.getMobile(), BusinessEnum.TIMES_PRIME, true, false);
                        if (lastUserSubscription != null && !StatusEnum.INVALID_BACKEND_SUBSCRIPTION_STATUS_SET.contains(lastUserSubscription.getStatus())) {
                            dto.setMessage(ValidationError.FUTURE_SUBSCRIPTION_EXISTS_FOR_USER.getErrorMessage());
                            failureList.add(dto);
                        } else {
                            model = subscriptionServiceHelper.getBackendSubscriptionUser(model, dto);
                            saveBackendSubscriptionUser(model, EventEnum.BACKEND_USER_SUBSCRIPTION_CREATION);
                            successList.add(dto);
                        }
                    } else {
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
        if (validationResponse.isValid()) {
            backendUser = backendSubscriptionUserRepository.findByMobileAndCompletedFalseAndDeletedFalse(request.getUser().getMobile());
            validationResponse = subscriptionValidationService.validatePostBackendSubscriptionActivation(request, backendUser, validationResponse);
        }
        UserSubscriptionModel userSubscriptionModel = null;
        if (validationResponse.isValid()) {
            UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(request.getUser().getMobile(), request.getBusiness(), true, false);
            if (lastUserSubscription == null) {
                request.getUser().setFirstName(request.getUser().getFirstName());
                request.getUser().setLastName(request.getUser().getLastName());
                request.getUser().setEmail(StringUtils.isEmpty(request.getUser().getEmail()) ? backendUser.getEmail() : request.getUser().getEmail());
                SubscriptionVariantModel variantModel = propertyService.getBackendFreeTrialVariant(BusinessEnum.TIMES_PRIME, CountryEnum.IN);
                UserModel userModel = getOrCreateUserWithMobileCheck(request, validationResponse);
                if(validationResponse.isValid()){
                    userSubscriptionModel = subscriptionServiceHelper.generateInitPurchaseUserSubscription(request.getUser(), request.getChannel(), request.getPlatform(), PlanTypeEnum.TRIAL, variantModel.getDurationDays(), variantModel, null, userModel, variantModel.getPrice(), false, true);
                    EventEnum eventEnum = EventEnum.getEventForBackendActivation(userSubscriptionModel.getPlanStatus());
                    userSubscriptionModel = saveUserSubscription(userSubscriptionModel, true, eventEnum, userSubscriptionModel.isOrderCompleted(), userSubscriptionModel.isOrderCompleted());
                    if (userSubscriptionModel.isOrderCompleted()) {
                        if (PlanStatusEnum.FREE_TRIAL.equals(userSubscriptionModel.getPlanStatus())) {
                            communicationService.sendFreeTrialSubscriptionSuccessCommunication(userSubscriptionModel);
                        } else {
                            communicationService.sendPaidSubscriptionSuccessCommunication(userSubscriptionModel);
                        }
                    }
                }
            } else if (lastUserSubscription.getStatus() == StatusEnum.FUTURE) {
                validationResponse.addValidationError(ValidationError.FUTURE_SUBSCRIPTION_EXISTS_FOR_USER);
            } else {
                //extend
                StatusEnum statusEnum = lastUserSubscription.getStatus();
                lastUserSubscription = subscriptionServiceHelper.extendSubscription(lastUserSubscription, backendUser.getDurationDays());
                userSubscriptionModel = saveUserSubscription(lastUserSubscription, false, EventEnum.SUBSCRIPTION_TRIAL_EXTENSION, true, !lastUserSubscription.getStatus().equals(statusEnum) || !lastUserSubscription.isSsoCommunicated());
                backendUser.setCompleted(true);
                EventEnum eventEnum = EventEnum.getEventForBackendActivation(userSubscriptionModel.getPlanStatus());
                saveBackendSubscriptionUser(backendUser, eventEnum);
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
        if (userModel == null) {
            userModel = subscriptionServiceHelper.getUser(request);
            userModel = saveUserModel(userModel, EventEnum.NORMAL_USER_CREATION,true);
        } else {
            subscriptionValidationService.validateBlockedUser(userModel, validationResponse);
            if (validationResponse.isValid()) {
                if(!request.getUser().getSsoId().equals(userModel.getSsoId())){
                    List<UserSubscriptionModel> relevantUserSubscriptions = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndOrderCompletedTrueAndDeletedFalseOrderById(userModel.getMobile());
                    List<UserSubscriptionModel> relevantActiveAndFutureSubscriptions = relevantUserSubscriptions.stream().filter(us -> StatusEnum.VALID_WORKING_STATUS_SET.contains(us.getStatus())).collect(Collectors.toList());
                    userModel.setIsDelete(true);
                    userModel = saveUserModel(userModel, EventEnum.USER_SUSPENSION,false);
                    String primeId = userModel.getPrimeId();
                    updateUserDetailsInCache(userModel);
                    if (StringUtils.isNotEmpty(userModel.getEmail())) {
                        if (CollectionUtils.isNotEmpty(relevantActiveAndFutureSubscriptions)) {
                            communicationService.sendUserSuspensionCommunication(userModel, relevantActiveAndFutureSubscriptions);
                        }
                    }
                    userModel = subscriptionServiceHelper.getUser(request);
                    userModel.setPrimeId(primeId);
                    userModel = saveUserModel(userModel, EventEnum.USER_CREATION_WITH_EXISTING_MOBILE, false);
                    if (CollectionUtils.isNotEmpty(relevantUserSubscriptions)) {
                        for (UserSubscriptionModel model : relevantUserSubscriptions) {
                            model.setUser(userModel);
                            saveUserSubscription(model, false, EventEnum.USER_SUBSCRIPTION_SWITCH, true, true, true, true);
                        }
                    }
                    updateUserDetailsInCache(userModel);
                    communicationService.sendUserCreationWithExistingMobileCommunication(userModel, relevantActiveAndFutureSubscriptions);
                }
            }
        }
        return userModel;
    }

    @Override
    @Transactional
    public UserSubscriptionModel saveUserSubscription(UserSubscriptionModel userSubscriptionModel, boolean retryForOrderId,  EventEnum event, boolean publishStatus, boolean updateSSO) {
        return saveUserSubscription(userSubscriptionModel, retryForOrderId, event, publishStatus, updateSSO, updateSSO, publishStatus);
    }

    @Override
    @Transactional
    public UserSubscriptionModel saveUserSubscription(UserSubscriptionModel userSubscriptionModel, boolean retryForOrderId, EventEnum event, boolean publishStatus, boolean updateSSO, boolean ssoIdUpdated, boolean publishDetailsUpdated) {
        int retryCount = retryForOrderId ? GlobalConstants.DB_RETRY_COUNT : GlobalConstants.SINGLE_TRY;
        retryLoop:
        while (retryCount > 0) {
            try {
                userSubscriptionModel = userSubscriptionRepository.save(userSubscriptionModel);
                SubscriptionStatusDTO statusDTO = updateUserStatus(userSubscriptionModel);
                applicationContext.getBean(SubscriptionService.class).saveUserSubscriptionAuditWithExternalUpdatesAsync(userSubscriptionModel, statusDTO, event, publishStatus, updateSSO, ssoIdUpdated, publishDetailsUpdated);
                break retryLoop;
            } catch (Exception e) {
                retryCount--;
                if (retryCount > 0) {
                    userSubscriptionModel.setOrderId(UniqueIdGeneratorUtil.generateOrderId());
                    continue retryLoop;
                }
                throw e;
            }
        }
        return userSubscriptionModel;
    }

    @Async
    @Override
    @Transactional
    public void saveUserSubscriptionAuditWithExternalUpdatesAsync(UserSubscriptionModel userSubscriptionModel, SubscriptionStatusDTO statusDTO, EventEnum event, boolean publishStatus, boolean updateSSO, boolean ssoIdUpdated, boolean publishDetailsUpdated){
        if (userSubscriptionModel.isOrderCompleted()) {
            if(StatusEnum.VALID_SSO_UPDATE_STATUS_SET.contains(userSubscriptionModel.getStatus()) && updateSSO && (!userSubscriptionModel.isSsoCommunicated() || ssoIdUpdated)) {
                userSubscriptionModel = subscriptionServiceHelper.updateSSOStatus(userSubscriptionModel);
            }
            if(publishStatus && publishDetailsUpdated){
                userSubscriptionModel = subscriptionServiceHelper.publishUserStatus(userSubscriptionModel, statusDTO);
            }
            if((publishStatus || updateSSO) && (userSubscriptionModel.isSsoCommunicated() || userSubscriptionModel.isStatusPublished())){
                userSubscriptionModel = userSubscriptionRepository.save(userSubscriptionModel);
            }
        }
        UserSubscriptionAuditModel auditModel = subscriptionServiceHelper.getUserSubscriptionAuditModel(userSubscriptionModel, event);
        auditModel = userSubscriptionAuditRepository.save(auditModel);
    }

    public SubscriptionStatusDTO updateUserStatus(UserSubscriptionModel userSubscriptionModel) {
        return updateUserStatus(userSubscriptionModel, userSubscriptionModel.getUser());
    }

    @Override
    public GenericResponse sendOtp(OtpRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validateSendOtp(request);
        GenericResponse response = new GenericResponse();
        if (validationResponse.isValid()) {
            OtpStatus status = subscriptionServiceHelper.sendOtp(request.getUser().getMobile(), request.isResend());
            if(!status.isSuccess()){
                validationResponse.addValidationError(ValidationError.valueOf(status.getMessage()));
                validationResponse.setValid(false);
            }
        }
        response = subscriptionServiceHelper.prepareGenericResponse(response, validationResponse);
        return response;
    }

    @Override
    public OtpVerificationResponse verifyOtp(OtpVerificationRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validateVerifyOtp(request);
        OtpVerificationResponse response = new OtpVerificationResponse();
        if (validationResponse.isValid()) {
            OtpStatus status = subscriptionServiceHelper.verifyOtp(request.getUser().getMobile(), request.getOtp());
            if(!status.isSuccess()){
                validationResponse.addValidationError(ValidationError.valueOf(status.getMessage()));
                validationResponse.setValid(false);
            }
        }
        response = subscriptionServiceHelper.prepareOtpVerificationResponse(response, validationResponse);
        return response;
    }

    @Override
    @Transactional
    public SubscriptionStatusDTO updateUserStatus(UserSubscriptionModel userSubscriptionModel, UserModel userModel) {
        if (userSubscriptionModel != null && userSubscriptionModel.isOrderCompleted() && StatusEnum.VALID_CACHE_UPDATE_WITH_LAST_END_DATE_SET.contains(userSubscriptionModel.getStatus())) {
            try {
                if (userSubscriptionModel.getStatus() == StatusEnum.FUTURE) {
                    Cache.ValueWrapper vw = cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).get(userModel.getMobile());
                    SubscriptionStatusDTO statusDTO = null;
                    if (vw != null) {
                        statusDTO = (SubscriptionStatusDTO) vw.get();
                        statusDTO.setLastEndDate(userSubscriptionModel.getEndDate());
                        cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).put(userSubscriptionModel.getUser().getMobile(), statusDTO);
                    }
                    return statusDTO;
                }
                if (userSubscriptionModel.getStatus() == StatusEnum.CANCELLED) {
                    LOG.info("Updating user status for future cancellation of userSubscription: " + userSubscriptionModel + ", user: " + userModel);
                    UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndStatusInAndDeletedFalseAndOrderCompletedTrueOrderByIdDesc(userSubscriptionModel.getUser().getMobile(), StatusEnum.VALID_END_DATE_DISPLAY_STATUS_SET);
                    UserSubscriptionModel lastActiveUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndStatusInAndDeletedFalseAndOrderCompletedTrueOrderByIdDesc(userSubscriptionModel.getUser().getMobile(), StatusEnum.VALID_EXPIRY_STATUS_SET);
                    SubscriptionStatusDTO statusDTO = getSubscriptionStatusDTO(lastActiveUserSubscription, userModel, lastUserSubscription.getEndDate());
                    String mobile = userSubscriptionModel.getUser().getMobile();
                    LOG.info("Status DTO: " + statusDTO);
                    cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).put(mobile, statusDTO);
                    return statusDTO;
                }
                LOG.info("Updating user status, userSubscription: " + userSubscriptionModel + ", user: " + userModel);
                UserSubscriptionModel lastUserSubscription = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndStatusInAndDeletedFalseAndOrderCompletedTrueOrderByIdDesc(userSubscriptionModel.getUser().getMobile(), StatusEnum.VALID_END_DATE_DISPLAY_STATUS_SET);
                SubscriptionStatusDTO statusDTO = getSubscriptionStatusDTO(userSubscriptionModel, userModel, lastUserSubscription.getEndDate());
                String mobile = userSubscriptionModel.getUser().getMobile();
                LOG.info("Status DTO: " + statusDTO);
                cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).put(mobile, statusDTO);
                return statusDTO;
            } catch (Exception e) {
                LOG.error("Exception while updateUserStatus: ", e);
                throw e;
            }
        }
        return getUserStatusCacheWithUpdateByMobile(userModel.getMobile());
    }

    private SubscriptionStatusDTO getUserStatusCacheByMobile(String mobile){
        Cache.ValueWrapper vw = cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).get(mobile);
        if (vw != null) {
            return  (SubscriptionStatusDTO) vw.get();
        }
        return null;
    }

    @Override
    public SubscriptionStatusDTO getUserStatusCacheWithUpdateByMobile(String mobile) {
        SubscriptionStatusDTO statusDTO = getUserStatusCacheByMobile(mobile);
        if (statusDTO == null) { //cache update step
            LOG.info("Updating status cache for mobile: "+mobile);
            UserSubscriptionModel userSubscriptionModel = null;
            userSubscriptionModel = userSubscriptionRepository.findFirstByUserMobileAndUserDeletedFalseAndStatusInAndDeletedFalseAndOrderCompletedTrueOrderByIdDesc(mobile, StatusEnum.VALID_USER_STATUS_HISTORY_SET);
            if (userSubscriptionModel!=null) {
                statusDTO = updateUserStatus(userSubscriptionModel);
            }else{
                throw new RuntimeException("INVALID_USER_SUBSCRIPTION_DETAILS");
            }
        }
        LOG.info("SubscriptionStatusDTO: " + statusDTO);
        return statusDTO;
    }

    @Override
    public void clearUserStatusCache(String mobile) {
        if (StringUtils.isNotEmpty(mobile)) {
            cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).evict(mobile);
        }
    }

    private void updateUserDetailsInCache(UserModel userModel) {
        Cache.ValueWrapper vw = cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).get(userModel.getMobile());
        SubscriptionStatusDTO statusDTO = null;
        if (vw != null) {
            statusDTO = (SubscriptionStatusDTO) vw.get();
        }
        if (statusDTO != null) {
            statusDTO.setBlocked(userModel.isBlocked());
            statusDTO.setEmail(userModel.getEmail());
            cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).put(userModel.getMobile(), statusDTO);
        }
    }

    private SubscriptionStatusDTO getSubscriptionStatusDTO(UserSubscriptionModel userSubscriptionModel, UserModel userModel, Date lastEndDate) {
        LOG.info("UserSubscription: " + userSubscriptionModel);
        LOG.info("User: " + userModel);
        SubscriptionStatusDTO statusDTO = new SubscriptionStatusDTO();
        if (userSubscriptionModel == null) {
            if (userModel == null) {
                throw new RuntimeException("User must be present");
            }
            statusDTO.setBlocked(userModel.isBlocked());
            statusDTO.setPlanStatus(userModel.isBlocked() ? PlanStatusEnum.BLOCKED.getCode() : PlanStatusEnum.INIT.getCode());
            LOG.info("StatusDTO: " + statusDTO);
            return statusDTO;
        }
        StatusEnum status = userSubscriptionModel.getStatus();
        statusDTO.setStartDate(userSubscriptionModel.getStartDate());
        statusDTO.setEndDate(userSubscriptionModel.getEndDate());
        statusDTO.setLastEndDate(lastEndDate);
        statusDTO.setBlocked(userSubscriptionModel.getUser().isBlocked());
        if (status == StatusEnum.EXPIRED) {
            if (userSubscriptionModel.getPlanStatus() == PlanStatusEnum.SUBSCRIPTION || userSubscriptionModel.getPlanStatus() == PlanStatusEnum.SUBSCRIPTION_AUTO_RENEWAL) {
                statusDTO.setPlanStatus(PlanStatusEnum.SUBSCRIPTION_EXPIRED.getCode());
            } else if (userSubscriptionModel.getPlanStatus() == PlanStatusEnum.FREE_TRIAL) {
                statusDTO.setPlanStatus(PlanStatusEnum.FREE_TRAIL_EXPIRED.getCode());
            } else if (userSubscriptionModel.getPlanStatus() == PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT) {
                statusDTO.setPlanStatus(PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT_EXPIRED.getCode());
            }else{
                statusDTO.setPlanStatus(userSubscriptionModel.getPlanStatus().getCode());
            }
        } else {
            statusDTO.setPlanStatus(userSubscriptionModel.getPlanStatus().getCode());
        }
        if (userSubscriptionModel.isDeleted()) {
            statusDTO.setPlanStatus(PlanStatusEnum.SUBSCRIPTION_CANCELLED.getCode());
        }
        if (statusDTO.isBlocked()) {
            statusDTO.setPlanStatus(PlanStatusEnum.BLOCKED.getCode());
        }
        statusDTO.setAutoRenewal(userSubscriptionModel.isAutoRenewal());
        statusDTO.setFirstName(userSubscriptionModel.getUser().getFirstName());
        statusDTO.setLastName(userSubscriptionModel.getUser().getLastName());
        statusDTO.setPrimeId(userSubscriptionModel.getUser().getPrimeId());
        LOG.info("StatusDTO: " + statusDTO);
        return statusDTO;
    }

    @Override
    @Transactional
    public UserModel saveUserModel(UserModel userModel, EventEnum eventEnum, boolean retryForPrimeId) {
        if(retryForPrimeId){
            UserModel primeIdModel = userRepository.findFirstByPrimeId(userModel.getPrimeId());
            if(primeIdModel!=null){
                int retryCount = GlobalConstants.PRIME_ID_GENERATION_RETRY_COUNT;
                retryLoop:
                while (retryCount > 0 && primeIdModel!=null) {
                    userModel.setPrimeId(UniqueIdGeneratorUtil.generatePrimeId());
                    primeIdModel = userRepository.findFirstByPrimeId(userModel.getPrimeId());
                    if(primeIdModel==null){
                        break retryLoop;
                    }
                    retryCount--;
                    if(retryCount==0){
                        LOG.error("Can't create unique PrimeId for mobile: "+userModel.getMobile());
                        throw new RuntimeException("Can't create unique PrimeId");
                    }
                    continue retryLoop;
                }
            }
        }
        userModel = userRepository.save(userModel);
        applicationContext.getBean(SubscriptionService.class).saveUserAuditAsync(userModel, eventEnum);
        return userModel;
    }

    @Override
    public GenericResponse updateCacheForMobile(GenericRequest request) {
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreUpdateGenericCRMRequestWithMobile(request);
        GenericResponse response = new GenericResponse();

        if (validationResponse.isValid()) {
            clearUserStatusCache(request.getUser().getMobile());
        }
        response = subscriptionServiceHelper.prepareUpdateCacheResponse(response, validationResponse);
        return response;
    }

    @Override
    public CustomerSearchCRMResponse customerSearchCRM(CustomerSearchRequest request) {
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreCustomerSearchCRM(request);
    	CustomerSearchCRMResponse customerSearchCRMResponse = new CustomerSearchCRMResponse();
    	CustomerSearchDTOs customerSearchDTOs = new CustomerSearchDTOs();
    	List<CustomerSearchDTO> customerSearchDTOList = new ArrayList<CustomerSearchDTO>();
    	if(validationResponse.isValid()){
        	
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
        }
        customerSearchCRMResponse = subscriptionServiceHelper.prepareCustomerSearchCRMResponse(customerSearchCRMResponse, customerSearchDTOs, validationResponse);
        return customerSearchCRMResponse;
    	
    }


    @Override
    public CustomerDetailsCRMResponse customerDetailsCRM(CustomerSearchRequest request) {
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreCustomerDetailsCRM(request);
    	CustomerDetailsCRMResponse customerDetailsCRMResponse = new CustomerDetailsCRMResponse();
    	CustomerCRM customerCRM = null;
    	if(validationResponse.isValid()){
        	UserModel userModel = new UserModel(); 
            UserDTO user = request.getUser();
            //String ssoId = user.getSsoId();
            String mobile = user.getMobile();
            
          	userModel = userRepository.findByMobileAndDeletedFalse(mobile);
           	//userSubscriptionModelList = userSubscriptionRepository.findByUserMobileAndOrderCompletedAndDeleted(userModel.getMobile(), true, false);
           	if(userModel!=null){
           		customerCRM = convertUserToCustomerCRM(userModel);	
           	}

        }

    	customerDetailsCRMResponse = subscriptionServiceHelper.prepareCustomerDetailsResponse(customerDetailsCRMResponse, customerCRM, validationResponse);
    	return customerDetailsCRMResponse;            
        
    }

    

    private CustomerSearchDTO convertUserModelToCustomerSearchDTO(UserModel userModel){
    	
    	CustomerSearchDTO customerSearchDTO = new CustomerSearchDTO();
    	customerSearchDTO.setSsoId(userModel.getSsoId());
    	customerSearchDTO.setMobile(userModel.getMobile());
    	if(userModel.getLastName()!=null){
            customerSearchDTO.setName((userModel.getFirstName()+" "+userModel.getLastName()).trim());
    	}else{
            customerSearchDTO.setName(userModel.getFirstName().trim());
    	}
    	customerSearchDTO.setEmail(userModel.getEmail());

    	
    	
    	if(userModel.getCreated()!=null){
        	customerSearchDTO.setDateOfActivationValSubs(userModel.getCreated());
    	}
    	
    	try{
    	
    		UserSubscriptionModel userSubscriptionModel = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusAndDeletedAndOrderCompletedTrue(userModel.getMobile(), StatusEnum.ACTIVE, false);

    	if(userSubscriptionModel!=null){
        	customerSearchDTO.setCurrentStatus(userSubscriptionModel.getStatus().name());
        	if(userSubscriptionModel.getEndDate()!=null){
        		customerSearchDTO.setExpiryDateValSubs(userSubscriptionModel.getEndDate());
        	}
    	}else{

    		Page<UserSubscriptionModel> userSubscriptionModels = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusAndDeletedOrderByEndDateDesc(userModel.getMobile(), StatusEnum.EXPIRED, false, new PageRequest(0,1));
            if(userSubscriptionModels!=null && userSubscriptionModels.getSize()>0 
        			&& userSubscriptionModels.getContent().size()>0){
        		
        		List<UserSubscriptionModel> userSubscriptionModelList = userSubscriptionModels.getContent();
                userSubscriptionModel = userSubscriptionModelList.get(0);
                if(userSubscriptionModel!=null){
                	customerSearchDTO.setCurrentStatus(userSubscriptionModel.getStatus().name());
                	if(userSubscriptionModel.getEndDate()!=null){
                		customerSearchDTO.setExpiryDateValSubs(userSubscriptionModel.getEndDate());
                	}
            	}
        	}
    	}
    	}catch(Exception e){
    	    LOG.error("Exception in fetching user subscriptions: ", e);

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
    	if(userModel.getLastName()!=null){
    		customerCRM.setName((userModel.getFirstName()+" "+userModel.getLastName()).trim());
    	}else{
    		customerCRM.setName(userModel.getFirstName().trim());
    	}
    	
    	customerCRM.setEmailId(userModel.getEmail());
    	if(userModel.getCreated()!=null){
    		customerCRM.setActivationDateValSubs(userModel.getCreated());
    	}
    	
    	if(userModel.isBlocked()){
    		customerCRM.setBlockedStatus(true);	
    		if(userModel.getUpdated()!=null){
        		customerCRM.setBlockedDateValSubs(userModel.getUpdated());    			
    		}
    	}else{
    		customerCRM.setBlockedStatus(false);	
    	}
    	
    	List<UserSubscriptionModel> userSubscriptionModelList = userModel.getUserSubscriptions();
    	for(UserSubscriptionModel userSubscriptionModel: userSubscriptionModelList){
    		if(!userSubscriptionModel.isDeleted() && StringUtils.isNotEmpty(userSubscriptionModel.getOrderId())){
            	OrderSearchResultCRM orderSearchResultCRM = new OrderSearchResultCRM();
            	
            	orderSearchResultCRM.setOrderCompleted(userSubscriptionModel.isOrderCompleted());
                if (PlanStatusEnum.FREE_TRIAL.equals(userSubscriptionModel.getPlanStatus())) {
                	orderSearchResultCRM.setFreeTrial(true);	
                }else{
                	orderSearchResultCRM.setFreeTrial(false);
                }

                if(userSubscriptionModel.getTransactionStatus()!=null){
                    orderSearchResultCRM.setSubscriptionTransactionStatus(userSubscriptionModel.getStatus().toString());
                }
                if(userSubscriptionModel.getStatus()!=null){
                    orderSearchResultCRM.setSubscriptionStatus(userSubscriptionModel.getStatus().toString());
                }
                
                if(userSubscriptionModel.getSubscriptionVariant()!=null && userSubscriptionModel.getSubscriptionVariant().getPrice()!=null){
                    orderSearchResultCRM.setAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().doubleValue());            	
                }

                if(userSubscriptionModel.getEndDate()!=null){
                    orderSearchResultCRM.setExpiryDateVal(userSubscriptionModel.getEndDate());            	
                }

                if(userSubscriptionModel.getCreated()!=null){
                    orderSearchResultCRM.setOrderDateVal(userSubscriptionModel.getCreated());            	
                }

                orderSearchResultCRM.setOrderDetail("");
                orderSearchResultCRM.setOrderId(userSubscriptionModel.getOrderId());
                
                if(userSubscriptionModel.getStatus()==StatusEnum.ACTIVE && userSubscriptionModel.isOrderCompleted()){
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
                		customerCRM.setExpiryDateValSubs(userSubscriptionModel.getEndDate());
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
                
                if(userSubscriptionModel.getStatus()==StatusEnum.FUTURE && userSubscriptionModel.isOrderCompleted()){
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
    		
        	if(userSubscriptionModels!=null && userSubscriptionModels.getSize()>0 
        			&& userSubscriptionModels.getContent().size()>0){
        		List<UserSubscriptionModel> userSubscriptionModelSubList = userSubscriptionModels.getContent();
        		UserSubscriptionModel userSubscriptionModel = userSubscriptionModelSubList.get(0);
                if(userSubscriptionModel!=null){
                	customerCRM.setSubscriptionStatus(StatusEnum.EXPIRED.toString());
                	if(userSubscriptionModel.getEndDate()!=null){
                		customerCRM.setExpiryDateValSubs(userSubscriptionModel.getEndDate());
                	}
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
    public OrderDetailsCRMResponse getOrderDetailsCRM(OrderDetailsRequest request) {
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreOrderDetailsCRM(request);
    	OrderDetailsCRMResponse orderDetailsCRMResponse = new OrderDetailsCRMResponse();
		OrderDetailsCRM orderDetailsCRM = null;

    	if(validationResponse.isValid()){
        	
    		String orderId = request.getOrderId();

    			UserSubscriptionModel userSubscriptionModel = userSubscriptionRepository.findByOrderIdAndDeleted(orderId, false);
            	//userSubscriptionModelList = userSubscriptionRepository.findByUserMobileAndOrderCompletedAndDeleted(userModel.getMobile(), true, false);
            	if(userSubscriptionModel!=null){
            		orderDetailsCRM = convertUserSubscriptionModelToOrderDetailsCRM(userSubscriptionModel);
            	}            
        }
    	orderDetailsCRMResponse = subscriptionServiceHelper.prepareOrderDetailsResponse(orderDetailsCRMResponse, orderDetailsCRM, validationResponse);

    	return orderDetailsCRMResponse;

    }
    
    private OrderDetailsCRM convertUserSubscriptionModelToOrderDetailsCRM(UserSubscriptionModel userSubscriptionModel){
    	OrderDetailsCRM orderDetailsCRM = new OrderDetailsCRM();
    	try{
    		UserModel user = userSubscriptionModel.getUser();
    		orderDetailsCRM.setOrderId(userSubscriptionModel.getOrderId());

    		
        	try{
            	
        		UserSubscriptionModel userSubscriptionModelDB = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusAndDeletedAndOrderCompletedTrue(user.getMobile(), StatusEnum.ACTIVE, false);
        		Page<UserSubscriptionModel> userSubscriptionModels = userSubscriptionRepository.findByUserMobileAndUserDeletedFalseAndStatusAndDeletedOrderByEndDateDesc(user.getMobile(), StatusEnum.EXPIRED, false, new PageRequest(0,1));

            	if(userSubscriptionModelDB!=null){
            		orderDetailsCRM.setUserSubscriptionStatus(userSubscriptionModelDB.getStatus().name());
            	}else if(userSubscriptionModels!=null && userSubscriptionModels.getSize()>0 
            			&& userSubscriptionModels.getContent().size()>0){
            		
            		List<UserSubscriptionModel> userSubscriptionModelList = userSubscriptionModels.getContent();
            		userSubscriptionModelDB = userSubscriptionModelList.get(0);
                    if(userSubscriptionModelDB!=null){
                    	orderDetailsCRM.setUserSubscriptionStatus(userSubscriptionModelDB.getStatus().name());
                	}
            	}
        	}catch(Exception e){
        	    LOG.error("Exception in fetching user subscriptions: ", e);
        	}
        	
        	if(user.getCreated()!=null){
        		orderDetailsCRM.setUserCreatedDateValSubs(user.getCreated());	
        	}
        	
    		orderDetailsCRM.setUserSubscriptionId(userSubscriptionModel.getId().toString());
    		if(user!=null){
        		orderDetailsCRM.setSsoId(user.getSsoId());
        		
            	if(user.getLastName()!=null){
            		orderDetailsCRM.setName((user.getFirstName()+" "+user.getLastName()).trim());
            	}else{
            		orderDetailsCRM.setName(user.getFirstName().trim());
            	}
        		orderDetailsCRM.setMobileNumber(user.getMobile());
        		orderDetailsCRM.setEmailId(user.getEmail());
    		}
        	if(userSubscriptionModel.getSubscriptionVariant()!=null){
            	orderDetailsCRM.setSubscriptionPlan(userSubscriptionModel.getSubscriptionVariant().getDurationDays().toString() + " DAY " + userSubscriptionModel.getSubscriptionVariant().getPlanType().toString());
            	if(userSubscriptionModel.getSubscriptionVariant().getPrice()!=null){
                	orderDetailsCRM.setAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().doubleValue());            		
                	orderDetailsCRM.setBilledAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().toString());
                	orderDetailsCRM.setRenewalAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().toString());
                	orderDetailsCRM.setPlanPrice(userSubscriptionModel.getSubscriptionVariant().getPrice().toString());

            	}
            	
            	if(userSubscriptionModel.getSubscriptionVariant().getId()!=null){
                	orderDetailsCRM.setVariantId(userSubscriptionModel.getSubscriptionVariant().getId().toString());
            	}
                orderDetailsCRM.setPlanID(userSubscriptionModel.getSubscriptionVariant().getSubscriptionPlan().getId().toString());
                orderDetailsCRM.setPlanType(userSubscriptionModel.getSubscriptionVariant().getPlanType().toString());
                orderDetailsCRM.setBusiness(userSubscriptionModel.getBusiness().toString());
                orderDetailsCRM.setChannel(userSubscriptionModel.getChannel().toString());
                orderDetailsCRM.setPlatform(userSubscriptionModel.getPlatform().toString());
                orderDetailsCRM.setPlanDurationDays(userSubscriptionModel.getSubscriptionVariant().getDurationDays().toString());
        	}
        	if(userSubscriptionModel.getCreated()!=null){
        		orderDetailsCRM.setOrderDateValSubs(userSubscriptionModel.getCreated());	
        	}
        	if(userSubscriptionModel.getEndDate()!=null){
            	orderDetailsCRM.setRenewalDateValSubs(userSubscriptionModel.getEndDate());
            	orderDetailsCRM.setExpiryDateValSubs(userSubscriptionModel.getEndDate());
        	}

        	if(userSubscriptionModel.getStartDate()!=null){
            	orderDetailsCRM.setStartDateValSubs(userSubscriptionModel.getStartDate());
        	}
        	
        	if(userSubscriptionModel.isAutoRenewal()){
            	orderDetailsCRM.setAutoRenewal(true);
            }else{
            	orderDetailsCRM.setAutoRenewal(false);
            }        	
            
            Date date = new Date();
            if(userSubscriptionModel.isOrderCompleted()){
            	orderDetailsCRM.setOrderCompleted(true);
            	if(userSubscriptionModel.getStatus()==StatusEnum.ACTIVE &&
                		userSubscriptionModel.getEndDate().after(date)){
                    orderDetailsCRM.setSubscriptionStatus("SUBSCRIBED");
                }else if(userSubscriptionModel.getStatus()==StatusEnum.FUTURE){
                    orderDetailsCRM.setSubscriptionStatus("FUTURE SUBSCRIPTION");
                }else if(userSubscriptionModel.getStatus()==StatusEnum.EXPIRED){
                	 orderDetailsCRM.setSubscriptionStatus("EXPIRED");
                }else{
                	orderDetailsCRM.setSubscriptionStatus("NA");
                }
            }else{
           	 	orderDetailsCRM.setSubscriptionStatus("NOT COMPLETED");
            }
            
            return orderDetailsCRM;

    	}catch(Exception e){
            LOG.error("Exception in convertUserSubscriptionModelToOrderDetailsCRM: ", e);
            return null;
    	}
    	
    }


    @Override
    public OrderSearchCRMResponse orderSearchCRM(OrderSearchRequest request) {
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreOrderSearchCRM(request);
    	OrderSearchCRMResponse orderSearchCRMResponse = new OrderSearchCRMResponse();
    	if(validationResponse.isValid()){

    		OrderSearchResultsCRM orderSearchResultsCRM = new OrderSearchResultsCRM();
    		List<UserSubscriptionModel> userSubscriptionModelList = null;
        	
        	
    		String subscriptionStatus = request.getSubscriptionStatus();
            String orderId= request.getOrderId();
            Date fromDate = request.getFromDate();
            Date toDate = request.getToDate();

            

            if(orderId==null && subscriptionStatus==null){
    			userSubscriptionModelList = userSubscriptionRepository.findByCreatedBetweenAndDeletedFalseAndOrderIdNotNull(fromDate, toDate);

            }
                        
            if(subscriptionStatus!=null){
        		if(subscriptionStatus.equalsIgnoreCase(StatusEnum.ACTIVE.toString())){
        			if(fromDate!=null || toDate!=null){
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndCreatedBetweenAndDeletedFalseAndOrderIdNotNull(StatusEnum.ACTIVE, fromDate!=null? fromDate: new Date(), toDate!=null? toDate: new Date());
        			}else{
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndDeletedFalseAndOrderIdNotNull(StatusEnum.ACTIVE);
        			}
        		}else if(subscriptionStatus.equalsIgnoreCase(StatusEnum.FUTURE.toString())){
        			if(fromDate!=null || toDate!=null){
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndCreatedBetweenAndDeletedFalseAndOrderIdNotNull(StatusEnum.FUTURE, fromDate!=null? fromDate: new Date(), toDate!=null? toDate: new Date());
        			}else{
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndDeletedFalseAndOrderIdNotNull(StatusEnum.FUTURE);
        			}
        		}else if(subscriptionStatus.equalsIgnoreCase(StatusEnum.EXPIRED.toString())){
        			if(fromDate!=null || toDate!=null){
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndCreatedBetweenAndDeletedFalseAndOrderIdNotNull(StatusEnum.EXPIRED, fromDate!=null? fromDate: new Date(), toDate!=null? toDate: new Date());
            		}else{
            			userSubscriptionModelList = userSubscriptionRepository.findByStatusAndDeletedFalseAndOrderIdNotNull(StatusEnum.EXPIRED);
            		}
        		}

            }
            
            if(orderId!=null){
    			userSubscriptionModelList = userSubscriptionRepository.findByOrderIdAndDeletedFalseAndOrderIdNotNull(orderId);
            }

    		List<OrderSearchResultCRM> orderSearchResultCRMList = new ArrayList<OrderSearchResultCRM>(); 
            //userSubscriptionModelList = userSubscriptionRepository.findByUserMobileAndOrderCompletedAndDeleted(userModel.getMobile(), true, false);
            for(UserSubscriptionModel userSubscriptionModel: userSubscriptionModelList){
            	OrderSearchResultCRM orderSearchResultCRM = convertUserSubscriptionModelToOrderSearchCRM(userSubscriptionModel);
            	orderSearchResultCRMList.add(orderSearchResultCRM);
            }
            orderSearchResultsCRM.setOrderSearchResultsCRM(orderSearchResultCRMList);
            
            orderSearchCRMResponse = subscriptionServiceHelper.prepareOrderSearchResponse(orderSearchCRMResponse, orderSearchResultsCRM, validationResponse);
            return orderSearchCRMResponse;
            
        }else{
        	return null;
        }
        
    }
    

    private OrderSearchResultCRM convertUserSubscriptionModelToOrderSearchCRM(UserSubscriptionModel userSubscriptionModel){
    	OrderSearchResultCRM orderSearchResultCRM = new OrderSearchResultCRM();
    	try{
    		
    		orderSearchResultCRM.setOrderCompleted(userSubscriptionModel.isOrderCompleted());
    		orderSearchResultCRM.setCustomerMobile(userSubscriptionModel.getUser().getMobile());
    		if(userSubscriptionModel.getUser().getFirstName()!=null &&
    				userSubscriptionModel.getUser().getLastName()!=null){
        		orderSearchResultCRM.setCustomerName((userSubscriptionModel.getUser().getFirstName()+" "+userSubscriptionModel.getUser().getLastName()).trim());
    		}else if(userSubscriptionModel.getUser().getFirstName()!=null &&
    				userSubscriptionModel.getUser().getLastName()==null){
        		orderSearchResultCRM.setCustomerName(userSubscriptionModel.getUser().getFirstName().trim());
    		}else if(userSubscriptionModel.getUser().getFirstName()==null &&
    				userSubscriptionModel.getUser().getLastName()!=null){
        		orderSearchResultCRM.setCustomerName(userSubscriptionModel.getUser().getLastName().trim());

    		}
    		
            if(userSubscriptionModel.getTransactionStatus()!=null){
                orderSearchResultCRM.setSubscriptionTransactionStatus(userSubscriptionModel.getStatus().toString());
            }
            if(userSubscriptionModel.getStatus()!=null){
                orderSearchResultCRM.setSubscriptionStatus(userSubscriptionModel.getStatus().toString());
            }

    		orderSearchResultCRM.setAmount(userSubscriptionModel.getSubscriptionVariant().getPrice().doubleValue());
    		orderSearchResultCRM.setSubscriptionStatus(userSubscriptionModel.getStatus().toString());
    		
    		orderSearchResultCRM.setExpiryDateVal(userSubscriptionModel.getEndDate());
    		orderSearchResultCRM.setOrderDateVal(userSubscriptionModel.getCreated());
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
    public BackendSubscriptionUserModel saveBackendSubscriptionUser(BackendSubscriptionUserModel user, EventEnum eventEnum) {
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
    
    public PropertyDataGetResponseCRM getPropertyTableData(PropertyDataRequestCRM request){

		PropertyDataGetResponseCRM propertyDataGetResponse = new PropertyDataGetResponseCRM();
    	
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreGetCRMProperties(request);
    	if(validationResponse.isValid()){

    		
    		Map<PropertyEnum, String> propertyMap = propertyService.getPropertyTableData();
    		
    		propertyDataGetResponse = subscriptionServiceHelper.preparePropertyDataGetResponse(propertyDataGetResponse, propertyMap, validationResponse);
            return propertyDataGetResponse;
            
        }else{
        	return null;
        }
    }

    
    public GenericResponse updatePropertyTableData(PropertyDataUpdateRequestCRM request){

    	
    	ValidationResponse validationResponse = subscriptionValidationService.validatePreUpdateCRMProperties(request);
    	if(validationResponse.isValid()){

    		
    		GenericResponse propertyUpdateResponse = propertyService.updatePropertyTableData(request);
            return propertyUpdateResponse;
            
        }else{
        	return null;
        }
    }

    @Override
    @Transactional(rollbackFor = {DataIntegrityViolationException.class, Exception.class})
    public GenericResponse deleteUser(GenericRequest request) {
        ValidationResponse validationResponse = subscriptionValidationService.validatePreUpdateGenericCRMRequestWithMobile(request);
        GenericResponse response = new GenericResponse();
        if (validationResponse.isValid()) {
            try{
                List<UserModel> users = userRepository.findByMobile(request.getUser().getMobile());
                if(CollectionUtils.isNotEmpty(users)) {
                    List<Long> userIdList = users.stream().map(UserModel::getId).collect(Collectors.toList());
                    for (Long userId : userIdList) {
                        userSubscriptionAuditRepository.deleteByUserId(userId);
                        userAuditRepository.deleteByUserId(userId);
                    }
                    for (UserModel userModel : users) {
                        userSubscriptionRepository.deleteByUser(userModel);
                        userRepository.delete(userModel);
                    }
                    cacheManager.getCache(RedisConstants.PRIME_STATUS_CACHE_KEY).evict(request.getUser().getMobile());
                    cacheManager.getCache(RedisConstants.SSO_AUTH_CACHE_KEY).evict(request.getUser().getMobile());
                }else{
                    validationResponse.addValidationError(ValidationError.INVALID_USER);
                }
            }catch (Exception e){
                LOG.error("Exception", e);
                throw e;
            }
        }
        response = subscriptionServiceHelper.prepareGenericResponse(response, validationResponse);
        return response;
    }

    @Override
    @Async
    @Transactional
    public void saveUserAuditAsync(UserModel userModel, EventEnum event) {
        UserAuditModel userAuditModel = subscriptionServiceHelper.getUserAudit(userModel, event);
        userAuditRepository.save(userAuditModel);
    }

}
