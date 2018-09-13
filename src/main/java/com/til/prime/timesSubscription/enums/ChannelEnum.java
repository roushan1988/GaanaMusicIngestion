package com.til.prime.timesSubscription.enums;

import java.util.HashSet;
import java.util.Set;

public enum ChannelEnum {
    TIMES_PRIME("Times Prime"), GAANA("GAANA"), TOI("TOI plus"), DINEOUT("DINEOUT"), ET("ET");
    String name;
    ChannelEnum(String name){
        this.name = name;
    }

    private static final Set<String> names = new HashSet<String>(){{
        for(ChannelEnum channelEnum: ChannelEnum.values()){
            add(channelEnum.name());
        }
    }};

    public String getName() {
        return name;
    }

    public static Set<String> names(){
        return names;
    }
}
