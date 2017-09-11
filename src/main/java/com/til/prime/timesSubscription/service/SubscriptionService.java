package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.aspect.Loggable;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.model.UserModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public interface SubscriptionService {

    PlanListResponse getAllPlans(PlanListRequest request);
    InitPurchaseResponse initPurchasePlan(InitPurchaseRequest request);
    GenerateOrderResponse generateOrder(GenerateOrderRequest request);
    SubmitPurchaseResponse submitPurchasePlan(SubmitPurchaseRequest request);
    PurchaseHistoryResponse getPurchaseHistory(PurchaseHistoryRequest request);
    CancelSubscriptionResponse cancelSubscription(CancelSubscriptionRequest request);
    ExtendTrialResponse extendTrial(ExtendTrialRequest request);
    GenericResponse checkEligibility(CheckEligibilityRequest request);
    UserModel getOrCreateUser(GenericRequest request);
}
