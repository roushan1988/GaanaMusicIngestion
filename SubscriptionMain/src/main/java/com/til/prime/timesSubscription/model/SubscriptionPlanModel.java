package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.enums.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="subscription_plan")
public class SubscriptionPlanModel extends BaseModel {
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
    @Fetch(FetchMode.SUBSELECT)
    private List<SubscriptionVariantModel> variants;

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
}