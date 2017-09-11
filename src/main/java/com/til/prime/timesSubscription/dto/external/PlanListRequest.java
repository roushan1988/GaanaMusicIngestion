package com.til.prime.timesSubscription.dto.external;

public class PlanListRequest extends GenericRequest {
    private String business;
    private String country;

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanListRequest{");
        sb.append("business='").append(business).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
