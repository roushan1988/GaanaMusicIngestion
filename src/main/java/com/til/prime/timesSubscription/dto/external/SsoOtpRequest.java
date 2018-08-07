package com.til.prime.timesSubscription.dto.external;

public class SsoOtpRequest {
    private String mobile;
    private String otp;
    private String checksum;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SsoOtpRequest{");
        sb.append("mobile='").append(mobile).append('\'');
        sb.append(", otp='").append(otp).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
