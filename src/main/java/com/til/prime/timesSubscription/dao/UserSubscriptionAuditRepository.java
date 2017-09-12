package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.UserSubscriptionAuditModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionAuditRepository extends GenericJpaRepository<UserSubscriptionAuditModel, Long> {
}
