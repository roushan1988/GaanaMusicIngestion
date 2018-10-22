package com.til.prime.timesSubscription.dto.internal;

public class ShortenedUrlData {
    private String global_hash;
    private String hash;
    private String long_url;
    private Integer new_hash;
    private String url;

    public String getGlobal_hash() {
        return global_hash;
    }

    public void setGlobal_hash(String global_hash) {
        this.global_hash = global_hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getLong_url() {
        return long_url;
    }

    public void setLong_url(String long_url) {
        this.long_url = long_url;
    }

    public Integer getNew_hash() {
        return new_hash;
    }

    public void setNew_hash(Integer new_hash) {
        this.new_hash = new_hash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ShortenedUrlData{");
        sb.append("global_hash='").append(global_hash).append('\'');
        sb.append(", hash='").append(hash).append('\'');
        sb.append(", long_url='").append(long_url).append('\'');
        sb.append(", new_hash=").append(new_hash);
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
