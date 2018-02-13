package com.til.prime.timesSubscription.dto.external;

public class PaymentsRefundRequest {
    private String orderId;
    private double amount;
    private boolean forceAmount;
    private String secretKey;
    private String requestHash;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isForceAmount() {
        return forceAmount;
    }

    public void setForceAmount(boolean forceAmount) {
        this.forceAmount = forceAmount;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RefundRequest{");
        sb.append("orderId='").append(orderId).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", forceAmount=").append(forceAmount);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append(", requestHash='").append(requestHash).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
