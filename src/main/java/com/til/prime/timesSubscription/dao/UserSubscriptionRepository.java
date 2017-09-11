package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.PlanTypeEnum;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.Date;
import java.util.List;

public interface UserSubscriptionRepository extends GenericJpaRepository<UserSubscriptionModel, Long> {
    UserSubscriptionModel findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(Long id, String orderId, Long variantId, boolean deleted);
    UserSubscriptionModel findFirstByUserSsoIdAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(String ssoId, BusinessEnum business, PlanTypeEnum planType, boolean orderCompleted, boolean deleted);
    List<UserSubscriptionModel> findByUserSsoIdAndBusinessAndOrderCompletedAndDeleted(String ssoId, BusinessEnum business, boolean orderCompleted, boolean deleted);
    List<UserSubscriptionModel> findByUserSsoIdAndBusinessAndOrderCompleted(String ssoId, BusinessEnum business, boolean orderCompleted);
    List<UserSubscriptionModel> findByUserSsoIdAndOrderCompletedAndDeleted(String ssoId, boolean orderCompleted, boolean deleted);
    List<UserSubscriptionModel> findByUserSsoIdAndOrderCompleted(String ssoId, boolean orderCompleted);
    UserSubscriptionModel findFirstByUserSsoIdAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(String ssoId, BusinessEnum business, boolean orderCompleted, Date date, Date date2, boolean deleted);
    UserSubscriptionModel findFirstByUserSsoIdAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfter(String ssoId, BusinessEnum business, boolean orderCompleted, Date date, Date date2);
    UserSubscriptionModel findFirstByUserSsoIdAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(String ssoId, boolean orderCompleted, Date date, Date date2, boolean deleted);
    UserSubscriptionModel findFirstByUserSsoIdAndOrderCompletedAndStartDateBeforeAndEndDateAfter(String ssoId, boolean orderCompleted, Date date, Date date2);
    UserSubscriptionModel findFirstByUserSsoIdAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(String ssoId, BusinessEnum business, boolean orderCompleted, boolean deleted);
    UserSubscriptionModel findFirstByUserSsoIdAndBusinessAndOrderCompletedOrderByIdDesc(String ssoId, BusinessEnum business, boolean orderCompleted);
}
