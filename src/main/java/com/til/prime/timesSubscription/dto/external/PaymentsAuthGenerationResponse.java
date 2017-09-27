package com.til.prime.timesSubscription.dto.external;

public class PaymentsAuthGenerationResponse extends PaymentsGenericResponse {
    private AuthDTO data;

    public AuthDTO getData() {
        return data;
    }

    public void setData(AuthDTO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentsAuthGenerationResponse{");
        sb.append("status='").append(status).append('\'');
        sb.append(", statusCode=").append(statusCode);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
