package com.til.prime.timesSubscription.dto.external;

public class OrderDetailsRequest extends GenericRequest {
    private String orderId;

	@Override
	public String toString() {
		return "OrderDetailsRequest [orderId=" + orderId + ", checksum="
				+ checksum + "]";
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
