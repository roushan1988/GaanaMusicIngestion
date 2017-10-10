package com.til.prime.timesSubscription.dto.external;

public class CheckStatusRequest extends GenericRequest {
    private String checksum;
    private boolean fallback = false;

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public boolean isFallback() {
        return fallback;
    }

    public void setFallback(boolean fallback) {
        this.fallback = fallback;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckStatusRequest{");
        sb.append("checksum='").append(checksum).append('\'');
        sb.append(", fallback=").append(fallback);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
