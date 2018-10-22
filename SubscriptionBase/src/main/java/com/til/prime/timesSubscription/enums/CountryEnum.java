package com.til.prime.timesSubscription.enums;

import java.util.HashSet;
import java.util.Set;

public enum CountryEnum {
    IN("India"), USA("United States of America");

    CountryEnum(String name){
        this.fullName = name;
    }
    private final String fullName;

    public String getFullName() {
        return fullName;
    }

    private static final Set<String> names = new HashSet<String>(){{
        for(CountryEnum countryEnum: CountryEnum.values()){
            add(countryEnum.name());
        }
    }};

    public static Set<String> names(){
        return names;
    }

}
