package com.til.prime.timesSubscription.dto.internal;

public class RefundInternalResponse {
    private boolean success;
    private Double refundedAmount;

    public RefundInternalResponse(boolean success, Double refundedAmount) {
        this.success = success;
        this.refundedAmount = refundedAmount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Double getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(Double refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RefundInternalResponse{");
        sb.append("success=").append(success);
        sb.append(", refundedAmount=").append(refundedAmount);
        sb.append('}');
        return sb.toString();
    }
}
