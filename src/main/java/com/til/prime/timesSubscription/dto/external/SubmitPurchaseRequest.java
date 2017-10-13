package com.til.prime.timesSubscription.dto.external;

import java.math.BigDecimal;

public class SubmitPurchaseRequest extends GenericRequest {
    private Long userSubscriptionId;
    private String orderId;
    private Long variantId;
    private boolean paymentSuccess;
    private String paymentMethod;
    private BigDecimal price;
    private String paymentReference;
    private boolean autoRenewal;
    private String checksum;
    private boolean autoRenewalJob = false;

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

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public boolean isPaymentSuccess() {
        return paymentSuccess;
    }

    public void setPaymentSuccess(boolean paymentSuccess) {
        this.paymentSuccess = paymentSuccess;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public boolean isAutoRenewal() {
        return autoRenewal;
    }

    public void setAutoRenewal(boolean autoRenewal) {
        this.autoRenewal = autoRenewal;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public boolean isAutoRenewalJob() {
        return autoRenewalJob;
    }

    public void setAutoRenewalJob(boolean autoRenewalJob) {
        this.autoRenewalJob = autoRenewalJob;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubmitPurchaseRequest{");
        sb.append("userSubscriptionId=").append(userSubscriptionId);
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", variantId=").append(variantId);
        sb.append(", paymentSuccess=").append(paymentSuccess);
        sb.append(", paymentMethod='").append(paymentMethod).append('\'');
        sb.append(", price=").append(price);
        sb.append(", paymentReference='").append(paymentReference).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append(", autoRenewal=").append(autoRenewal);
        sb.append(", autoRenewalJob=").append(autoRenewalJob);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
