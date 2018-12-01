package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.MxGaanaDbEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GaanaDataRepository extends GenericJpaRepository<MxGaanaDbEntity, Long> {
    MxGaanaDbEntity findByTrackId(Long trackId);
    List<MxGaanaDbEntity> findByYoutubeIdNotNullOrderById(Pageable pageable);
    List<MxGaanaDbEntity> findByYoutubeIdNotNullAndValidNullOrderById(Pageable pageable);
    List<MxGaanaDbEntity> findAllByOrderByPopularityIndexDesc(Pageable pageable);
    List<MxGaanaDbEntity> findFirst10ByYoutubeIdNullOrderByPopularityIndexDesc();
}
