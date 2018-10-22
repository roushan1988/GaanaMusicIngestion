package helper;

import com.til.prime.timesSubscription.dto.external.ValuePropDTO;
import com.til.prime.timesSubscription.enums.PlanStatusEnum;
import com.til.prime.timesSubscription.enums.PlanTypeEnum;
import com.til.prime.timesSubscription.enums.StatusEnum;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.util.TimeUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

import static com.til.prime.timesSubscription.constants.GlobalConstants.*;
import static java.text.MessageFormat.format;

public class PlanStatusHelper {
    public static ValuePropDTO getPlanTexts(ValuePropDTO valuePropDTO, PlanStatusEnum planStatusEnum, Date endDate, Date lastEndDate, String channel, String price, Long daysForPayment) {
        String message = null;
        String vpHeading = null;
        String vpSubHading = null;
        String sdkCta = null;
        String ctaLarge = null;
        String ctaSmall = null;
        boolean payment = false;
        boolean redirectToParent = false;
        if (valuePropDTO == null) {
            valuePropDTO = new ValuePropDTO();
        }
        Date date = new Date();
        switch (planStatusEnum) {
            case INIT:
                vpHeading = PAYMENT_VP_INIT_HEADING;
                vpSubHading = PAYMENT_VP_INIT_SUBHEADING;
                sdkCta = PAYMENT_SUBSCRIPTION_FREE_CTA;
                ctaSmall = PAYMENT_SUBSCRIPTION_FREE_CTA_SMALL;
                ctaLarge = PAYMENT_SUBSCRIPTION_FREE_CTA;
                break;
            case FREE_TRIAL:
                message = format("Your subscription will expire on {0}", DateFormatUtils.format(lastEndDate, "dd-MMM-yyyy"));
                if (DateUtils.isSameDay(endDate, lastEndDate)) {
                    vpHeading = format(PAYMENT_VP_HEADING, price);
                    vpSubHading = PAYMENT_VP_SUBHEADING;
                    sdkCta = PAYMENT_CTA;
                    payment = true;
                    ctaSmall = PAYMENT_SUBSCRIPTION_CTA;
                    ctaLarge = format(PAYMENT_SUBSCRIPTION_CTA_LARGE, price);
                } else {
                    sdkCta = format("Continue to {0}", channel);
                    redirectToParent = true;
                    vpHeading = PAYMENT_VP_TP_HEADING;
                    vpSubHading = PAYMENT_VP_TP_SUBHEADING;
                }
                break;
            case FREE_TRIAL_EXPIRED:
                message = format("Your free trial has been expired");
                vpHeading = format(PAYMENT_VP_HEADING, price);
                vpSubHading = PAYMENT_VP_SUBHEADING;
                sdkCta = PAYMENT_CTA;
                payment = true;
                ctaSmall = PAYMENT_SUBSCRIPTION_CTA;
                ctaLarge = format(PAYMENT_SUBSCRIPTION_CTA_LARGE, price);
                break;
            case FREE_TRIAL_WITH_PAYMENT:
                message = format("Your subscription will expire on {0}", DateFormatUtils.format(lastEndDate, "dd-MMM-yyyy"));
                if (TimeUtils.getDifferenceInDays(lastEndDate, date) < daysForPayment) {
                    vpHeading = format(PAYMENT_VP_HEADING_3, TimeUtils.getDifferenceInDays(lastEndDate, date));
                    vpSubHading = format(PAYMENT_VP_SUBHEADING_3, price);
                    payment = true;
                    sdkCta = PAYMENT_RENEW_CTA;
                    ctaSmall = PAYMENT_RENEW_CTA;
                    ctaLarge = format(PAYMENT_SUBSCRIPTION_CTA_LARGE, price);
                } else {
                    sdkCta = format("Continue to {0}", channel);
                    redirectToParent = true;
                    vpHeading = PAYMENT_VP_TP_HEADING;
                    vpSubHading = PAYMENT_VP_TP_SUBHEADING;

                }
                break;
            case FREE_TRIAL_WITH_PAYMENT_EXPIRED:
                message = format("Your free trial has been expired");
                vpHeading = format(PAYMENT_VP_HEADING, price);
                vpSubHading = PAYMENT_VP_SUBHEADING;
                sdkCta = PAYMENT_CTA;
                payment = true;
                ctaSmall = PAYMENT_SUBSCRIPTION_CTA;
                ctaLarge = format(PAYMENT_SUBSCRIPTION_CTA_LARGE, price);
                break;
            case SUBSCRIPTION:
                message = format("Your subscription will expire on {0}", DateFormatUtils.format(lastEndDate, "dd-MMM-yyyy"));
                if (TimeUtils.getDifferenceInDays(lastEndDate, date) < daysForPayment) {
                    vpHeading = format(PAYMENT_VP_HEADING_3, TimeUtils.getDifferenceInDays(lastEndDate, date));
                    vpSubHading = format(PAYMENT_VP_SUBHEADING_3, price);
                    payment = true;
                    sdkCta = PAYMENT_RENEW_CTA;
                    ctaSmall = PAYMENT_RENEW_CTA;
                    ctaLarge = PAYMENT_RENEW_CTA;

                } else {
                    vpHeading = PAYMENT_VP_TP_HEADING;
                    vpSubHading = PAYMENT_VP_TP_SUBHEADING;
                    sdkCta = format("Continue to {0}", channel);
                    redirectToParent = true;

                }
                break;
            case SUBSCRIPTION_EXPIRED:
                message = format("Your subscription has been expired");
                vpHeading = format(PAYMENT_VP_HEADING, price);
                vpSubHading = PAYMENT_VP_SUBHEADING;
                sdkCta = PAYMENT_CTA;
                payment = true;
                ctaSmall = PAYMENT_SUBSCRIPTION_CTA;
                ctaLarge = format(PAYMENT_SUBSCRIPTION_CTA_LARGE, price);
                break;
            case SUBSCRIPTION_CANCELLED:
                message = format("Your subscription has been cancelled");
                vpHeading = format(PAYMENT_VP_HEADING, price);
                vpSubHading = PAYMENT_VP_SUBHEADING_2;
                sdkCta = PAYMENT_CTA;
                payment = true;
                ctaSmall = PAYMENT_SUBSCRIPTION_CTA;
                ctaLarge = format(PAYMENT_SUBSCRIPTION_CTA_LARGE, price);
                break;
            case SUBSCRIPTION_AUTO_RENEWAL:
                message = format("Your subscription will expire on {0}", DateFormatUtils.format(lastEndDate, "dd-MMM-yyyy"));
                vpHeading = PAYMENT_VP_TP_HEADING;
                vpSubHading = PAYMENT_VP_TP_SUBHEADING;
                if (TimeUtils.getDifferenceInDays(lastEndDate, date) < daysForPayment) {
                    vpHeading = format(PAYMENT_VP_HEADING_3, TimeUtils.getDifferenceInDays(lastEndDate, date));
                    vpSubHading = format(PAYMENT_VP_SUBHEADING_3, price);
                    payment = true;
                    sdkCta = PAYMENT_RENEW_CTA;
                    ctaSmall = sdkCta;
                    ctaLarge = format(PAYMENT_SUBSCRIPTION_CTA_LARGE, price);

                } else {
                    sdkCta = format("Continue to {0}", channel);
                    redirectToParent = true;

                }
                break;
            case BLOCKED:
                vpHeading = "You are not allowed to access Times Prime membership";
                vpSubHading = "please reach out to support@timesprime.com to activate your account";
                break;

        }
        valuePropDTO.setHeading(vpHeading);
        valuePropDTO.setSubHeading(vpSubHading);
        valuePropDTO.setSdkCta(sdkCta);
        valuePropDTO.setPayment(payment);
        valuePropDTO.setLargeCta(ctaLarge);
        valuePropDTO.setSmallCta(ctaSmall);
        valuePropDTO.setMessage(message);
        valuePropDTO.setRedirectToParent(redirectToParent);
        return valuePropDTO;
    }

