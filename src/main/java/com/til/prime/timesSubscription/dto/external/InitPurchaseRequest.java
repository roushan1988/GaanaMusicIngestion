package com.til.prime.timesSubscription.dto.external;

import java.math.BigDecimal;

public class InitPurchaseRequest extends GenericRequest {
    protected Long planId;
    protected Long variantId;
    protected BigDecimal price;
    protected Long durationDays;
    protected String planType;
    protected String business;
    protected String channel;
    protected String platform;
    protected String checksum;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Long durationDays) {
        this.durationDays = durationDays;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InitPurchaseRequest{");
        sb.append("planId=").append(planId);
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
