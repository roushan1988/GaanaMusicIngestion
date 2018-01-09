package com.til.prime.timesSubscription.dto.external;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.ChannelEnum;
import com.til.prime.timesSubscription.enums.PlatformEnum;

public class BackendSubscriptionActivationRequest extends GenericRequest {
    private BusinessEnum business;
    private ChannelEnum channel;
    private PlatformEnum platform;
    private String activationCode;

    public void setBusiness(BusinessEnum business) {
        this.business = business;
    }

    public void setChannel(ChannelEnum channel) {
        this.channel = channel;
    }

    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }

    public BusinessEnum getBusiness() {
        return business;
    }

    public ChannelEnum getChannel() {
        return channel;
    }

    public PlatformEnum getPlatform() {
        return platform;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BackendSubscriptionActivationRequest{");
        sb.append("business=").append(business);
        sb.append(", channel=").append(channel);
        sb.append(", platform=").append(platform);
        sb.append(", activationCode='").append(activationCode).append('\'');
        sb.append(", user=").append(user);
        sb.append(", secretKey='").append(secretKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
