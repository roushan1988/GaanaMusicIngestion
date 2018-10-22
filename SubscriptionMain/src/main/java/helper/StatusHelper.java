package helper;

import com.til.prime.timesSubscription.enums.StatusEnum;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.Date;

public class StatusHelper {
    public static StatusEnum getStatusForUserSubscription(UserSubscriptionModel userSubscriptionModel, Date currentDate){
        return StatusEnum.getStatusForUserSubscription(userSubscriptionModel.getStartDate(), userSubscriptionModel.getEndDate(), currentDate);
    }
}
