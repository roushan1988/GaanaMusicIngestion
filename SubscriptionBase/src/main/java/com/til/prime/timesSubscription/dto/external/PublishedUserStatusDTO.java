package com.til.prime.timesSubscription.dto.external;

import java.io.Serializable;
import java.util.Date;

public class PublishedUserStatusDTO implements Serializable {
    private static final long serialVersionUID = 1l;

    private String mobile;
    private String ssoId;
    private boolean timesPrimeUser;
    private Date expiry;
    private Date lastExpiry;
    private int planStatus;
    private String email;
    private boolean autoRenewal;
    private Date startDate;
    private String firstName;
    private String lastName;
    private String primeId;

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

    public Date getLastExpiry() {
        return lastExpiry;
    }

    public void setLastExpiry(Date lastExpiry) {
        this.lastExpiry = lastExpiry;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrimeId() {
        return primeId;
    }

    public void setPrimeId(String primeId) {
        this.primeId = primeId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PublishedUserStatusDTO{");
        sb.append("mobile='").append(mobile).append('\'');
        sb.append(", ssoId='").append(ssoId).append('\'');
        sb.append(", timesPrimeUser=").append(timesPrimeUser);
        sb.append(", expiry=").append(expiry);
        sb.append(", lastExpiry=").append(lastExpiry);
        sb.append(", planStatus=").append(planStatus);
        sb.append(", email='").append(email).append('\'');
        sb.append(", autoRenewal=").append(autoRenewal);
        sb.append(", startDate=").append(startDate);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", primeId='").append(primeId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
