package com.til.prime.timesSubscription.dto.external;

import java.util.Map;

import com.til.prime.timesSubscription.enums.PropertyEnum;


public class PropertyDataGetResponseCRM extends GenericResponse {
	Map<PropertyEnum, Object> subscription_property_data;

	public Map<PropertyEnum, Object> getSubscription_property_data() {
		return subscription_property_data;
	}

	public void setSubscription_property_data(
			Map<PropertyEnum, Object> subscription_property_data) {
		this.subscription_property_data = subscription_property_data;
	}

}
