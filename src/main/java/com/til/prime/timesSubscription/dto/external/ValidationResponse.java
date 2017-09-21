package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.ValidationError;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ValidationResponse implements Serializable {
	private boolean valid = false;
	private Set<ValidationError> validationErrorSet = new HashSet<ValidationError>();
	private Integer maxCategory = 0;

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

	public Integer getMaxCategory() {
		return maxCategory;
	}

	public void setMaxCategory(Integer maxCategory) {
		this.maxCategory = maxCategory;
	}

	public void addValidationError(ValidationError validationError){
		validationErrorSet.add(validationError);
		if(validationError.getCategory()>maxCategory){
			maxCategory=validationError.getCategory();
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ValidationResponse{");
		sb.append("valid=").append(valid);
		sb.append(", validationErrorSet=").append(validationErrorSet);
		sb.append(", maxCategory=").append(maxCategory);
		sb.append('}');
		return sb.toString();
	}
}
