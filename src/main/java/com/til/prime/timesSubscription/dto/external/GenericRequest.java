package com.til.prime.timesSubscription.dto.external;

public class GenericRequest {
    protected UserDTO user;
    protected String secretKey;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GenericRequest{");
        sb.append("user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
