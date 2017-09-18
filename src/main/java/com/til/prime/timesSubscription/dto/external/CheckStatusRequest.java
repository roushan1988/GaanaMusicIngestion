package com.til.prime.timesSubscription.dto.external;

public class CheckStatusRequest extends GenericRequest {
    private Long userSubscriptionId;
    private Long variantId;
    private String orderId;
    private String checksum;

    public Long getUserSubscriptionId() {
        return userSubscriptionId;
    }

    public void setUserSubscriptionId(Long userSubscriptionId) {
        this.userSubscriptionId = userSubscriptionId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckStatusRequest{");
        sb.append("userSubscriptionId=").append(userSubscriptionId);
        sb.append(", variantId=").append(variantId);
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
