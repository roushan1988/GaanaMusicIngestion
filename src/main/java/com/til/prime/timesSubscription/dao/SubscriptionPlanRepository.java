package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.CountryEnum;
import com.til.prime.timesSubscription.model.SubscriptionPlanModel;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends GenericJpaRepository<SubscriptionPlanModel, Long> {
    List<SubscriptionPlanModel> findByBusinessInAndCountryAndDeleted(Collection<BusinessEnum> businesses, CountryEnum country, boolean deleted);
    List<SubscriptionPlanModel> findByBusinessAndCountryAndDeleted(BusinessEnum business, CountryEnum country, boolean deleted);
}
