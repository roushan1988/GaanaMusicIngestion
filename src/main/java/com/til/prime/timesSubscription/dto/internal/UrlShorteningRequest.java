package com.til.prime.timesSubscription.dto.internal;

public class UrlShorteningRequest {
    public UrlShorteningRequest(String longUrl) {
        this.longUrl = longUrl;
    }

    private String longUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UrlShorteningRequest{");
        sb.append("longUrl='").append(longUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
