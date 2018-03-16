package com.til.prime.timesSubscription.dto.external;

public class OtpVerificationRequest extends GenericRequest {
    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OtpVerificationRequest{");
        sb.append("otp='").append(otp).append('\'');
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
