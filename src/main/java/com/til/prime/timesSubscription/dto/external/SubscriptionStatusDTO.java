package com.til.prime.timesSubscription.dto.external;

import java.io.Serializable;
import java.util.Date;

public class SubscriptionStatusDTO implements Serializable {
    private static final long serialVersionUID = 1l;
    private Long userId;
    private boolean blocked;
    private Date startDate;
    private Date endDate;
    private int planStatus;
    private boolean autoRenewal;
    private String email;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionStatusDTO{");
        sb.append("userId=").append(userId);
        sb.append(", blocked=").append(blocked);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", planStatus=").append(planStatus);
        sb.append(", autoRenewal=").append(autoRenewal);
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
