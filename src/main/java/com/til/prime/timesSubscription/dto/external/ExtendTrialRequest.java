package com.til.prime.timesSubscription.dto.external;

public class ExtendTrialRequest extends CancelSubscriptionRequest {
    private Long extensionDays;

    public Long getExtensionDays() {
        return extensionDays;
    }

    public void setExtensionDays(Long extensionDays) {
        this.extensionDays = extensionDays;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExtendTrialRequest{");
        sb.append("extensionDays=").append(extensionDays);
        sb.append(", userSubscriptionId=").append(userSubscriptionId);
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", subscriptionVariantId=").append(subscriptionVariantId);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
