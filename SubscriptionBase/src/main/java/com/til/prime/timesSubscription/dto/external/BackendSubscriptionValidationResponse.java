package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.ValidationError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BackendSubscriptionValidationResponse implements Serializable {
    private Set<ValidationError> validationErrors = new HashSet<>();
    private List<BackendActivationUserDTO> failureList = new ArrayList<>();

    public Set<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Set<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<BackendActivationUserDTO> getFailureList() {
        return failureList;
    }

    public void setFailureList(List<BackendActivationUserDTO> failureList) {
        this.failureList = failureList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BackendSubscriptionValidationResponse{");
        sb.append("validationErrors=").append(validationErrors);
        sb.append(", failureList=").append(failureList);
        sb.append('}');
        return sb.toString();
    }
}
