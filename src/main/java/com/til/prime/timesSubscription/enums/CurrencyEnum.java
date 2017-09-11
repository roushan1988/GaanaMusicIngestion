package com.til.prime.timesSubscription.enums;

import java.util.HashSet;
import java.util.Set;

public enum CurrencyEnum {
    USD, INR;

    private static final Set<String> names = new HashSet<String>(){{
        for(CurrencyEnum currencyEnum: CurrencyEnum.values()){
            add(currencyEnum.name());
        }
    }};

    public static Set<String> names(){
        return names;
    }
}
