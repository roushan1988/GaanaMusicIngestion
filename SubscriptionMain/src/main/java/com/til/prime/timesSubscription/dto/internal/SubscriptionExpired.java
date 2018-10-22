package com.til.prime.timesSubscription.dto.internal;

import java.util.List;

/**
 * @author amarnath.pathak
 * @date 05/09/18
 **/
public class SubscriptionExpired {

    private Long recordsAffected;
    private List<Long> affectedModels;


    public SubscriptionExpired(Long recordsAffected, List<Long> affectedModels) {
        this.recordsAffected = recordsAffected;
        this.affectedModels = affectedModels;
    }

    public Long getRecordsAffected() {
        return recordsAffected;
    }

    public List<Long> getAffectedModels() {
        return affectedModels;
    }
}
