package com.til.prime.timesSubscription.dto.internal;

public class UrlShorteningResponse {
    private String id;
    private String kind;
    private String longUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UrlShorteningResponse{");
        sb.append("id='").append(id).append('\'');
        sb.append(", kind='").append(kind).append('\'');
        sb.append(", longUrl='").append(longUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
