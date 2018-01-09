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
    UserSubscriptionModel findFirstByUserMobileAndUserDeletedFalseAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(String mobile, BusinessEnum business, PlanTypeEnum planType, boolean orderCompleted, boolean deleted);
    List<UserSubscriptionModel> findByUserMobileAndUserDeletedFalseAndStatusInAndOrderCompletedTrueAndAutoRenewalFalseAndDeletedFalse(String mobile, Collection<StatusEnum> statusEnums);
    List<UserSubscriptionModel> findByUserMobileAndUserDeletedFalseAndStatusInAndOrderCompletedTrueAndAutoRenewalTrueAndDeletedFalse(String mobile, Collection<StatusEnum> statusEnums);
    List<UserSubscriptionModel> findByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeleted(String mobile, BusinessEnum business, boolean orderCompleted, boolean deleted);
    List<UserSubscriptionModel> findByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompleted(String mobile, BusinessEnum business, boolean orderCompleted);
    List<UserSubscriptionModel> findByUserMobileAndUserDeletedFalseAndOrderCompletedAndDeleted(String mobile, boolean orderCompleted, boolean deleted);
    List<UserSubscriptionModel> findByUserMobileAndUserDeletedFalseAndOrderCompleted(String mobile, boolean orderCompleted);
    UserSubscriptionModel findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(String mobile, BusinessEnum business, boolean orderCompleted, Date date, Date date2, boolean deleted);
    UserSubscriptionModel findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndStartDateBeforeAndEndDateAfter(String mobile, BusinessEnum business, boolean orderCompleted, Date date, Date date2);
    UserSubscriptionModel findFirstByUserMobileAndUserDeletedFalseAndOrderCompletedAndStartDateBeforeAndEndDateAfterAndDeleted(String mobile, boolean orderCompleted, Date date, Date date2, boolean deleted);
    UserSubscriptionModel findFirstByUserMobileAndUserDeletedFalseAndOrderCompletedAndStartDateBeforeAndEndDateAfter(String mobile, boolean orderCompleted, Date date, Date date2);
    UserSubscriptionModel findFirstByUserMobileAndUserDeletedFalseAndBusinessAndOrderCompletedAndDeletedOrderByIdDesc(String mobile, BusinessEnum business, boolean orderCompleted, boolean deleted);
    UserSubscriptionModel findFirstByUserMobileAndBusinessAndStatusAndUserDeletedFalseAndOrderCompletedTrueAndDeletedFalseOrderByIdDesc(String mobile, BusinessEnum business, StatusEnum status);
    Long countByStatusAndEndDateBeforeAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date);
    Page<UserSubscriptionModel> findByStatusAndEndDateBeforeAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date, Pageable pageable);
    Page<UserSubscriptionModel> findByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalFalse(StatusEnum status, Date date1, Date date2, Pageable pageable);
    Page<UserSubscriptionModel> findByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalTrue(StatusEnum status, Date date1, Date date2, Pageable pageable);
    Long countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date1, Date date2);
    Long countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalFalse(StatusEnum status, Date date1, Date date2);
    Long countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalTrue(StatusEnum status, Date date1, Date date2);
    Long countByUserMobileAndUserDeletedFalseAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrue(String mobile, StatusEnum status, Date date1);
    UserSubscriptionModel findFirstByUserMobileAndUserDeletedFalseAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrueOrderById(String mobile, StatusEnum status, Date date1);
    List<UserSubscriptionModel> findByUserMobileAndUserDeletedFalseAndStatusInAndOrderCompletedTrueAndDeletedFalse(String mobile, Collection<StatusEnum> statusCollection);
    UserSubscriptionModel findByUserMobileAndUserDeletedFalseAndStatusAndDeletedAndOrderCompletedTrue(String mobile, StatusEnum status, boolean deleted);
    UserSubscriptionModel findByUserMobileAndUserDeletedFalseAndStatusAndOrderCompletedTrue(String mobile, StatusEnum statusEnum);
}
