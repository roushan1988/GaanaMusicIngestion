package com.til.prime.timesSubscription.dto.external;

public class OrderDetailsRequest extends GenericRequest {
    private String orderId;
    private String checksum;
    
    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

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
