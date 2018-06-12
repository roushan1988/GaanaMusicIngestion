package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.BusinessEnum;

public class CheckStatusRequest extends GenericRequest {
    private String clientId;
    private BusinessEnum business = BusinessEnum.TIMES_PRIME;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public BusinessEnum getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEnum business) {
        this.business = business;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckStatusRequest{");
        sb.append("clientId='").append(clientId).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append(", business=").append(business);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
