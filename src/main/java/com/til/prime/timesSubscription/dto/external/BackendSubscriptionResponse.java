package com.til.prime.timesSubscription.dto.external;

import java.util.List;

public class BackendSubscriptionResponse extends GenericResponse {
    private List<BackendActivationUserDTO> successList;
    private List<BackendActivationUserDTO> failureList;

    public List<BackendActivationUserDTO> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<BackendActivationUserDTO> successList) {
        this.successList = successList;
    }

    public List<BackendActivationUserDTO> getFailureList() {
        return failureList;
    }

    public void setFailureList(List<BackendActivationUserDTO> failureList) {
        this.failureList = failureList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BackendSubscriptionResponse{");
        sb.append("successList=").append(successList);
        sb.append(", failureList=").append(failureList);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrorCategory=").append(validationErrorCategory);
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
