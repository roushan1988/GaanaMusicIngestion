package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.BackendSubscriptionUserModel;
import org.springframework.stereotype.Repository;

@Repository
public interface BackendSubscriptionUserRepository extends GenericJpaRepository<BackendSubscriptionUserModel, Long> {
    BackendSubscriptionUserModel findByMobileAndDeletedFalse(String mobile);
    BackendSubscriptionUserModel findByMobileAndCompletedFalseAndDeletedFalse(String mobile);
}
