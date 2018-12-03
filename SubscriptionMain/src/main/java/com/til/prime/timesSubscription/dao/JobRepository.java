package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.enums.JobEnum;
import com.til.prime.timesSubscription.model.JobEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends GenericJpaRepository<JobEntity, Long> {
    JobEntity findByName(JobEnum name);
}
