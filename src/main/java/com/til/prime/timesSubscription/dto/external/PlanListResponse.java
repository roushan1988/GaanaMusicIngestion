package com.til.prime.timesSubscription.dto.external;

import java.util.List;

public class PlanListResponse extends GenericResponse {
    private List<SubscriptionPlanDTO> planDTOList;

    public List<SubscriptionPlanDTO> getPlanDTOList() {
        return planDTOList;
    }

    public void setPlanDTOList(List<SubscriptionPlanDTO> planDTOList) {
        this.planDTOList = planDTOList;
    }

    @Override
    public String toString() {
        return "PlanListResponse{" +
                "planDTOList=" + planDTOList +
                ", success=" + success +
                ", responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\'' +
                ", validationErrors=" + validationErrors +
                '}';
    }
}
