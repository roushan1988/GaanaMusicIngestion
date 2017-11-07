package com.til.prime.timesSubscription.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="user")
public class UserModel extends BaseModel {
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserSubscriptionModel> userSubscriptions;

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

    public List<UserSubscriptionModel> getUserSubscriptions() {
        return userSubscriptions;
    }

    public void setUserSubscriptions(List<UserSubscriptionModel> userSubscriptions) {
        this.userSubscriptions = userSubscriptions;
    }
}
