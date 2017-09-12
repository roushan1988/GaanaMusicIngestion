package com.til.prime.timesSubscription.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="subscription_property")
public class SubscriptionPropertyModel extends BaseModel {
    @Column(name="key_name", nullable = false)
    private String key;

    @Column(name="key_value", nullable = false)
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
