package com.til.prime.timesSubscription.dto.external;

public class AuthDTO {
    private String status;
    private String statusMessage;
    private String userToken;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AuthDTO{");
        sb.append("status='").append(status).append('\'');
        sb.append(", statusMessage='").append(statusMessage).append('\'');
        sb.append(", userToken='").append(userToken).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
