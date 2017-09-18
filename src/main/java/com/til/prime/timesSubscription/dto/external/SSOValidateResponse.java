package com.til.prime.timesSubscription.dto.external;

public class SSOValidateResponse {
    private String code;
    private String gassoid;
    private String emailId;
    private String userId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGassoid() {
        return gassoid;
    }

    public void setGassoid(String userId) {
        this.gassoid = userId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SSOValidateResponse{");
        sb.append("code='").append(code).append('\'');
        sb.append(", gassoid='").append(gassoid).append('\'');
        sb.append(", emailId='").append(emailId).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
