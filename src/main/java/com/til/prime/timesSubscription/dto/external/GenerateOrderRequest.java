package com.til.prime.timesSubscription.dto.external;

import java.math.BigDecimal;

public class GenerateOrderRequest extends GenericRequest {
    private Long userSubscriptionId;
    private Long planId;
    private Long variantId;
    private BigDecimal price;
    private Long durationDays;
    private String planType;
    private String business;
    private boolean retryOnFailure;
    private boolean renewal;
    private boolean duplicate;
    private boolean job;
    private String platform;
    private Double pgAmount = 0d;
    private String pgMethod;
    private String promoCode;
    private Double promoAmount = 0d;
    private Double tpAmount = 0d;

    public Long getUserSubscriptionId() {
        return userSubscriptionId;
    }

    public void setUserSubscriptionId(Long userSubscriptionId) {
        this.userSubscriptionId = userSubscriptionId;
    }

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

    public boolean isRetryOnFailure() {
        return retryOnFailure;
    }

    public void setRetryOnFailure(boolean retryOnFailure) {
        this.retryOnFailure = retryOnFailure;
    }

    public boolean isRenewal() {
        return renewal;
    }

    public void setRenewal(boolean renewal) {
        this.renewal = renewal;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public boolean isJob() {
        return job;
    }

    public void setJob(boolean job) {
        this.job = job;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Double getPgAmount() {
        return pgAmount;
    }

    public void setPgAmount(Double pgAmount) {
        this.pgAmount = pgAmount;
    }

    public String getPgMethod() {
        return pgMethod;
    }

    public void setPgMethod(String pgMethod) {
        this.pgMethod = pgMethod;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public Double getPromoAmount() {
        return promoAmount;
    }

    public void setPromoAmount(Double promoAmount) {
        this.promoAmount = promoAmount;
    }

    public Double getTpAmount() {
        return tpAmount;
    }

    public void setTpAmount(Double tpAmount) {
        this.tpAmount = tpAmount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GenerateOrderRequest{");
        sb.append("userSubscriptionId=").append(userSubscriptionId);
        sb.append(", planId=").append(planId);
        sb.append(", variantId=").append(variantId);
        sb.append(", price=").append(price);
        sb.append(", durationDays=").append(durationDays);
        sb.append(", planType='").append(planType).append('\'');
        sb.append(", business='").append(business).append('\'');
        sb.append(", retryOnFailure=").append(retryOnFailure);
        sb.append(", renewal=").append(renewal);
        sb.append(", duplicate=").append(duplicate);
        sb.append(", job=").append(job);
        sb.append(", platform='").append(platform).append('\'');
        sb.append(", pgAmount=").append(pgAmount);
        sb.append(", pgMethod='").append(pgMethod).append('\'');
        sb.append(", promoCode='").append(promoCode).append('\'');
        sb.append(", promoAmount=").append(promoAmount);
        sb.append(", tpAmount=").append(tpAmount);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
