package com.til.prime.timesSubscription.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* Highest priority messages and category are shown in response */
public enum ValidationError {
	INVALID_ID(1, "Invalid ID", 1, 1),
	INVALID_BUSINESS(2, "Oops! Something went wrong. Please try again.", 1, 1),
	INVALID_COUNTRY(3, "Oops! Something went wrong. Please try again.", 1, 1),
	INVALID_SSO_ID(4, "Oops! Something went wrong. Please login again.", 1, 2),
	INVALID_TICKET_ID(5, "Oops! Something went wrong. Please login again.", 1, 2),
	INVALID_SSO_CREDENTIALS(6, "Oops! Something went wrong. Please login again.", 1, 2),
	INVALID_PLAN_ID(7, "Oops! Something went wrong. Please try again.", 1, 1),
	INVALID_VARIANT_ID(8, "Oops! Something went wrong. Please try again.", 1, 1),
	INVALID_PRICE(9, "Oops! Something went wrong. Please try again.", 1, 1),
	INVALID_DURATION_DAYS(10, "Oops! Something went wrong. Please try again.", 1, 1),
	INVALID_SUBSCRIPTION_VARIANT(11, "Oops! Something went wrong. Please try again.", 1, 1),
	INVALID_ORDER_ID(12, "Invalid Order Id", 1, 1),
	INVALID_PAYMENT_DETAILS(13, "Invalid Payment Details", 1, 1),
	INVALID_USER_SUBSCRIPTION_DETAILS(14, "Invalid User Subscription Details", 1, 1),
	ORDER_ALREADY_COMPLETED(15, "Order Already Completed", 1, 1),
	INVALID_PLAN_TYPE(16, "Oops! Something went wrong. Please try again.", 1, 1),
	USER_PLAN_DOES_NOT_QUALIFY(17, "Oops! Something went wrong. Please try again.", 2, 1),
	OTP_SENDING_ERROR(17, "Oops! Something went wrong. Please try again.", 2, 1),
	OTP_VERIFICATION_ERROR(17, "Oops! Something went wrong. Please try again.", 2, 1),
	INVALID_USER_SUBSCRIPTION_ID(18, "Invalid User Subscription Id", 1, 1),
	INVALID_PAYMENT_METHOD(19, "Invalid Payment Method", 1, 1),
	INVALID_PAYMENT_REFERENCE(20, "Invalid Payment Reference", 1, 1),
	INVALID_RECURRING_PAYMENT(21, "Invalid Recurring Payment", 1, 1),
	INVALID_CHANNEL(22, "Oops! Something went wrong. Please try again.", 1, 1),
	INVALID_PLATFORM(23, "Oops! Something went wrong. Please try again.", 1, 1),
	INVALID_SECRET_KEY(24, "Invalid Secret Key", 1, 1),
	ALREADY_EXPIRED(25, "Already Expired", 1, 1),
	INVALID_EXTENSION_DAYS(26, "Invalid Extension Days", 1, 1),
	INVALID_MOBILE(27, "The mobile number you entered is invalid. Please enter a valid mobile number.", 1, 4),
	INVALID_EMAIL(28, "The email ID you entered is invalid. Please enter a valid email ID.", 1, 3),
	ORDER_ALREADY_GENERATED(29, "Order already generated", 1, 1),
	ORDER_NOT_GENERATED(30, "Order not generated", 1, 1),
	USER_DETAILS_MISSING(31, "Oops! Something went wrong. Please login again.", 1, 2),
	INVALID_TRANSACTION_STATUS(33, "Invalid Transaction Status", 1, 1),
	PAYMENT_REFUND_ERROR(34, "Payment Refund error", 1, 1),
	INVALID_VARIANT_NAME(35, "Invalid Variant Name", 1, 1),
	NO_SUBSCRIPTIONS_FOUND(36, "No subscriptions found", 1, 1),
	ALREADY_BLOCKED_USER(37, "User already blocked", 1, 3),
	ALREADY_UNBLOCKED_USER(38, "User already unblocked", 1, 3),
	INVALID_SUBSCRIPTION_STATUS(39, "Invalid Subscription Status", 1, 1),
	RECORD_ALREADY_EXISTS_AND_NOT_ACTIVATED(39, "Record already exists and has not been activated", 1, 1),
	FUTURE_SUBSCRIPTION_EXISTS_FOR_USER(40, "Future subscription exists for user", 1, 1),
	INVALID_RECORD(41, "Invalid Record", 1, 1),
	EMPTY_BACKEND_USER_LIST(42, "Empty backend user list", 1, 1),
	USER_SUBSCRIPTION_EXPIRED(43, "User Subscription expired", 1, 1),
	INVALID_USER(1000, "Invalid User", 1, 1),
	INVALID_CLIENT_ID(1001, "Invalid Client ID", 1, 1),
	INVALID_CLIENT_SECRET_KEY(1002, "Invalid Client Secret Key", 1, 1),
	INVALID_CHECKSUM(1003, "Invalid Checksum", 1, 1),
	INVALID_ENCRYPTION(1004, "Invalid Encryption", 1, 1),
	BLOCKED_USER(1005, "Oops! Your account is temporarily suspended.", 1, 3),
	INVALID_ACTIVATION_CODE(1006, "Oops! Your activation code is invalid.", 1, 3),
	USER_ALREADY_PURCHASED_MULTIPLE_FUTURE_PLANS(1007, "Don't worry! We've got you covered for more than 2 years, Please continue enjoying all your offers and privileges.", 3, 3),
	INVALID_OTP(1007, "Please enter correct otp.", 1, 2),
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

	private final Integer errorCode;
	private final String errorMessage;
	private final Integer category;
	private final Integer messagePriority;

	ValidationError(Integer errorCode, String errorMessage, Integer category, Integer messagePriority) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.category = category;
		this.messagePriority = messagePriority;
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

	public Integer getMessagePriority() {
		return messagePriority;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ValidationError{");
		sb.append("errorCode=").append(errorCode);
		sb.append(", errorMessage='").append(errorMessage).append('\'');
		sb.append(", category=").append(category);
		sb.append(", messagePriority=").append(messagePriority);
		sb.append('}');
		return sb.toString();
	}
}
