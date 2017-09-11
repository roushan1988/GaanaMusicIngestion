package com.til.prime.timesSubscription.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum PlanTypeEnum {
    TRIAL(1), TRIAL_WITH_PAYMENT(2), PAYMENT(3);

    PlanTypeEnum(int order) {
        this.order = order;
    }

    private final int order;

    public int getOrder() {
        return order;
    }

    public static final Set<PlanTypeEnum> USAGE_RESTRICTED_PLANS_TYPES = new HashSet<>(Arrays.asList(TRIAL, TRIAL_WITH_PAYMENT));

    private static final Set<String> names = new HashSet<String>(){{
        for(PlanTypeEnum planTypeEnum: PlanTypeEnum.values()){
            add(planTypeEnum.name());
        }
    }};

    public static Set<String> names(){
        return names;
    }
}
