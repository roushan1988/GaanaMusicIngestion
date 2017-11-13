package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.PlanTypeEnum;
import com.til.prime.timesSubscription.enums.StatusEnum;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface UserSubscriptionRepository extends GenericJpaRepository<UserSubscriptionModel, Long> {
    UserSubscriptionModel findByIdAndDeleted(Long id, boolean deleted);
    UserSubscriptionModel findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(Long id, String orderId, Long variantId, boolean deleted);
    UserSubscriptionModel findByOrderIdAndSubscriptionVariantIdAndDeleted(String orderId, Long variantId, boolean deleted);
    UserSubscriptionModel findFirstByUserMobileAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(String mobile, BusinessEnum business, PlanTypeEnum planType, boolean orderCompleted, boolean deleted);
    List<UserSubscriptionModel> findByUserMobileAndStatusInAndOrderCompletedTrueAndAutoRenewalFalseAndDeletedFalse(String mobile, Collection<StatusEnum> statusEnums);
    List<UserSubscriptionModel> findByUserMobileAndBusinessAndOrderCompletedAndDeleted(String mobile, BusinessEnum business, boolean orderCompleted, boolean deleted);
    List<UserSubscriptionModel> findByUserMobileAndBusinessAndOrderCompleted(String mobile, BusinessEnum business, boolean orderCompleted);
    List<UserSubscriptionModel> findByUserMobileAndOrderCompletedAndDeleted(String mobile, boolean orderCompleted, boolean deleted);
    List<UserSubscriptionModel> findByUserMobileAndOrderCompleted(String mobile, boolean orderCompleted);
    UserSubscriptionModel findFirstByUserMobileAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(String mobile, BusinessEnum business, boolean orderCompleted, Date date, Date date2, boolean deleted);
    UserSubscriptionModel findFirstByUserMobileAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfter(String mobile, BusinessEnum business, boolean orderCompleted, Date date, Date date2);
    UserSubscriptionModel findFirstByUserMobileAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(String mobile, boolean orderCompleted, Date date, Date date2, boolean deleted);
    UserSubscriptionModel findFirstByUserMobileAndOrderCompletedAndStartDateBeforeAndEndDateAfter(String mobile, boolean orderCompleted, Date date, Date date2);
    UserSubscriptionModel findFirstByUserMobileAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(String mobile, BusinessEnum business, boolean orderCompleted, boolean deleted);
    Long countByStatusAndEndDateBeforeAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date);
    Page<UserSubscriptionModel> findByStatusAndEndDateBeforeAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date, Pageable pageable);
    Page<UserSubscriptionModel> findByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalFalse(StatusEnum status, Date date1, Date date2, Pageable pageable);
    Page<UserSubscriptionModel> findByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalTrue(StatusEnum status, Date date1, Date date2, Pageable pageable);
    Long countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date1, Date date2);
    Long countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalFalse(StatusEnum status, Date date1, Date date2);
    Long countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalTrue(StatusEnum status, Date date1, Date date2);
    Long countByUserMobileAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrue(String mobile, StatusEnum status, Date date1);
    UserSubscriptionModel findFirstByUserMobileAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrueOrderById(String mobile, StatusEnum status, Date date1);
    List<UserSubscriptionModel> findByUserMobileAndStatusInAndOrderCompletedTrueAndDeletedFalse(String mobile, Collection<StatusEnum> statusCollection);
    UserSubscriptionModel findByUserMobileAndStatusAndDeletedAndOrderCompletedTrue(String mobile, StatusEnum status, boolean deleted);
    UserSubscriptionModel findByUserMobileAndStatusAndOrderCompletedTrue(String mobile, StatusEnum statusEnum);
}
