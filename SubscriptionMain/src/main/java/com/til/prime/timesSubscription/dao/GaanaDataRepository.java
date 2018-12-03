package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.MxGaanaDbEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GaanaDataRepository extends GenericJpaRepository<MxGaanaDbEntity, Long> {
    MxGaanaDbEntity findByTrackId(Long trackId);
    List<MxGaanaDbEntity> findByYoutubeIdNotNullOrderById(Pageable pageable);
    List<MxGaanaDbEntity> findByYoutubeIdNotNullAndValidNullOrderById(Pageable pageable);
    List<MxGaanaDbEntity> findAllByOrderByPopularityIndexDesc(Pageable pageable);
    List<MxGaanaDbEntity> findFirst10ByYoutubeIdNullOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findFirst1000ByYoutubeIdNullOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findFirst10ByOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findFirst1000ByOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findByYoutubeIdNotNullAndPopularityIndexBetweenOrderByPopularityIndexDesc(long min, long max, Pageable pageable);
    List<MxGaanaDbEntity> findByYoutubeIdNotNullAndUpdatedBetweenOrderByPopularityIndexDesc(Date start, Date end, Pageable pageable);
    List<MxGaanaDbEntity> findFirst1000ByS3VideoThumbnailPathNullOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findFirst1000ByS3VideoThumbnailPathNullAndYoutubeIdNotNullOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findFirst1000ByS3VideoThumbnailPathNullAndYoutubeIdNotNullAndYoutubeIdNotOrderByPopularityIndexDesc(String na);
    List<MxGaanaDbEntity> findFirst10ByS3VideoThumbnailPathNullAndYoutubeIdNotNullAndYoutubeIdNotOrderByPopularityIndexDesc(String na);
}
