package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.enums.EventEnum;

import javax.persistence.*;

@Entity
@Table(name="backend_subscription_user_audit")
public class BackendSubscriptionUserAuditModel extends BaseModel {
    @Column(name="backend_subscription_user_id")
    private Long backendSubscriptionUserId;
    @Column
    private String mobile;
    @Column
    private String email;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column
    private String code;
    @Column(name="shortened_url")
    private String shortenedUrl;
    @Column(name="duration_days")
    private Long durationDays;
    @Column
    private boolean completed;
    @Column
    @Enumerated(EnumType.STRING)
    private EventEnum event;

    public Long getBackendSubscriptionUserId() {
        return backendSubscriptionUserId;
    }

    public void setBackendSubscriptionUserId(Long backendSubscriptionUserId) {
        this.backendSubscriptionUserId = backendSubscriptionUserId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public Long getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Long durationDays) {
        this.durationDays = durationDays;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public EventEnum getEvent() {
        return event;
    }

    public void setEvent(EventEnum event) {
        this.event = event;
    }
}