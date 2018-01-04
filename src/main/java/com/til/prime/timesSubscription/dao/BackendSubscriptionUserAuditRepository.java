package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.BackendSubscriptionUserAuditModel;
import org.springframework.stereotype.Repository;

@Repository
public interface BackendSubscriptionUserAuditRepository extends GenericJpaRepository<BackendSubscriptionUserAuditModel, Long> {
}
