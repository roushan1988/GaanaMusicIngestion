package com.til.prime.timesSubscription.dto.external;

import java.util.List;

public class PlanListResponse extends GenericResponse {
    private List<SubscriptionPlanDTO> planDTOList;
    private boolean freeTrialEnded;

    public List<SubscriptionPlanDTO> getPlanDTOList() {
        return planDTOList;
    }

    public void setPlanDTOList(List<SubscriptionPlanDTO> planDTOList) {
        this.planDTOList = planDTOList;
    }

    public boolean isFreeTrialEnded() {
        return freeTrialEnded;
    }

    public void setFreeTrialEnded(boolean freeTrialEnded) {
        this.freeTrialEnded = freeTrialEnded;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanListResponse{");
        sb.append("planDTOList=").append(planDTOList);
        sb.append(", freeTrialEnded=").append(freeTrialEnded);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrorCategory=").append(validationErrorCategory);
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
