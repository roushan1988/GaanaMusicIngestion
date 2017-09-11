package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.BusinessEnum;

public class SubscriptionOfferDTO {
    private String channel;
    private boolean thirdParty;
    private String offering;
    private Long quantity;

    public SubscriptionOfferDTO(String channel, boolean thirdParty, String offering, Long quantity) {
        this.channel = channel;
        this.thirdParty = thirdParty;
        this.offering = offering;
        this.quantity = quantity;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(boolean thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getOffering() {
        return offering;
    }

    public void setOffering(String offering) {
        this.offering = offering;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionOfferDTO{");
        sb.append("channel='").append(channel).append('\'');
        sb.append(", thirdParty=").append(thirdParty);
        sb.append(", offering='").append(offering).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }
}
