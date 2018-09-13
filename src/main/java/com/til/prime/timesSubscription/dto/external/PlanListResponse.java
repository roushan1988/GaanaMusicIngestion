package com.til.prime.timesSubscription.dto.external;

import java.util.List;

public class PlanListResponse extends GenericResponse {
    private List<SubscriptionPlanDTO> planDTOList;
    private ValuePropDTO valueProp;

    public List<SubscriptionPlanDTO> getPlanDTOList() {
        return planDTOList;
    }

    public void setPlanDTOList(List<SubscriptionPlanDTO> planDTOList) {
        this.planDTOList = planDTOList;
    }

    public ValuePropDTO getValueProp() {
        return valueProp;
    }

    public void setValueProp(ValuePropDTO valueProp) {
        this.valueProp = valueProp;
    }

    @Override
    public String toString() {
        return "PlanListResponse{" +
                "planDTOList=" + planDTOList +
                "valueProp=" + valueProp +
                ", success=" + success +
                ", responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\'' +
                ", validationErrors=" + validationErrors +
                '}';
    }
}
