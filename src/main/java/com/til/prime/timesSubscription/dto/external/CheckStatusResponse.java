package com.til.prime.timesSubscription.dto.external;

public class CheckStatusResponse extends GenericResponse {
    private boolean timesPrimeUser;
    private SubscriptionStatusDTO subscriptionStatusDTO;

    public boolean isTimesPrimeUser() {
        return timesPrimeUser;
    }

    public void setTimesPrimeUser(boolean timesPrimeUser) {
        this.timesPrimeUser = timesPrimeUser;
    }

    public SubscriptionStatusDTO getSubscriptionStatusDTO() {
        return subscriptionStatusDTO;
    }

    public void setSubscriptionStatusDTO(SubscriptionStatusDTO subscriptionStatusDTO) {
        this.subscriptionStatusDTO = subscriptionStatusDTO;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckStatusResponse{");
        sb.append("timesPrimeUser=").append(timesPrimeUser);
        sb.append(", subscriptionStatusDTO=").append(subscriptionStatusDTO);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrorCategory=").append(validationErrorCategory);
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
