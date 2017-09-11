package com.til.prime.timesSubscription.enums;

import java.util.*;

public enum BusinessEnum {
    TIMES_PRIME, GAANA, TOI, ET, DINEOUT;

    private static final Map<BusinessEnum, Set<BusinessEnum>> ENVELOP_BUSINESS = new HashMap<BusinessEnum, Set<BusinessEnum>>(){{
        put(TIMES_PRIME, new HashSet<>(Arrays.asList(TIMES_PRIME)));
        put(GAANA, new HashSet<>(Arrays.asList(TIMES_PRIME, GAANA)));
        put(TOI, new HashSet<>(Arrays.asList(TIMES_PRIME, TOI)));
        put(ET, new HashSet<>(Arrays.asList(TIMES_PRIME, ET)));
    }};

    private static final Set<String> names = new HashSet<String>(){{
        for(BusinessEnum businessEnum: BusinessEnum.values()){
            add(businessEnum.name());
        }
    }};

    public static Set<String> names(){
        return names;
    }

    public static Set<BusinessEnum> getEnvelopBusiness(BusinessEnum businessEnum){
        return ENVELOP_BUSINESS.get(businessEnum);
    }
}
