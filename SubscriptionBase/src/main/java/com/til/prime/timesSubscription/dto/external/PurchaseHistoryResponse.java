package com.til.prime.timesSubscription.dto.external;

import java.util.List;

public class PurchaseHistoryResponse extends GenericResponse {
    private List<UserSubscriptionDTO> userSubscriptionDTOList;

    public List<UserSubscriptionDTO> getUserSubscriptionDTOList() {
        return userSubscriptionDTOList;
    }

    public void setUserSubscriptionDTOList(List<UserSubscriptionDTO> userSubscriptionDTOList) {
        this.userSubscriptionDTOList = userSubscriptionDTOList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PurchaseHistoryResponse{");
        sb.append("userSubscriptionDTOList=").append(userSubscriptionDTOList);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