    public static PlanStatusEnum getPlanStatus(StatusEnum status, PlanTypeEnum planTypeEnum, BigDecimal price, UserSubscriptionModel lastUserSubscription, boolean autoRenewal){
        if(status==StatusEnum.CANCELLED || status==StatusEnum.ACTIVE_CANCELLED){
            return PlanStatusEnum.SUBSCRIPTION_CANCELLED;
        }else if(status==StatusEnum.EXPIRED){
            if(PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planTypeEnum)){
                if(price.compareTo(BigDecimal.ZERO)<=0){
                    return PlanStatusEnum.FREE_TRIAL_EXPIRED;
                }else{
                    return PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT_EXPIRED;
                }
            }else{
                return PlanStatusEnum.SUBSCRIPTION_EXPIRED;
            }
        }else{
            if(PlanTypeEnum.USAGE_RESTRICTED_PLANS_TYPES.contains(planTypeEnum)){
                if(price.compareTo(BigDecimal.ZERO)<=0){
                    return PlanStatusEnum.FREE_TRIAL;
                }else{
                    return PlanStatusEnum.FREE_TRIAL_WITH_PAYMENT;
                }
            }else if(price.compareTo(BigDecimal.ZERO)>0){
                if(autoRenewal && lastUserSubscription!=null && (lastUserSubscription.getPlanStatus()==PlanStatusEnum.SUBSCRIPTION || lastUserSubscription.getPlanStatus()==PlanStatusEnum.SUBSCRIPTION_AUTO_RENEWAL)){
                    return PlanStatusEnum.SUBSCRIPTION_AUTO_RENEWAL;
                }else{
                    return PlanStatusEnum.SUBSCRIPTION;
                }
            }
            return PlanStatusEnum.SUBSCRIPTION;
        }
    }
}
