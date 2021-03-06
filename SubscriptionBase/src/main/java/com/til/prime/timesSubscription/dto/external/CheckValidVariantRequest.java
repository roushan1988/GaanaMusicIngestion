package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.BusinessEnum;

public class CheckValidVariantRequest extends GenericRequest {
    private Long planId;
    private Long variantId;
    private String variantName;
    private BusinessEnum business = BusinessEnum.TIMES_PRIME;

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

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public BusinessEnum getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEnum business) {
        this.business = business;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckValidVariantRequest{");
        sb.append("planId=").append(planId);
        sb.append(", variantId=").append(variantId);
        sb.append(", variantName='").append(variantName).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append(", business=").append(business);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
