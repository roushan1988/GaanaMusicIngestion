package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.enums.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="subscription_plan")
public class SubscriptionPlanModel extends BaseModel {
//    @Column(name="plan_id")
//    private String planId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    @Enumerated(EnumType.STRING)
    private BusinessEnum business;
    @Column
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;
    @Column
    @Enumerated(EnumType.STRING)
    private CountryEnum country;
    @Column
    @Enumerated(EnumType.STRING)
    private PlanFamilyEnum family;
    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SubscriptionVariantModel> variants;
    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OfferModel> offers;

//    public String getPlanId() {
//        return planId;
//    }
//
//    public void setPlanId(String planId) {
//        this.planId = planId;
//    }

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

    public PlanFamilyEnum getFamily() {
        return family;
    }

    public void setFamily(PlanFamilyEnum family) {
        this.family = family;
    }

    public List<SubscriptionVariantModel> getVariants() {
        return variants;
    }

    public void setVariants(List<SubscriptionVariantModel> variants) {
        this.variants = variants;
    }

    public List<OfferModel> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferModel> offers) {
        this.offers = offers;
    }
}
