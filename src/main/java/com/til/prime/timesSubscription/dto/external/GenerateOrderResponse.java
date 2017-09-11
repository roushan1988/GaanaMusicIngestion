package com.til.prime.timesSubscription.dto.external;

public class GenerateOrderResponse extends GenericResponse {
    private Long userSubscriptionId;
    private String orderId;
    private Long planId;
    private Long variantId;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GenerateOrderResponse{");
        sb.append("userSubscriptionId=").append(userSubscriptionId);
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", planId='").append(planId);
        sb.append(", variantId=").append(variantId);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
