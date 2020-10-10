package com.til.prime.timesSubscription.dto.external;

public class CheckEligibilityRequest extends GenericRequest {
    private Long planId;
    private Long variantId;
    private String planType;
    private String business;

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

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckEligibilityRequest{");
        sb.append("planId=").append(planId);
        sb.append(", variantId=").append(variantId);
        sb.append(", planType='").append(planType).append('\'');
        sb.append(", business='").append(business).append('\'');
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
