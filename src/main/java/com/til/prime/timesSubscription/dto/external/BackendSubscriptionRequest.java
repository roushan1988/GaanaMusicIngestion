package com.til.prime.timesSubscription.dto.external;

import java.util.List;

public class BackendSubscriptionRequest extends GenericRequest {
    private List<BackendActivationUserDTO> userList;
    private String checksum;

    public List<BackendActivationUserDTO> getUserList() {
        return userList;
    }

    public void setUserList(List<BackendActivationUserDTO> userList) {
        this.userList = userList;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
