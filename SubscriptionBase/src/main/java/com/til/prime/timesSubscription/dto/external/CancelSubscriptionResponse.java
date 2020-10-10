package com.til.prime.timesSubscription.dto.external;

import java.math.BigDecimal;

public class CancelSubscriptionResponse extends GenericResponse {
    private BigDecimal refundAmount;

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CancelSubscriptionResponse{");
        sb.append("refundAmount=").append(refundAmount);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
