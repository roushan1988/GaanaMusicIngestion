package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.enums.PropertyEnum;
import com.til.prime.timesSubscription.model.SubscriptionPropertyModel;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPropertyRepository extends GenericJpaRepository<SubscriptionPropertyModel, Long> {
    SubscriptionPropertyModel findByKey(PropertyEnum key);
}
