package com.til.prime.timesSubscription.enums;

import java.util.Arrays;
import java.util.List;

public enum PropertyEnum {
    SUBSCRIPTION_RENEWAL_REMINDER_DAYS("String"),
    EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER_DAYS("String"),
    SUBSCRIPTION_EXPIRY_REMINDER_DAYS("String"),
    EXTERNAL_CLIENTS("List<Object>");

    public static final List<PropertyEnum> RELOAD_PROPERTIES = Arrays.asList(SUBSCRIPTION_EXPIRY_REMINDER_DAYS, SUBSCRIPTION_RENEWAL_REMINDER_DAYS, EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER_DAYS);

    private final String type;

    PropertyEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
