package com.til.prime.timesSubscription.dto.external;

public class BlockUnblockRequest extends GenericRequest {
    private boolean blockUser = true;
    private String checksum;

    public boolean isBlockUser() {
        return blockUser;
    }

    public void setBlockUser(boolean blockUser) {
        this.blockUser = blockUser;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BlockUnblockRequest{");
        sb.append("blockUser=").append(blockUser);
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
