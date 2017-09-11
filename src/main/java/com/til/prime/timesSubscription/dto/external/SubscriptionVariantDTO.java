package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.PlanTypeEnum;

import java.math.BigDecimal;

public class SubscriptionVariantDTO {
    private Long variantId;
    private String name;
    private PlanTypeEnum planType;
    private BigDecimal price;
    private Long durationInDays;
    private boolean recurring;

    public SubscriptionVariantDTO(Long variantId, String name, PlanTypeEnum planType, BigDecimal price, Long durationInDays, boolean recurring) {
        this.variantId = variantId;
        this.name = name;
        this.planType = planType;
        this.price = price;
        this.durationInDays = durationInDays;
        this.recurring = recurring;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlanTypeEnum getPlanType() {
        return planType;
    }

    public void setPlanType(PlanTypeEnum planType) {
        this.planType = planType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(Long durationInDays) {
        this.durationInDays = durationInDays;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionVariantDTO{");
        sb.append("name='").append(name).append('\'');
        sb.append(", planType=").append(planType);
        sb.append(", variantId=").append(variantId);
        sb.append(", price=").append(price);
        sb.append(", durationInDays=").append(durationInDays);
        sb.append(", recurring=").append(recurring);
        sb.append('}');
        return sb.toString();
    }
}
