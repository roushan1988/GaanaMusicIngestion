package com.til.prime.timesSubscription.dto.internal;

public class UrlShorteningResponse {
    private Integer status_code;
    private String status_txt;
    private ShortenedUrlData data;


    public Integer getStatus_code() {
        return status_code;
    }

    public void setStatus_code(Integer status_code) {
        this.status_code = status_code;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_txt) {
        this.status_txt = status_txt;
    }

    public ShortenedUrlData getData() {
        return data;
    }

    public void setData(ShortenedUrlData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UrlShorteningResponse{");
        sb.append("status_code=").append(status_code);
        sb.append(", status_txt='").append(status_txt).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
