package com.til.prime.timesSubscription.enums;

import com.google.common.collect.Sets;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.Date;
import java.util.Set;

public enum StatusEnum {
    EXPIRED, ACTIVE, FUTURE, CANCELLED, ACTIVE_CANCELLED;

    public static Set<StatusEnum> VALID_TURN_OFF_DEBIT_STATUS_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.FUTURE);
    public static Set<StatusEnum> INVALID_BACKEND_SUBSCRIPTION_STATUS_SET = Sets.newHashSet(StatusEnum.ACTIVE_CANCELLED, StatusEnum.CANCELLED, StatusEnum.FUTURE);
    public static Set<StatusEnum> VALID_EXPIRY_STATUS_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.EXPIRED, StatusEnum.ACTIVE_CANCELLED);
    public static Set<StatusEnum> VALID_END_DATE_DISPLAY_STATUS_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.FUTURE, StatusEnum.EXPIRED, StatusEnum.ACTIVE_CANCELLED);
    public static Set<StatusEnum> VALID_CANCEL_STATUS_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.FUTURE);
    public static Set<StatusEnum> VALID_WORKING_STATUS_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.FUTURE);
    public static Set<StatusEnum> VALID_USABLE_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.EXPIRED, StatusEnum.FUTURE);
    public static Set<StatusEnum> VALID_INIT_STATUS_SET = Sets.newHashSet(StatusEnum.EXPIRED, StatusEnum.ACTIVE, StatusEnum.FUTURE, StatusEnum.ACTIVE_CANCELLED);
    public static Set<StatusEnum> VALID_USER_STATUS_HISTORY_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.EXPIRED, StatusEnum.CANCELLED, StatusEnum.ACTIVE_CANCELLED);
    public static Set<StatusEnum> VALID_EXTERNAL_PUBLISH_WITH_LAST_END_DATE_STATUS_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.CANCELLED, StatusEnum.EXPIRED, StatusEnum.ACTIVE_CANCELLED);
    public static Set<StatusEnum> VALID_EXTERNAL_PUBLISH_STATUS_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.EXPIRED, StatusEnum.ACTIVE_CANCELLED);
    public static Set<StatusEnum> VALID_SSO_UPDATE_STATUS_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.EXPIRED, StatusEnum.ACTIVE_CANCELLED);
    public static Set<StatusEnum> VALID_CACHE_UPDATE_WITH_LAST_END_DATE_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.EXPIRED, StatusEnum.CANCELLED, StatusEnum.ACTIVE_CANCELLED, StatusEnum.FUTURE);
    public static Set<StatusEnum> VALID_CACHE_UPDATE_SET = Sets.newHashSet(StatusEnum.ACTIVE, StatusEnum.EXPIRED, StatusEnum.ACTIVE_CANCELLED, StatusEnum.FUTURE);

    public static StatusEnum getStatusForUserSubscription(UserSubscriptionModel userSubscriptionModel, Date currentDate){
        return getStatusForUserSubscription(userSubscriptionModel.getStartDate(), userSubscriptionModel.getEndDate(), currentDate);
    }

    public static StatusEnum getStatusForUserSubscription(Date startDate, Date endDate, Date currentDate){
        if(currentDate==null){
            currentDate = new Date();
        }
        if(currentDate.getTime()<startDate.getTime()){
            return StatusEnum.FUTURE;
        }
        if(currentDate.getTime()>endDate.getTime()){
            return StatusEnum.EXPIRED;
        }
        if(currentDate.getTime()>=startDate.getTime() && currentDate.getTime()<=endDate.getTime()){
            return StatusEnum.ACTIVE;
        }
        return null;
    }
}
