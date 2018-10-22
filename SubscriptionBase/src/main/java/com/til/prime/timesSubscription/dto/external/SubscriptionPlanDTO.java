package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.CountryEnum;
import com.til.prime.timesSubscription.enums.CurrencyEnum;

import java.util.List;

public class SubscriptionPlanDTO {
    private Long planId;
    private String name;
    private BusinessEnum business;
    private CurrencyEnum currency;
    private CountryEnum country;
    private List<SubscriptionVariantDTO> variants;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BusinessEnum getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEnum business) {
        this.business = business;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    public CountryEnum getCountry() {
        return country;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public List<SubscriptionVariantDTO> getVariants() {
        return variants;
    }

    public void setVariants(List<SubscriptionVariantDTO> variants) {
        this.variants = variants;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionPlanDTO{");
        sb.append("planId=").append(planId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", business=").append(business);
        sb.append(", currency=").append(currency);
        sb.append(", country=").append(country);
        sb.append(", variants=").append(variants);
        sb.append('}');
        return sb.toString();
    }
}
