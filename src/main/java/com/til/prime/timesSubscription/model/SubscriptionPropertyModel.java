package com.til.prime.timesSubscription.model;

import com.til.prime.timesSubscription.enums.PropertyEnum;

import javax.persistence.*;

@Entity
@Table(name="subscription_property")
public class SubscriptionPropertyModel extends BaseModel {
    @Column(name="key_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private PropertyEnum key;

    @Column(name="key_value", nullable = false)
    private String value;

    public PropertyEnum getKey() {
        return key;
    }

    public void setKey(PropertyEnum key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
