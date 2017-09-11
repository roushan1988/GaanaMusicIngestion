package com.til.prime.timesSubscription.dto.external;

import java.math.BigDecimal;
import java.util.Date;

public class UserSubscriptionDTO {
    private Long userSubscriptionId;
    private Long subscriptionVariantId;
    private String orderId;
    private String name;
    private String planType;
    private Long durationDays;
    private BigDecimal price;
    private Date startDate;
    private Date endDate;
    private String planStatus;
    private String transactionStatus;
    private String business;
    private String country;
    private String currency;

    public Long getUserSubscriptionId() {
        return userSubscriptionId;
    }

    public void setUserSubscriptionId(Long userSubscriptionId) {
        this.userSubscriptionId = userSubscriptionId;
    }

    public Long getSubscriptionVariantId() {
        return subscriptionVariantId;
    }

    public void setSubscriptionVariantId(Long subscriptionVariantId) {
        this.subscriptionVariantId = subscriptionVariantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Long getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Long durationDays) {
        this.durationDays = durationDays;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserSubscriptionDTO{");
        sb.append("orderId='").append(orderId).append('\'');
        sb.append(", userSubscriptionId=").append(userSubscriptionId);
        sb.append(", variantId=").append(subscriptionVariantId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", planType='").append(planType).append('\'');
        sb.append(", durationDays=").append(durationDays);
        sb.append(", price=").append(price);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", planStatus='").append(planStatus).append('\'');
        sb.append(", transactionStatus='").append(transactionStatus).append('\'');
        sb.append(", business='").append(business).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
