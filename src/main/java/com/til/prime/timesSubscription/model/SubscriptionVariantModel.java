package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.enums.PlanTypeEnum;
import sun.dc.pr.PRError;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="subscription_variant")
public class SubscriptionVariantModel extends BaseModel {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlanModel subscriptionPlan;
    @Column
    private String name;
    @Column(name="plan_type")
    @Enumerated(EnumType.STRING)
    private PlanTypeEnum planType;
    @Column
    private boolean recurring;
    @Column
    private BigDecimal price;
    @Column(name="duration_days")
    private Long durationDays;
    @OneToMany(mappedBy = "subscriptionVariant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserSubscriptionModel> userSubscriptions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubscriptionPlanModel getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlanModel subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public PlanTypeEnum getPlanType() {
        return planType;
    }

    public void setPlanType(PlanTypeEnum planType) {
        this.planType = planType;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Long durationDays) {
        this.durationDays = durationDays;
    }

    public List<UserSubscriptionModel> getUserSubscriptions() {
        return userSubscriptions;
    }

    public void setUserSubscriptions(List<UserSubscriptionModel> userSubscriptions) {
        this.userSubscriptions = userSubscriptions;
    }
}
