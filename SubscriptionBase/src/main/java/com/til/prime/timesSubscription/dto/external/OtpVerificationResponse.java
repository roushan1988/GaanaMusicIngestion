package com.til.prime.timesSubscription.dto.external;

public class OtpVerificationResponse extends GenericResponse {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OtpVerificationResponse{");
        sb.append("type='").append(type).append('\'');
        sb.append(", success=").append(success);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", validationErrorCategory=").append(validationErrorCategory);
        sb.append(", validationErrors=").append(validationErrors);
        sb.append('}');
        return sb.toString();
    }
}
