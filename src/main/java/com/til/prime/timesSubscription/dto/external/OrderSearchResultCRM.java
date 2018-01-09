package com.til.prime.timesSubscription.dto.external;

import java.util.Date;


public class OrderSearchResultCRM {
	
	private String orderId;
	private String orderDetail;
	private Date orderDate;
	private Double amount;
	private String currentStatus;
	private Date expiryDate;
	private boolean isAutoRenewal;
	
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
		return isAutoRenewal;
	}
	public void setAutoRenewal(boolean isAutoRenewal) {
		this.isAutoRenewal = isAutoRenewal;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
}
