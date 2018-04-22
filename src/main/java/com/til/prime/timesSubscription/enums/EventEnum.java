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
    USER_STATUS_PUBLISH,
    USER_SUBSCRIPTION_EXPIRY,
    USER_SUBSCRIPTION_ACTIVE,
    PURCHASE_HISTORY_FETCH,
    SUBSCRIPTION_APP_CANCELLATION,
    SUBSCRIPTION_SERVER_CANCELLATION,
    SUBSCRIPTION_TRIAL_EXTENSION,
    SUBSCRIPTION_TURN_OFF_AUTO_DEBIT,
    SUBSCRIPTION_AUTO_RENEWAL,
    SUBSCRIPTION_EXPIRY_REMINDER,
    SUBSCRIPTION_RENEWAL_REMINDER,
    EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER,
    USER_SUSPENSION,
    NORMAL_USER_CREATION,
    USER_CREATION_WITH_EXISTING_MOBILE,
    USER_BLOCK,
    USER_UNBLOCK,
    USER_SUBSCRIPTION_SWITCH,
    BACKEND_USER_SUBSCRIPTION_CREATION,
    BACKEND_FREE_TRAIL_ACTIVATION,
    BACKEND_SUBSCRIPTION_EXTENSION,
    PRIME_ID_GENERATION,
    ;

    public static final EventEnum getEventByInitPlanStatus(PlanStatusEnum planStatus){
        if(planStatus== PlanStatusEnum.FREE_TRIAL){
            return EventEnum.FREE_TRIAL;
        }else if(planStatus==PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT){
            return EventEnum.FREE_TRIAL_WITH_PAYMENT;
        }else{
            return EventEnum.INIT_PURCHASE;
        }
    }

    public static final EventEnum getEventForBackendActivation(PlanStatusEnum planStatus){
        if(planStatus== PlanStatusEnum.FREE_TRIAL){
            return EventEnum.BACKEND_FREE_TRAIL_ACTIVATION;
        }else{
            return EventEnum.BACKEND_SUBSCRIPTION_EXTENSION;
        }
    }
}
