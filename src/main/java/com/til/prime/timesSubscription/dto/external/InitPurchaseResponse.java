package com.til.prime.timesSubscription.dto.external;

import java.math.BigDecimal;

public class InitPurchaseResponse extends GenericResponse {
    private Long userSubscriptionId;
    private String orderId;
    private Long planId;
    private Long variantId;
    private boolean paymentRequired;
    private BigDecimal paymentAmount;
    private int daysLeft;
    private String type;
    private String primeId;

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

    public boolean isPaymentRequired() {
        return paymentRequired;
    }

    public void setPaymentRequired(boolean paymentRequired) {
        this.paymentRequired = paymentRequired;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrimeId() {
        return primeId;
    }

    public void setPrimeId(String primeId) {
        this.primeId = primeId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InitPurchaseResponse{");
        sb.append("userSubscriptionId=").append(userSubscriptionId);
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", planId=").append(planId);
        sb.append(", variantId=").append(variantId);
        sb.append(", paymentRequired=").append(paymentRequired);
        sb.append(", paymentAmount=").append(paymentAmount);
        sb.append(", daysLeft=").append(daysLeft);
        sb.append(", type='").append(type).append('\'');
        sb.append(", primeId='").append(primeId).append('\'');
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
