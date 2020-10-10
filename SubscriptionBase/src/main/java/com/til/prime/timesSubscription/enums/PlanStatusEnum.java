package com.til.prime.timesSubscription.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    public static PlanStatusEnum getPlanStatusEnum(int code){
        for(PlanStatusEnum planStatusEnum : PlanStatusEnum.values()){
            if(planStatusEnum.getCode() == code){
                return planStatusEnum;
            }
        }
        return null;
    }
}
