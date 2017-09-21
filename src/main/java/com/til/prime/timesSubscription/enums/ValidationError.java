package com.til.prime.timesSubscription.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum ValidationError {
	INVALID_ID(1, "Invalid ID", 1),
	INVALID_BUSINESS(2, "Invalid Business", 1),
	INVALID_COUNTRY(3, "Invalid Country", 1),
	INVALID_SSO_ID(4, "Invalid SSO Id", 1),
	INVALID_TICKET_ID(5, "Invalid Ticket Id", 1),
	INVALID_SSO_CREDENTIALS(6, "Invalid SSO Credentials", 1),
	INVALID_PLAN_ID(7, "Invalid Plan Id", 1),
	INVALID_VARIANT_ID(8, "Invalid Variant Id", 1),
	INVALID_PRICE(9, "Invalid Price", 1),
	INVALID_DURATION_DAYS(10, "Invalid Duration days", 1),
	INVALID_SUBSCRIPTION_VARIANT(11, "Invalid Subcription Variant", 1),
	INVALID_ORDER_ID(12, "Invalid Order Id", 1),
	INVALID_PAYMENT_DETAILS(13, "Invalid Payment Details", 1),
	INVALID_USER_SUBSCRIPTION_DETAILS(14, "Invalid User Subscription Details", 1),
	ORDER_ALREADY_COMPLETED(15, "Order Already Completed", 1),
	INVALID_PLAN_TYPE(16, "Invalid Plan Type", 1),
	USER_PLAN_DOES_NOT_QUALIFY(17, "User doesn't qualify for plan", 2),
	INVALID_USER_SUBSCRIPTION_ID(18, "Invalid User Subscription Id", 1),
	INVALID_PAYMENT_METHOD(19, "Invalid Payment Method", 1),
	INVALID_PAYMENT_REFERENCE(20, "Invalid Payment Reference", 1),
	INVALID_RECURRING_PAYMENT(21, "Invalid Recurring Payment", 1),
	INVALID_CHANNEL(22, "Invalid Channel", 1),
	INVALID_PLATFORM(23, "Invalid Platform", 1),
	INVALID_SECRET_KEY(24, "Invalid Secret Key", 1),
	ALREADY_EXPIRED(25, "Already Expired", 1),
	INVALID_EXTENSION_DAYS(26, "Invalid Extension Days", 1),
	INVALID_MOBILE(27, "Invalid Mobile", 1),
	INVALID_EMAIL(28, "Invalid Email", 1),
	ORDER_ALREADY_GENERATED(29, "Order already generated", 1),
	ORDER_NOT_GENERATED(30, "Order not generated", 1),
	USER_DETAILS_MISSING(31, "User details missing", 1),
	INVALID_ENCRYPTION(32, "Invalid Encryption", 1),
	;

	public static final Integer maxCategory = 2;

	private static final Map<Integer, Set<ValidationError>> categorySetMap = new HashMap<>();

	static {
		for(ValidationError validationError: ValidationError.values()){
			if(categorySetMap.get(validationError.category)==null){
				categorySetMap.put(validationError.category, new HashSet<>());
			}
			categorySetMap.get(validationError.category).add(validationError);
		}
	}

	private Integer errorCode;
	private String errorMessage;
	private Integer category;

	ValidationError(Integer errorCode, String errorMessage, Integer category) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.category = category;
	}

	public static Set<ValidationError> getErrorSet(Integer category){
		return categorySetMap.get(category);
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Integer getCategory() {
		return category;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ValidationError{");
		sb.append("errorCode=").append(errorCode);
		sb.append(", errorMessage='").append(errorMessage).append('\'');
		sb.append(", category=").append(category);
		sb.append('}');
		return sb.toString();
	}
}
