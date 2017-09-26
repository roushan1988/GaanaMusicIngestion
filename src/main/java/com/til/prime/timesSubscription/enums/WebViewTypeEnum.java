package com.til.prime.timesSubscription.enums;

public enum WebViewTypeEnum {
    TOAST("Toast"), DIALOG("Dialog");

    private final String name;

    WebViewTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
