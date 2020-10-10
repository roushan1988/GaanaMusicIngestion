package com.til.prime.timesSubscription.dto.external;

import java.util.Date;


public class OrderSearchResultCRM {
	
	private String orderId;
	private String orderDetail;
	private String customerMobile;
	private String customerName;
	private Date orderDateVal;
	private Double amount;
	private Date expiryDateVal;
	private boolean autoRenewal;
	private boolean orderCompleted;
	private boolean freeTrial;
	private String subscriptionStatus;
	private String subscriptionTransactionStatus;
	private String paymentStatus;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}
	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public boolean isAutoRenewal() {
		return autoRenewal;
	}
	public void setAutoRenewal(boolean autoRenewal) {
		this.autoRenewal = autoRenewal;
	}
	public Date getOrderDateVal() {
		return orderDateVal;
	}
	public void setOrderDateVal(Date orderDateVal) {
		this.orderDateVal = orderDateVal;
	}
	public Date getExpiryDateVal() {
		return expiryDateVal;
	}
	public void setExpiryDateVal(Date expiryDateVal) {
		this.expiryDateVal = expiryDateVal;
	}

    public boolean isOrderCompleted() {
        return orderCompleted;
    }

    public void setOrderCompleted(boolean orderCompleted) {
        this.orderCompleted = orderCompleted;
    }

	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}
	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}
	public String getSubscriptionTransactionStatus() {
		return subscriptionTransactionStatus;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public void setSubscriptionTransactionStatus(
			String subscriptionTransactionStatus) {
		this.subscriptionTransactionStatus = subscriptionTransactionStatus;
	}

	public boolean isFreeTrial() {
		return freeTrial;
	}
	public void setFreeTrial(boolean freeTrial) {
		this.freeTrial = freeTrial;
	}
}