package com.til.prime.timesSubscription.dto.external;

import java.util.Date;

public class ExtendTrialResponse extends GenericResponse {
    private Long userSubscriptionId;
    private String orderId;
    private Long planId;
    private Long variantId;
    private Date startDate;
    private Date endDate;

    public Long getUserSubscriptionId() {
        return userSubscriptionId;
    }

    public void setUserSubscriptionId(Long userSubscriptionId) {
        this.userSubscriptionId = userSubscriptionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExtendTrialResponse{");
        sb.append("userSubscriptionId=").append(userSubscriptionId);
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", planId=").append(planId);
        sb.append(", variantId=").append(variantId);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
