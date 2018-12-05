package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.MxGaanaDbEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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
    List<MxGaanaDbEntity> findFirst10ByLanguageAndYoutubeIdNullOrderByPopularityIndexDesc(String language);
    List<MxGaanaDbEntity> findFirst1000ByYoutubeIdNullOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findFirst1000ByYoutubeIdNotNullOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findByYoutubeIdNotNullOrderByPopularityIndexDesc(Pageable pageable);
    List<MxGaanaDbEntity> findFirst10ByYoutubeIdNotNullOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findFirst10ByOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findFirst1000ByOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findByYoutubeIdNotNullAndPopularityIndexBetweenOrderByPopularityIndexDesc(long min, long max, Pageable pageable);
    List<MxGaanaDbEntity> findByJobTagAndYoutubeIdNotNullAndUpdatedBetweenOrderByPopularityIndexDesc(String jobTag, Date start, Date end, Pageable pageable);
    List<MxGaanaDbEntity> findByYoutubeIdNotNullAndValidTrueAndUpdatedBetweenOrderByPopularityIndexDesc(Date start, Date end, Pageable pageable);
    List<MxGaanaDbEntity> findFirst1000ByS3VideoThumbnailPathNullOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findFirst1000ByS3VideoThumbnailPathNullAndYoutubeIdNotNullOrderByPopularityIndexDesc();
    List<MxGaanaDbEntity> findFirst1000ByS3VideoThumbnailPathNullAndYoutubeIdNotNullAndYoutubeIdNotOrderByPopularityIndexDesc(String na);
    List<MxGaanaDbEntity> findFirst10ByS3VideoThumbnailPathNullAndYoutubeIdNotNullAndYoutubeIdNotOrderByPopularityIndexDesc(String na);
    List<MxGaanaDbEntity> findAllByYoutubeIdNotNullAndThumbnailNotNullAndS3VideoThumbnailPathNull();
    List<MxGaanaDbEntity> findAllByYoutubeIdNotNullAndS3AlbumThumbnailPathNull();
    @Query(value="select m from MxGaanaDbEntity m where m.youtubeId is null and (length(m.actor)>0 or length(m.actress)>0)")
    List<MxGaanaDbEntity> findAllByYTNullAndActorOrActressPresent(Pageable pageable);
    @Query(value="select count(m) from MxGaanaDbEntity m where m.youtubeId is null and (length(m.actor)>0 or length(m.actress)>0)")
    Long countByYTNullAndActorOrActressPresent();
}
