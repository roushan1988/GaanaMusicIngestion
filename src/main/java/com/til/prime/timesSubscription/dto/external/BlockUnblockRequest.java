package com.til.prime.timesSubscription.dto.external;

public class BlockUnblockRequest extends GenericRequest {
    private boolean blockUser = true;

    public boolean isBlockUser() {
        return blockUser;
    }

    public void setBlockUser(boolean blockUser) {
        this.blockUser = blockUser;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BlockUnblockRequest{");
        sb.append("blockUser=").append(blockUser);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
