package com.til.prime.timesSubscription.dto.external;

public class CheckStatusResponse extends GenericResponse {
    private UserSubscriptionDTO userSubscriptionDTO;

    public UserSubscriptionDTO getUserSubscriptionDTO() {
        return userSubscriptionDTO;
    }

    public void setUserSubscriptionDTO(UserSubscriptionDTO userSubscriptionDTO) {
        this.userSubscriptionDTO = userSubscriptionDTO;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckStatusResponse{");
        sb.append("userSubscriptionDTO=").append(userSubscriptionDTO);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
