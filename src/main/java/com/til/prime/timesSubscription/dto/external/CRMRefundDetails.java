package com.til.prime.timesSubscription.dto.external;

import java.sql.Timestamp;


public class CRMRefundDetails {

	private String refundId;
	private Timestamp refundPlacedDate;
	double refundAmount;
	String refundStatus;
	String paymentProvider;
	public String getRefundId() {
		return refundId;
	}
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	public Timestamp getRefundPlacedDate() {
		return refundPlacedDate;
	}
	public void setRefundPlacedDate(Timestamp refundPlacedDate) {
		this.refundPlacedDate = refundPlacedDate;
	}
	public double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getPaymentProvider() {
		return paymentProvider;
	}
	public void setPaymentProvider(String paymentProvider) {
		this.paymentProvider = paymentProvider;
	}
}
