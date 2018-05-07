package com.til.prime.timesSubscription.dto.external;

import java.io.Serializable;
import java.util.Date;

public class SubscriptionStatusDTO implements Serializable {
    private static final long serialVersionUID = 1l;
    private boolean blocked;
    private Date startDate;
    private Date endDate;
    private int planStatus;
    private boolean autoRenewal;
    private String email;
    private String firstName;
    private String lastName;
    private String primeId;

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }

    public boolean isAutoRenewal() {
        return autoRenewal;
    }

    public void setAutoRenewal(boolean autoRenewal) {
        this.autoRenewal = autoRenewal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPrimeId() {
        return primeId;
    }

    public void setPrimeId(String primeId) {
        this.primeId = primeId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionStatusDTO{");
        sb.append("blocked=").append(blocked);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", planStatus=").append(planStatus);
        sb.append(", autoRenewal=").append(autoRenewal);
        sb.append(", email='").append(email).append('\'');
        sb.append(", primeId='").append(primeId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
