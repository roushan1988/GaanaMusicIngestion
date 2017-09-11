package com.til.prime.timesSubscription.enums;

public enum ValidationError {
	INVALID_ID(1, "Invalid ID"),
	INVALID_BUSINESS(2, "Invalid Business"),
	INVALID_COUNTRY(3, "Invalid Country"),
	INVALID_SSO_ID(4, "Invalid SSO Id"),
	INVALID_TICKET_ID(5, "Invalid Ticket Id"),
	INVALID_SSO_CREDENTIALS(6, "Invalid SSO Credentials"),
	INVALID_PLAN_ID(7, "Invalid Plan Id"),
	INVALID_VARIANT_ID(8, "Invalid Variant Id"),
	INVALID_PRICE(9, "Invalid Price"),
	INVALID_DURATION_DAYS(10, "Invalid Duration days"),
	INVALID_SUBSCRIPTION_VARIANT(11, "Invalid Subcription Variant"),
	INVALID_ORDER_ID(12, "Invalid Order Id"),
	INVALID_PAYMENT_DETAILS(13, "Invalid Payment Details"),
	INVALID_USER_SUBSCRIPTION_DETAILS(14, "Invalid User Subscription Details"),
	ORDER_ALREADY_COMPLETED(15, "Order Already Completed"),
	INVALID_PLAN_TYPE(16, "Invalid Plan Type"),
	USER_PLAN_DOES_NOT_QUALIFY(17, "User doesn't qualify for plan"),
	INVALID_USER_SUBSCRIPTION_ID(18, "Invalid User Subscription Id"),
	INVALID_PAYMENT_METHOD(19, "Invalid Payment Method"),
	INVALID_PAYMENT_REFERENCE(20, "Invalid Payment Reference"),
	INVALID_RECURRING_PAYMENT(21, "Invalid Recurring Payment"),
	INVALID_CHANNEL(22, "Invalid Channel"),
	INVALID_PLATFORM(23, "Invalid Platform"),
	INVALID_SECRET_KEY(24, "Invalid Secret Key"),
	ALREADY_EXPIRED(25, "Already Expired"),
	INVALID_EXTENSION_DAYS(26, "Invalid Extension Days"),
	INVALID_MOBILE(27, "Invalid Mobile"),
	INVALID_EMAIL(28, "Invalid Email"),
	ORDER_ALREADY_GENERATED(29, "Order already generated"),
	ORDER_NOT_GENERATED(30, "Order not generated"),
	USER_DETAILS_MISSING(31, "User details missing"),
	;

	private Integer errorCode;
	private String errorMessage;

	ValidationError(Integer errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ValidationError{");
		sb.append("errorCode=").append(errorCode);
		sb.append(", errorMessage='").append(errorMessage).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
