package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.enums.JobKeyEnum;
import com.til.prime.timesSubscription.model.JobModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JobRepository extends GenericJpaRepository<JobModel, Long> {
    JobModel findByJobKey(JobKeyEnum jobKey);
    @Modifying
    @Transactional
    @Query(value="update JobModel j set j.owner = :owner where j.jobKey = :jobKey and j.owner is null")
    int updateOwnerIfNull(@Param("owner") String owner, @Param("jobKey") JobKeyEnum jobKey);
}
