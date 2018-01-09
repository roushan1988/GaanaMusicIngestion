package com.til.prime.timesSubscription.dto.external;

public class UpdateCacheForMobileRequest extends GenericRequest {
    private String checksum;

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UpdateCacheForMobileRequest{");
        sb.append("checksum='").append(checksum).append('\'');
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
