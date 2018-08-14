package com.til.prime.timesSubscription.dto.external;

public class SubmitPurchaseRequest extends GenericRequest {
    private Long userSubscriptionId;
    private String orderId;
    private Long variantId;
    private Double price;
    private boolean paymentSuccess;
    private Double pgAmount = 0d;
    private String pgMethod;
    private String pgReference;
    private String promoCode;
    private Double promoAmount = 0d;
    private Double tpAmount = 0d;
    private String tpReference;
    private boolean autoRenewal;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isPaymentSuccess() {
        return paymentSuccess;
    }

    public void setPaymentSuccess(boolean paymentSuccess) {
        this.paymentSuccess = paymentSuccess;
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

    public String getPgReference() {
        return pgReference;
    }

    public void setPgReference(String pgReference) {
        this.pgReference = pgReference;
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

    public String getTpReference() {
        return tpReference;
    }

    public void setTpReference(String tpReference) {
        this.tpReference = tpReference;
    }

    public boolean isAutoRenewal() {
        return autoRenewal;
    }

    public void setAutoRenewal(boolean autoRenewal) {
        this.autoRenewal = autoRenewal;
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
        sb.append(", price=").append(price);
        sb.append(", paymentSuccess=").append(paymentSuccess);
        sb.append(", pgAmount=").append(pgAmount);
        sb.append(", pgMethod='").append(pgMethod).append('\'');
        sb.append(", pgReference='").append(pgReference).append('\'');
        sb.append(", promoCode='").append(promoCode).append('\'');
        sb.append(", promoAmount=").append(promoAmount);
        sb.append(", tpAmount=").append(tpAmount);
        sb.append(", tpReference='").append(tpReference).append('\'');
        sb.append(", autoRenewal=").append(autoRenewal);
        sb.append(", autoRenewalJob=").append(autoRenewalJob);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
