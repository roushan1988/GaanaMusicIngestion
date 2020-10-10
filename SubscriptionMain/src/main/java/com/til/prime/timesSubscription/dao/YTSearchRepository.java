package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.YTSearchResultsEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface YTSearchRepository extends GenericJpaRepository<YTSearchResultsEntity, Long> {
}
