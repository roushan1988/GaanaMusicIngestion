package com.til.prime.timesSubscription.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="external_clients")
public class ExternalClientModel extends BaseModel {
    @Column(name="client_id")
    private String clientId;
    @Column(name="secret_key")
    private String secretKey;
    @Column(name="encryption_key")
    private String encryptionKey;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
}
