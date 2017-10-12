package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.ExternalClientModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalClientRepository extends GenericJpaRepository<ExternalClientModel, Long> {
}
