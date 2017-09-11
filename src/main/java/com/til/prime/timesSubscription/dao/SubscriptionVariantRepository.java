package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.SubscriptionVariantModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface SubscriptionVariantRepository extends JpaRepository<SubscriptionVariantModel, Long> {
    SubscriptionVariantModel findByIdAndSubscriptionPlanIdAndPriceAndDurationDaysAndDeleted(Long variantId, Long planId, BigDecimal price, Long durationDays, boolean deleted);
    SubscriptionVariantModel findByIdAndSubscriptionPlanIdAndDeleted(Long variantId, Long planId, boolean deleted);
}
