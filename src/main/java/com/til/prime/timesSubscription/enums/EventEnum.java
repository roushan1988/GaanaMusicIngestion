package com.til.prime.timesSubscription.enums;

public enum EventEnum {
    PLAN_CREATION,
    PLAN_FETCH,
    FREE_TRIAL,
    FREE_TRIAL_WITH_PAYMENT,
    INIT_PURCHASE,
    ORDER_ID_GENERATION,
    PAYMENT_SUCCESS,
    PAYMENT_FAILURE,
    SSO_COMMUNICATION,
    PURCHASE_HISTORY_FETCH,
    SUBSCRIPTION_CANCELLATION,
    SUBSCRIPTION_TRIAL_EXTENSION;

    public static final EventEnum getEventByInitPlanStatus(PlanStatusEnum planStatus){
        if(planStatus== PlanStatusEnum.FREE_TRIAL){
            return EventEnum.FREE_TRIAL;
        }else if(planStatus==PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT){
            return EventEnum.FREE_TRIAL_WITH_PAYMENT;
        }else{
            return EventEnum.INIT_PURCHASE;
        }
    }
}
