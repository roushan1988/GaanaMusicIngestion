package com.til.prime.timesSubscription.enums;

import java.util.HashSet;
import java.util.Set;

public enum PlatformEnum {
    ANDROID("ANDROID_APP"), IOS("IOS_APP"), WEB("WEB"), WEB_VIA_BACKEND("WEB_VIA_BACKEND"), MOBILE_BROWSER("WAP"), JOB("JOB"), CRM("CRM");

    PlatformEnum(String ssoChannel) {
        this.ssoChannel = ssoChannel;
    }

    private final String ssoChannel;

    public String getSsoChannel() {
        return ssoChannel;
    }

    private static final Set<String> names = new HashSet<String>(){{
        for(PlatformEnum platformEnum: PlatformEnum.values()){
            add(platformEnum.name());
        }
    }};

    public static Set<String> names(){
        return names;
    }
}
