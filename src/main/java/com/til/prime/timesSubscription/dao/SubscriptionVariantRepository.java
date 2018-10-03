package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.model.SubscriptionVariantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface SubscriptionVariantRepository extends JpaRepository<SubscriptionVariantModel, Long> {
    SubscriptionVariantModel findByIdAndSubscriptionPlanIdAndPriceAndDurationDaysAndDeleted(Long variantId, Long planId, BigDecimal price, Long durationDays, boolean deleted);
    SubscriptionVariantModel findByIdAndSubscriptionPlanIdAndDeleted(Long variantId, Long planId, boolean deleted);
    SubscriptionVariantModel findByIdAndSubscriptionPlanIdAndSubscriptionPlanBusinessAndDeletedFalse(Long variantId, Long planId, BusinessEnum business);
    SubscriptionVariantModel findByIdAndNameAndSubscriptionPlanIdAndDeleted(Long variantId, String name, Long planId, boolean deleted);
}
