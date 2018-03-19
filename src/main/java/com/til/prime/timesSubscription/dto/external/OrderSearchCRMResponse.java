package com.til.prime.timesSubscription.dto.external;


public class OrderSearchCRMResponse extends GenericResponse {
    private OrderSearchResultsCRM orderSearchResultsCRM;

	public OrderSearchResultsCRM getOrderSearchResultsCRM() {
		return orderSearchResultsCRM;
	}

	public void setOrderSearchResultsCRM(OrderSearchResultsCRM orderSearchResultsCRM) {
		this.orderSearchResultsCRM = orderSearchResultsCRM;
	}
}
