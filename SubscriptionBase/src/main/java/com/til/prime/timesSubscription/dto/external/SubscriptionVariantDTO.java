package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.PlanTypeEnum;

import java.math.BigDecimal;

public class SubscriptionVariantDTO implements Comparable<SubscriptionVariantDTO> {
    private Long variantId;
    private PlanTypeEnum planType;
    private BigDecimal price;
    private Long durationInDays;
    private boolean recurring;

    public SubscriptionVariantDTO(){

    }

    public SubscriptionVariantDTO(Long variantId, String name, PlanTypeEnum planType, BigDecimal price, Long durationInDays, boolean recurring) {
        this.variantId = variantId;
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
        sb.append("planType=").append(planType);
        sb.append(", variantId=").append(variantId);
        sb.append(", price=").append(price);
        sb.append(", durationInDays=").append(durationInDays);
        sb.append(", recurring=").append(recurring);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(SubscriptionVariantDTO o) {
        if(this.planType.getOrder()<o.planType.getOrder()){
            return -1;
        }
        if(this.planType.getOrder()>o.planType.getOrder()){
            return 1;
        }
        return 0;
    }
}
