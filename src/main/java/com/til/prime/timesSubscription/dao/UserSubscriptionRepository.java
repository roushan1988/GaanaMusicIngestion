package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.PlanTypeEnum;
import com.til.prime.timesSubscription.enums.StatusEnum;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface UserSubscriptionRepository extends GenericJpaRepository<UserSubscriptionModel, Long> {
    @Query(value="select us from UserSubscriptionModel us where us.ssoCommunicated = false and us.id in (select max(id) from UserSubscriptionModel us1 where us1.orderCompleted = true and us1.status in :statusSet and us1.deleted = false and us1.user.deleted = false group by us1.user)")
    List<UserSubscriptionModel> findUserSubscriptionsForSSOStatusUpdate(@Param("statusSet") Set<StatusEnum> statusSet, Pageable pageable);
    @Query(value="select us from UserSubscriptionModel us where us.statusPublished = false and us.id in (select max(id) from UserSubscriptionModel us1 where us1.orderCompleted = true and us1.status in :statusSet and us1.deleted = false and us1.user.deleted = false group by us1.user)")
    List<UserSubscriptionModel> findUserSubscriptionsForStatusPublish(@Param("statusSet") Set<StatusEnum> statusSet, Pageable pageable);
    @Query(value="select us from UserSubscriptionModel us where us.id in (select min(id) from UserSubscriptionModel where order_completed=true and deleted = false and status= :futureStatus and start_date < :currentTime and end_date > :currentTime and user in (select user  FROM UserSubscriptionModel " +
            "where orderCompleted = true and deleted = false GROUP BY user having SUM( CASE WHEN status = :activeStatus THEN 1 ELSE 0 END )=0 and SUM( CASE WHEN status = :futureStatus THEN 1 ELSE 0 END )>0) GROUP BY user)")
    List<UserSubscriptionModel> findUserSubscriptionsForFutureActivation(@Param("currentTime") Date currentTime, @Param("activeStatus") StatusEnum activeStatus, @Param("futureStatus") StatusEnum futureStatus, Pageable pageable);
    UserSubscriptionModel findByIdAndDeleted(Long id, boolean deleted);
    UserSubscriptionModel findByIdAndOrderIdAndSubscriptionVariantIdAndDeleted(Long id, String orderId, Long variantId, boolean deleted);
    UserSubscriptionModel findByOrderIdAndSubscriptionVariantIdAndDeleted(String orderId, Long variantId, boolean deleted);
    UserSubscriptionModel findFirstByUserMobileAndUserDeletedFalseAndBusinessAndSubscriptionVariantPlanTypeAndOrderCompletedAndDeletedOrderByIdDesc(String mobile, BusinessEnum business, PlanTypeEnum planType, boolean orderCompleted, boolean deleted);
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
    Long countByStatusAndEndDateBeforeAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date);
    Page<UserSubscriptionModel> findByStatusAndEndDateBeforeAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date, Pageable pageable);
    Page<UserSubscriptionModel> findByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date1, Date date2, Pageable pageable);
    Page<UserSubscriptionModel> findByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalFalse(StatusEnum status, Date date1, Date date2, Pageable pageable);
    Page<UserSubscriptionModel> findByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalTrue(StatusEnum status, Date date1, Date date2, Pageable pageable);
    Long countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date1, Date date2);
    Long countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalFalse(StatusEnum status, Date date1, Date date2);
    Long countByStatusAndEndDateBetweenAndDeletedFalseAndOrderCompletedTrueAndAutoRenewalTrue(StatusEnum status, Date date1, Date date2);
    Page<UserSubscriptionModel> findByUserMobileAndUserDeletedFalseAndStatusAndUserDeletedOrderByEndDateDesc(String mobile, StatusEnum statusEnum, boolean deleted, Pageable pageable);
    Page<UserSubscriptionModel> findByUserMobileAndUserDeletedFalseAndStatusAndDeletedOrderByEndDateDesc(String mobile, StatusEnum statusEnum, boolean deleted, Pageable pageable);
    UserSubscriptionModel findByOrderIdAndDeleted(String orderId, boolean deleted);
    List<UserSubscriptionModel> findByStatusAndDeletedFalseAndOrderCompletedTrue(StatusEnum status);
    List<UserSubscriptionModel> findByStatusAndCreatedBetweenAndDeletedFalseAndOrderCompletedTrue(StatusEnum status, Date date1, Date date2);
    List<UserSubscriptionModel> findByCreatedBetweenAndDeletedFalseAndOrderCompletedTrue(Date date1, Date date2);
    List<UserSubscriptionModel> findByOrderIdAndDeletedFalseAndOrderCompletedTrue(String orderId);
    Long countByUserMobileAndUserDeletedFalseAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrue(String mobile, StatusEnum status, Date date1);
    UserSubscriptionModel findFirstByUserMobileAndUserDeletedFalseAndStatusAndStartDateAfterAndDeletedFalseAndOrderCompletedTrueOrderById(String mobile, StatusEnum status, Date date1);
    List<UserSubscriptionModel> findByUserMobileAndUserDeletedFalseAndStatusInAndOrderCompletedTrueAndDeletedFalseOrderById(String mobile, Collection<StatusEnum> statusCollection);
    UserSubscriptionModel findByUserMobileAndUserDeletedFalseAndStatusAndDeletedAndOrderCompletedTrue(String mobile, StatusEnum status, boolean deleted);
    UserSubscriptionModel findFirstByUserMobileAndUserDeletedFalseAndStatusInAndDeletedFalseAndOrderCompletedTrueOrderByIdDesc(String mobile, Set<StatusEnum> statusSet);
    UserSubscriptionModel findByUserMobileAndUserDeletedFalseAndStatusAndOrderCompletedTrue(String mobile, StatusEnum statusEnum);
    UserSubscriptionModel findByUserMobileAndUserDeletedFalseAndStatusAndDeleted(String mobile, StatusEnum status, boolean deleted);
    Long countByUserMobileAndUserDeletedFalseAndStatusAndDeletedFalseAndOrderCompletedTrue(String mobile, StatusEnum statusEnum);
}
