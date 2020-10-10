package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.CountryEnum;
import com.til.prime.timesSubscription.enums.CurrencyEnum;

public class SubscriptionPlanVariantResponse extends GenericResponse {
    private Long planId;
    private String name;
    private BusinessEnum business;
    private CurrencyEnum currency;
    private CountryEnum country;
    private SubscriptionVariantDTO variant;

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

    public SubscriptionVariantDTO getVariant() {
        return variant;
    }

    public void setVariant(SubscriptionVariantDTO variant) {
        this.variant = variant;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionPlanVariantResponse{");
        sb.append("planId=").append(planId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", business=").append(business);
        sb.append(", currency=").append(currency);
        sb.append(", country=").append(country);
        sb.append(", variant=").append(variant);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrorCategory=").append(validationErrorCategory);
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
