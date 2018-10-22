package com.til.prime.timesSubscription.dto.external;

import java.util.List;

public class BackendSubscriptionRequest extends GenericRequest {
    private List<BackendActivationUserDTO> userList;

    public List<BackendActivationUserDTO> getUserList() {
        return userList;
    }

    public void setUserList(List<BackendActivationUserDTO> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BackendSubscriptionRequest{");
        sb.append("userList=").append(userList);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
