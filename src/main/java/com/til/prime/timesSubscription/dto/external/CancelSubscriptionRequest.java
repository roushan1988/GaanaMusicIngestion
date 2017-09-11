package com.til.prime.timesSubscription.dto.external;

public class CancelSubscriptionRequest extends GenericRequest {
    protected Long userSubscriptionId;
    protected String orderId;
    protected Long subscriptionVariantId;

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

    public Long getSubscriptionVariantId() {
        return subscriptionVariantId;
    }

    public void setSubscriptionVariantId(Long subscriptionVariantId) {
        this.subscriptionVariantId = subscriptionVariantId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CancelSubscriptionRequest{");
        sb.append("userSubscriptionId=").append(userSubscriptionId);
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", subscriptionVariantId=").append(subscriptionVariantId);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
