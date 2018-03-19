package com.til.prime.timesSubscription.dto.external;

import java.util.Date;


public class OrderSearchResultCRM {
	
	private String orderId;
	private String orderDetail;
	private Date orderDateVal;
	private Double amount;
	private String currentStatus;
	private Date expiryDateVal;
	private boolean autoRenewal;
	
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


	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
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

}
