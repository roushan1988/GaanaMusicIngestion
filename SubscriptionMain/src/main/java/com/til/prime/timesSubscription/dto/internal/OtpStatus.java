package com.til.prime.timesSubscription.dto.internal;

public class OtpStatus {
    private boolean success;
    private String message;

    public OtpStatus(boolean success) {
        this.success = success;
    }

    public OtpStatus(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OtpStatus{");
        sb.append("success=").append(success);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
