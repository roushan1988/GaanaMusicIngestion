package com.til.prime.timesSubscription.dto.external;

public class PaymentsGenericResponse {
    protected String status;
    protected int statusCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentsGenericResponse{");
        sb.append("status='").append(status).append('\'');
        sb.append(", statusCode=").append(statusCode);
        sb.append('}');
        return sb.toString();
    }
}
