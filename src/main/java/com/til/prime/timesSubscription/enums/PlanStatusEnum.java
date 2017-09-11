package com.til.prime.timesSubscription.enums;

import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.math.BigDecimal;
import java.util.*;

public enum PlanStatusEnum {
    INIT(0),
    FREE_TRIAL(1),
    FREE_TRAIL_EXPIRED(2),
    FREE_TRIAL_WITH_PAYMENT(3),
    FREE_TRIAL_WITH_PAYMENT_EXPIRED(4),
    SUBSCRIPTION(5),
    SUBSCRIPTION_EXPIRED(6),
    SUBSCRIPTION_CANCELLED(7),
    SUBSCRIPTION_AUTO_RENEWAL(8);

    PlanStatusEnum(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return code;
    }

    private static final Set<PlanStatusEnum> VALID_INITIATION_STATES = new HashSet<>(Arrays.asList(INIT, FREE_TRIAL, FREE_TRIAL_WITH_PAYMENT, SUBSCRIPTION));
    private static final Map<PlanStatusEnum, Set<PlanStatusEnum>> VALID_PROGRESSION_STATES = new HashMap<PlanStatusEnum, Set<PlanStatusEnum>>(){{
        put(INIT, new HashSet<>(Arrays.asList(FREE_TRIAL, FREE_TRIAL_WITH_PAYMENT, SUBSCRIPTION)));
        put(FREE_TRIAL, new HashSet<>(Arrays.asList(FREE_TRAIL_EXPIRED, FREE_TRIAL_WITH_PAYMENT, SUBSCRIPTION, SUBSCRIPTION_AUTO_RENEWAL)));
        put(FREE_TRAIL_EXPIRED, new HashSet<>(Arrays.asList(FREE_TRIAL_WITH_PAYMENT, SUBSCRIPTION, SUBSCRIPTION_AUTO_RENEWAL)));
        put(FREE_TRIAL_WITH_PAYMENT, new HashSet<>(Arrays.asList(FREE_TRIAL_WITH_PAYMENT_EXPIRED, SUBSCRIPTION, SUBSCRIPTION_AUTO_RENEWAL)));
        put(FREE_TRIAL_WITH_PAYMENT_EXPIRED, new HashSet<>(Arrays.asList(SUBSCRIPTION, SUBSCRIPTION_AUTO_RENEWAL)));
        put(SUBSCRIPTION, new HashSet<>(Arrays.asList(SUBSCRIPTION_EXPIRED, SUBSCRIPTION_CANCELLED, SUBSCRIPTION_AUTO_RENEWAL)));
        put(SUBSCRIPTION_EXPIRED, new HashSet<>(Arrays.asList(SUBSCRIPTION, SUBSCRIPTION_AUTO_RENEWAL)));
        put(SUBSCRIPTION_CANCELLED, new HashSet<>(Arrays.asList(SUBSCRIPTION, SUBSCRIPTION_AUTO_RENEWAL)));
        put(SUBSCRIPTION_AUTO_RENEWAL, new HashSet<>(Arrays.asList(SUBSCRIPTION_AUTO_RENEWAL, SUBSCRIPTION_CANCELLED, SUBSCRIPTION_EXPIRED)));
    }};

    public static boolean checkValidInitiationState(PlanStatusEnum planStatusEnum){
        return VALID_INITIATION_STATES.contains(planStatusEnum);
    }

    public static boolean checkValidProgressionState(PlanStatusEnum initEnum, PlanStatusEnum subsequentEnum){
        return VALID_PROGRESSION_STATES.get(initEnum).contains(subsequentEnum);
    }

    public static PlanStatusEnum getPlanStatus(PlanTypeEnum planTypeEnum, BigDecimal price, UserSubscriptionModel lastUserSubscription, boolean autoRenewal){
        if(PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planTypeEnum)){
            if(price.compareTo(BigDecimal.ZERO)<=0){
                return PlanStatusEnum.FREE_TRIAL;
            }else{
                return PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT;
            }
        }else if(price.compareTo(BigDecimal.ZERO)>0){
            if(autoRenewal && lastUserSubscription.getPlanStatus()==PlanStatusEnum.SUBSCRIPTION){
                return PlanStatusEnum.SUBSCRIPTION_AUTO_RENEWAL;
            }else{
                return PlanStatusEnum.SUBSCRIPTION;
            }
        }
        return PlanStatusEnum.SUBSCRIPTION;
    }
}
