package com.til.prime.timesSubscription.enums;

public enum PropertyEnum {
    TEST("String");

    private final String type;

    PropertyEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
