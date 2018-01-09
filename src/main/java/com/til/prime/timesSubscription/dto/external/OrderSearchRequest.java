package com.til.prime.timesSubscription.dto.external;

import java.util.Date;

public class OrderSearchRequest extends GenericRequest {
	private String orderId;
    private String checksum;
    private String subscriptionStatus;
    private String start;
    private Integer count;
    private Date fromDate;
    private Date toDate;
    
    public String getSubscriptionStatus() {
		return subscriptionStatus;
	}

	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("OrderSearchRequest{");
		sb.append("orderId='").append(orderId).append('\'');
		sb.append(", checksum='").append(checksum).append('\'');
		sb.append(", subscriptionStatus='").append(subscriptionStatus).append('\'');
		sb.append(", start='").append(start).append('\'');
		sb.append(", count=").append(count);
		sb.append(", fromDate=").append(fromDate);
		sb.append(", toDate=").append(toDate);
		sb.append(", user=").append(user);
		sb.append(", secretKey='").append(secretKey).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
