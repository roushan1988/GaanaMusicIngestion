package com.til.prime.timesSubscription.enums;

public enum EventEnum {
    USER_CREATION,
    PLAN_CREATION,
    PLAN_FETCH,
    FREE_TRIAL,
    FREE_TRIAL_WITH_PAYMENT,
    INIT_PURCHASE,
    ORDER_ID_GENERATION,
    PAYMENT_SUCCESS,
    PAYMENT_FAILURE,
    SSO_COMMUNICATION,
    USER_SUBSCRIPTION_EXPIRY,
    USER_SUBSCRIPTION_ACTIVE,
    PURCHASE_HISTORY_FETCH,
    SUBSCRIPTION_APP_CANCELLATION,
    SUBSCRIPTION_SERVER_CANCELLATION,
    SUBSCRIPTION_TRIAL_EXTENSION,
    SUBSCRIPTION_AUTO_RENEWAL,
    SUBSCRIPTION_EXPIRY_REMINDER,
    SUBSCRIPTION_RENEWAL_REMINDER,
    EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER;

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
