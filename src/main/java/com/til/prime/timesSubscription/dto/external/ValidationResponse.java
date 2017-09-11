package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.ValidationError;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ValidationResponse implements Serializable {
	private boolean valid = false;
	private Set<ValidationError> validationErrorSet = new HashSet<ValidationError>();


	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Set<ValidationError> getValidationErrorSet() {
		return validationErrorSet;
	}

	public void setValidationErrorSet(Set<ValidationError> validationErrorSet) {
		this.validationErrorSet = validationErrorSet;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ValidationResponse{");
		sb.append("valid=").append(valid);
		sb.append(", validationErrorSet=").append(validationErrorSet);
		sb.append('}');
		return sb.toString();
	}
}
