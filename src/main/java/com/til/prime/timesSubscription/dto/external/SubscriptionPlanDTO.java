package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.CountryEnum;
import com.til.prime.timesSubscription.enums.CurrencyEnum;

import java.util.List;

public class SubscriptionPlanDTO {
    private Long planId;
    private String name;
    private String description;
    private BusinessEnum business;
    private CurrencyEnum currency;
    private CountryEnum country;
    private String family;
    private List<SubscriptionVariantDTO> variants;
    private List<SubscriptionOfferDTO> offers;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public List<SubscriptionVariantDTO> getVariants() {
        return variants;
    }

    public void setVariants(List<SubscriptionVariantDTO> variants) {
        this.variants = variants;
    }

    public List<SubscriptionOfferDTO> getOffers() {
        return offers;
    }

    public void setOffers(List<SubscriptionOfferDTO> offers) {
        this.offers = offers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionPlanDTO{");
        sb.append("planId=").append(planId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", business=").append(business);
        sb.append(", currency=").append(currency);
        sb.append(", country=").append(country);
        sb.append(", family='").append(family).append('\'');
        sb.append(", variants=").append(variants);
        sb.append(", offers=").append(offers);
        sb.append('}');
        return sb.toString();
    }
}
