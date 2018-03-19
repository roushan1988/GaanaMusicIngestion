package com.til.prime.timesSubscription.dto.external;



public class CustomerSearchCRMResponse extends GenericResponse {

	CustomerSearchDTOs customerSearchDTOs;

	public CustomerSearchDTOs getCustomerSearchDTOs() {
		return customerSearchDTOs;
	}

	public void setCustomerSearchDTOs(CustomerSearchDTOs customerSearchDTOs) {
		this.customerSearchDTOs = customerSearchDTOs;
	}
}
