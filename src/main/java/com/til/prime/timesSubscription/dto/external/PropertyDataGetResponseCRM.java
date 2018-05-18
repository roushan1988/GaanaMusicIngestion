package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.PropertyEnum;

import java.util.Map;


public class PropertyDataGetResponseCRM extends GenericResponse {
	Map<PropertyEnum, String> subscription_property_data;

	public Map<PropertyEnum, String> getSubscription_property_data() {
		return subscription_property_data;
	}

	public void setSubscription_property_data(
			Map<PropertyEnum, String> subscription_property_data) {
		this.subscription_property_data = subscription_property_data;
	}

}
