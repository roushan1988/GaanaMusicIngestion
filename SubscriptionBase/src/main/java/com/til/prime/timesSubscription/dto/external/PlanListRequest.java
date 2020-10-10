package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.ChannelEnum;
import com.til.prime.timesSubscription.enums.CountryEnum;

public class PlanListRequest extends GenericRequest {
    private BusinessEnum business;
    private CountryEnum country;
    private Long planId;
    private ChannelEnum channel;

    public BusinessEnum getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEnum business) {
        this.business = business;
    }

    public CountryEnum getCountry() {
        return country;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public ChannelEnum getChannel() {
        return channel;
    }

    public void setChannel(ChannelEnum channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanListRequest{");
        sb.append("business=").append(business);
        sb.append(", country=").append(country);
        sb.append(", planId=").append(planId);
        sb.append(", channel=").append(channel);
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
