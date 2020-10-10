package com.til.prime.timesSubscription.dto.external;

import java.math.BigDecimal;

public class PlanPriceUpdateRequest extends GenericRequest {
    private Long planId;
    private Long variantId;
    private BigDecimal price;
    private Long timestamp;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanPriceUpdateRequest{");
        sb.append("planId=").append(planId);
        sb.append(", variantId=").append(variantId);
        sb.append(", price=").append(price);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
