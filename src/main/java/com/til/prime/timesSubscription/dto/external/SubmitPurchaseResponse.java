package com.til.prime.timesSubscription.dto.external;

public class SubmitPurchaseResponse extends GenericResponse {
    private String orderId;
    private Long variantId;
    private Long planId;
    private String planStatus;
    private String transactionStatus;
    private boolean orderCompleted;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public boolean isOrderCompleted() {
        return orderCompleted;
    }

    public void setOrderCompleted(boolean orderCompleted) {
        this.orderCompleted = orderCompleted;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubmitPurchaseResponse{");
        sb.append("orderId='").append(orderId).append('\'');
        sb.append(", variantId=").append(variantId);
        sb.append(", planId=").append(planId);
        sb.append(", planStatus='").append(planStatus).append('\'');
        sb.append(", transactionStatus='").append(transactionStatus).append('\'');
        sb.append(", orderCompleted=").append(orderCompleted);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}