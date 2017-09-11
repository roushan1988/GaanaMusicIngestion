package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.enums.ChannelEnum;

import javax.persistence.*;

@Entity
@Table(name="offer")
public class OfferModel extends BaseModel {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlanModel subscriptionPlan;
    @Column
    @Enumerated(EnumType.STRING)
    private ChannelEnum channel;
    @Column(name="third_party")
    private boolean thirdParty;
    @Column
    private String offering;
    @Column
    private Long quantity;

    public SubscriptionPlanModel getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlanModel subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public ChannelEnum getChannel() {
        return channel;
    }

    public void setChannel(ChannelEnum channel) {
        this.channel = channel;
    }

    public boolean isThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(boolean thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getOffering() {
        return offering;
    }

    public void setOffering(String offering) {
        this.offering = offering;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
