package com.til.prime.timesSubscription.dto.external;

public class CRMInitPurchaseRequest extends InitPurchaseRequest {
    private boolean free;

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CRMInitPurchaseRequest{");
        sb.append("free=").append(free);
        sb.append(", planId=").append(planId);
        sb.append(", variantId=").append(variantId);
        sb.append(", price=").append(price);
        sb.append(", durationDays=").append(durationDays);
        sb.append(", planType='").append(planType).append('\'');
        sb.append(", business='").append(business).append('\'');
        sb.append(", channel='").append(channel).append('\'');
        sb.append(", platform='").append(platform).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
