package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.UserAuditModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuditRepository extends GenericJpaRepository<UserAuditModel, Long> {
    void deleteByUserId(Long userId);
}
