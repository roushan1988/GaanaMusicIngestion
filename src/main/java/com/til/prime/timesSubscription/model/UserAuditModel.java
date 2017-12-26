package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.enums.EventEnum;

import javax.persistence.*;

@Entity
@Table(name="user_audit")
public class UserAuditModel extends BaseModel {
    @Column(name="user_id")
    private Long userId;
    @Column
    @Enumerated(EnumType.STRING)
    private EventEnum event;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String mobile;
    @Column
    private String email;
    @Column(name="sso_id", nullable = false)
    private String ssoId;
    @Column
    private String city;
    @Column
    private boolean blocked;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public EventEnum getEvent() {
        return event;
    }

    public void setEvent(EventEnum event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}