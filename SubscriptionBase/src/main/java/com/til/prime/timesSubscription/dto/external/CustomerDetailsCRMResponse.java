package com.til.prime.timesSubscription.dto.external;



public class CustomerDetailsCRMResponse extends GenericResponse {

	CustomerCRM customerCRM;

	public CustomerCRM getCustomerCRM() {
		return customerCRM;
	}

	public void setCustomerCRM(CustomerCRM customerCRM) {
		this.customerCRM = customerCRM;
	}
}
