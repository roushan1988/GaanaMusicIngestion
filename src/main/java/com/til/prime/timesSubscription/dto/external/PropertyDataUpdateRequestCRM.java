package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.PropertyEnum;

public class PropertyDataUpdateRequestCRM extends GenericRequest {


	private String tableName;
	private String checksum;
	private PropertyEnum key;
	private String value;
	
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public PropertyEnum getKey() {
		return key;
	}

	public void setKey(PropertyEnum key) {
		this.key = key;
	}



	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
