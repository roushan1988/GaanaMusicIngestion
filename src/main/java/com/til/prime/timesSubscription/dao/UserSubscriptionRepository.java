package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.PlanTypeEnum;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserSubscriptionRepository extends GenericJpaRepository<UserSubscriptionModel, Long> {
    UserSubscriptionModel findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(Long id, String orderId, Long variantId, boolean deleted);
    UserSubscriptionModel findByOrderIdAndSubscriptionVariantIdAndDeleted(String orderId, Long variantId, boolean deleted);
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
    Long countBySsoCommunicatedFalseAndOrderCompletedTrueAndDeletedFalse();
    Long countByExpiredFalseAndEndDateBeforeAndDeletedFalse(Date date);
    Page<UserSubscriptionModel> findByExpiredFalseAndEndDateBeforeAndDeletedFalse(Date date, Pageable pageable);
    Page<UserSubscriptionModel> findByExpiredAndEndDateBetweenAndDeletedFalse(boolean expired, Date date1, Date date2, Pageable pageable);
    Long countByExpiredAndEndDateBetweenAndDeletedFalse(boolean expired, Date date1, Date date2);
    Page<UserSubscriptionModel> findBySsoCommunicatedFalseAndOrderCompletedTrueAndDeletedFalseOrderById(Pageable pageable);
}
