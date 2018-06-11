package com.til.prime.timesSubscription.dto.external;

import java.io.Serializable;
import java.util.Date;

public class PublishedUserStatusDTO implements Serializable {
    private static final long serialVersionUID = 1l;

    private String mobile;
    private String ssoId;
    private boolean timesPrimeUser;
    private Date expiry;
    private int planStatus;
    private String email;
    private boolean autoRenewal;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public boolean isTimesPrimeUser() {
        return timesPrimeUser;
    }

    public void setTimesPrimeUser(boolean timesPrimeUser) {
        this.timesPrimeUser = timesPrimeUser;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAutoRenewal() {
        return autoRenewal;
    }

    public void setAutoRenewal(boolean autoRenewal) {
        this.autoRenewal = autoRenewal;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PublishedUserStatusDTO{");
        sb.append("mobile='").append(mobile).append('\'');
        sb.append(", ssoId='").append(ssoId).append('\'');
        sb.append(", timesPrimeUser=").append(timesPrimeUser);
        sb.append(", expiry=").append(expiry);
        sb.append(", planStatus=").append(planStatus);
        sb.append(", autoRenewal=").append(autoRenewal);
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
