package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.JobAuditModel;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAuditRepository extends GenericJpaRepository<JobAuditModel, Long> {
}
