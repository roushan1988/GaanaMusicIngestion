package com.til.prime.timesSubscription.es;

import com.til.prime.timesSubscription.model.RawESEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ESRepository extends ElasticsearchRepository<RawESEntity, Long> {
}
