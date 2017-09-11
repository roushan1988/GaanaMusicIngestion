package com.til.prime.timesSubscription.dto.external;

public class PurchaseHistoryRequest extends GenericRequest {
    private String business;
    private boolean currentSubscription;

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public boolean isCurrentSubscription() {
        return currentSubscription;
    }

    public void setCurrentSubscription(boolean currentSubscription) {
        this.currentSubscription = currentSubscription;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PurchaseHistoryRequest{");
        sb.append("business='").append(business).append('\'');
        sb.append(", currentSubscription=").append(currentSubscription);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
