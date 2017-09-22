package com.til.prime.timesSubscription.enums;

import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.Date;

public enum StatusEnum {
    EXPIRED, ACTIVE, FUTURE;

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
