package com.til.prime.timesSubscription.dto.external;

public class PropertyDataRequestCRM extends GenericRequest {

	private String tableName;
	private String checksum;

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
}
