package com.til.prime.timesSubscription.dto.external;

public class OtpRequest extends GenericRequest {
    private boolean resend;

    public boolean isResend() {
        return resend;
    }

    public void setResend(boolean resend) {
        this.resend = resend;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OtpRequest{");
        sb.append("resend=").append(resend);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
