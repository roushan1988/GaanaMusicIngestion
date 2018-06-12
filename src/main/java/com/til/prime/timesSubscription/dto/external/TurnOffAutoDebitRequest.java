package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.BusinessEnum;

public class TurnOffAutoDebitRequest extends GenericRequest {
    private BusinessEnum business = BusinessEnum.TIMES_PRIME;

    public BusinessEnum getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEnum business) {
        this.business = business;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TurnOffAutoDebitRequest{");
        sb.append("checksum='").append(checksum).append('\'');
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append(", business=").append(business);
        sb.append('}');
        return sb.toString();
    }
}
