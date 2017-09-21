package com.til.prime.timesSubscription.enums;

public enum PropertyEnum {
    SUBSCRIPTION_RENEWAL_REMINDER_DAYS("String"),
    SUBSCRIPTION_EXPIRY_REMINDER_DAYS("String");

    private final String type;

    PropertyEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
