package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.RawMxGaanaDbEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RawDataRepository extends GenericJpaRepository<RawMxGaanaDbEntity, Long> {
    RawMxGaanaDbEntity findByTrackId(Long trackId);
}
