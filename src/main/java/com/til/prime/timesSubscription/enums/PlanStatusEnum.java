package com.til.prime.timesSubscription.enums;

import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.math.BigDecimal;
import java.util.*;

public enum PlanStatusEnum {
    INIT(0),
    FREE_TRIAL(1),
    FREE_TRIAL_EXPIRED(2),
    FREE_TRIAL_WITH_PAYMENT(3),
    FREE_TRIAL_WITH_PAYMENT_EXPIRED(4),
    SUBSCRIPTION(5),
    SUBSCRIPTION_EXPIRED(6),
    SUBSCRIPTION_CANCELLED(7),
    SUBSCRIPTION_AUTO_RENEWAL(8),
    BLOCKED(9),
    ;

    public static final Set<PlanStatusEnum> ACTIVE_STATUS_SET = new HashSet<>(Arrays.asList(FREE_TRIAL, FREE_TRIAL_WITH_PAYMENT, SUBSCRIPTION, SUBSCRIPTION_AUTO_RENEWAL));
    public static final Set<Integer> ACTIVE_STATUS_CODE_SET = new HashSet<Integer>(){{
        for(PlanStatusEnum planStatus: ACTIVE_STATUS_SET){
            add(planStatus.getCode());
        }
    }};

    public static boolean validTimesPrimeUser(int planStatus){
        return PlanStatusEnum.ACTIVE_STATUS_CODE_SET.contains(planStatus);
    }

    PlanStatusEnum(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return code;
    }

    public static PlanStatusEnum getPlanStatus(StatusEnum status, PlanTypeEnum planTypeEnum, BigDecimal price, UserSubscriptionModel lastUserSubscription, boolean autoRenewal){
        if(status==StatusEnum.CANCELLED || status==StatusEnum.ACTIVE_CANCELLED){
            return PlanStatusEnum.SUBSCRIPTION_CANCELLED;
        }else if(status==StatusEnum.EXPIRED){
            if(PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planTypeEnum)){
                if(price.compareTo(BigDecimal.ZERO)<=0){
                    return PlanStatusEnum.FREE_TRIAL_EXPIRED;
                }else{
                    return PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT_EXPIRED;
                }
            }else{
                return PlanStatusEnum.SUBSCRIPTION_EXPIRED;
            }
        }else{
            if(PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planTypeEnum)){
                if(price.compareTo(BigDecimal.ZERO)<=0){
                    return PlanStatusEnum.FREE_TRIAL;
                }else{
                    return PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT;
                }
            }else if(price.compareTo(BigDecimal.ZERO)>0){
                if(autoRenewal && lastUserSubscription!=null && (lastUserSubscription.getPlanStatus()==PlanStatusEnum.SUBSCRIPTION || lastUserSubscription.getPlanStatus()==PlanStatusEnum.SUBSCRIPTION_AUTO_RENEWAL)){
                    return PlanStatusEnum.SUBSCRIPTION_AUTO_RENEWAL;
                }else{
                    return PlanStatusEnum.SUBSCRIPTION;
                }
            }
            return PlanStatusEnum.SUBSCRIPTION;
        }
    }
}
