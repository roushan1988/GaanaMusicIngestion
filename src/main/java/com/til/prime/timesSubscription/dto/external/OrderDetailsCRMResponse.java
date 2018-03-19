package com.til.prime.timesSubscription.dto.external;


public class OrderDetailsCRMResponse extends GenericResponse {

	OrderDetailsCRM orderDetailsCRM;

	public OrderDetailsCRM getOrderDetailsCRM() {
		return orderDetailsCRM;
	}

	public void setOrderDetailsCRM(OrderDetailsCRM orderDetailsCRM) {
		this.orderDetailsCRM = orderDetailsCRM;
	}
}
