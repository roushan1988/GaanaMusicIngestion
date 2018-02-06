package com.til.prime.timesSubscription.dto.external;

public class PaymentsRefundResponse extends PaymentsGenericResponse {
    protected Double refundedAmount;

    public Double getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(Double refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentsRefundResponse{");
        sb.append("refundedAmount=").append(refundedAmount);
        sb.append(", status='").append(status).append('\'');
        sb.append(", statusCode=").append(statusCode);
        sb.append(", refundedAmount=").append(refundedAmount);
        sb.append('}');
        return sb.toString();
    }
}
