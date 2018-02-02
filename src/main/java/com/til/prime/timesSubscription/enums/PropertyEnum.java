package com.til.prime.timesSubscription.enums;

import java.util.Arrays;
import java.util.List;

public enum PropertyEnum {
    HEALTH_CHECK("String"),
    BACKEND_FREE_TRIAL_PLAN("Object"),
    SUBSCRIPTION_RENEWAL_REMINDER_DAYS("String"),
    EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER_DAYS("String"),
    SUBSCRIPTION_EXPIRY_REMINDER_DAYS("String"),
    EXTERNAL_CLIENTS("List<Object>"),
    SUBSCRIPTION_PLAN_DTOS("List<Object>"),
    SUBSCRIPTION_PLAN_MODELS("List<Object>"),
    ;

    public static final List<PropertyEnum> RELOAD_PROPERTIES = Arrays.asList(HEALTH_CHECK, SUBSCRIPTION_EXPIRY_REMINDER_DAYS, SUBSCRIPTION_RENEWAL_REMINDER_DAYS, EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER_DAYS);
    public static final List<PropertyEnum> CRM_UPDATE_PROPERTIES = Arrays.asList(SUBSCRIPTION_EXPIRY_REMINDER_DAYS, SUBSCRIPTION_RENEWAL_REMINDER_DAYS, EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER_DAYS);

    private final String type;

    PropertyEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
