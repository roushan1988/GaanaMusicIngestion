package com.til.prime.timesSubscription.dto.external;

public class CustomerSearchRequest extends GenericRequest {
    private int page;
    private int pageSize=100;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UpdateCacheForMobileRequest{");
        sb.append("checksum='").append(checksum).append('\'');
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append(", page").append(page);
        sb.append(", pageSize").append(pageSize);
        sb.append('}');
        return sb.toString();
    }
}
