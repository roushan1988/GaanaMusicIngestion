package com.til.prime.timesSubscription.dto.external;

public class CancelSubscriptionServerRequest extends CancelSubscriptionRequest {
    private Double refundAmount;
    private boolean refund;

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public boolean isRefund() {
        return refund;
    }

    public void setRefund(boolean refund) {
        this.refund = refund;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CancelSubscriptionServerRequest{");
        sb.append("checksum='").append(checksum).append('\'');
        sb.append(", refundAmount=").append(refundAmount);
        sb.append(", refund=").append(refund);
        sb.append(", userSubscriptionId=").append(userSubscriptionId);
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", variantId=").append(variantId);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
