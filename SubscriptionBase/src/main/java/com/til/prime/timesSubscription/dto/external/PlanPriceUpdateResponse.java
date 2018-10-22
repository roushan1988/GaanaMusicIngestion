package com.til.prime.timesSubscription.dto.external;

import java.math.BigDecimal;

public class PlanPriceUpdateResponse extends GenericResponse {
    private BigDecimal oldPrice;

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanPriceUpdateResponse{");
        sb.append("oldPrice=").append(oldPrice);
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrorCategory=").append(validationErrorCategory);
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
