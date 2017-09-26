package com.til.prime.timesSubscription.enums;

import java.util.HashSet;
import java.util.Set;

public enum ChannelEnum {
    TIMES_PRIME, GAANA, TOI, DINEOUT, ET;

    private static final Set<String> names = new HashSet<String>(){{
        for(ChannelEnum channelEnum: ChannelEnum.values()){
            add(channelEnum.name());
        }
    }};

    public static Set<String> names(){
        return names;
    }
}
