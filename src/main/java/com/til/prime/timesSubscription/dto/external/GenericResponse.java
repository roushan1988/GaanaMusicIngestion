package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.ResponseCode;
import com.til.prime.timesSubscription.enums.ValidationError;

import java.util.ArrayList;
import java.util.List;

public class GenericResponse {
	private static final long serialVersionUID = 1l;

	protected Boolean success=false;
	protected ResponseCode responseCode;
	protected String responseMessage;
	protected List<ValidationError> validationErrors= new ArrayList<ValidationError>();

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public ResponseCode getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<ValidationError> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(List<ValidationError> validationErrors) {
		this.validationErrors = validationErrors;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("CouponResponse{");
		sb.append("success=").append(success);
		sb.append(", responseCode=").append(responseCode);
		sb.append(", responseMessage='").append(responseMessage).append('\'');
		sb.append(", validationErrors=").append(validationErrors);
		sb.append('}');
		return sb.toString();
	}
}
