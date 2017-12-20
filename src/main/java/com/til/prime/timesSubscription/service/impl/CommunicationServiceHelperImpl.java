package com.til.prime.timesSubscription.service.impl;

import com.google.common.collect.Maps;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.SMSTask;
import com.til.prime.timesSubscription.enums.TaskPriorityEnum;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.service.CommunicationServiceHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class CommunicationServiceHelperImpl implements CommunicationServiceHelper {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");
    @Resource(name = "config_properties")
    private Properties properties;
    @Override
    public EmailTask getUserMobileUpdateEmailTask(UserModel userModel, List<UserSubscriptionModel> userSubscriptionModels) {
        EmailTask emailTask = new EmailTask();
        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.MOBILE_UPDATE_TEMPLATE_KEY));
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setGroup(properties.getProperty(GlobalConstants.MOBILE_UPDATE_TEMPLATE_GROUP));
        emailTask.setEmailId(userModel.getEmail());
        emailTask.setFromName(GlobalConstants.EMAIL_NAME_FOR_COMMUNICATION);
        emailTask.setFromEmail(GlobalConstants.EMAIL_FOR_COMMUNICATION);
        Map<String, String> map = Maps.newHashMap();
        map.put("startDate", SDF.format(userSubscriptionModels.get(0).getStartDate()));
        map.put("endDate", SDF.format(userSubscriptionModels.get(userSubscriptionModels.size()-1).getEndDate()));
        map.put("mobile", userModel.getMobile());
        emailTask.setContext(map);
        emailTask.setCtaKey(properties.getProperty(GlobalConstants.MOBILE_UPDATE_TEMPLATE_CTA_KEY));
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

    @Override
    public EmailTask getSubscriptionSuccessEmailTask(UserSubscriptionModel userSubscription) {
        EmailTask emailTask = new EmailTask();
        emailTask.setTemplateKey("SUBSCRIPTION_SUCCESS_EMAIL");
        emailTask.setPartnerId("TimesSubscription");
        emailTask.setEmailId(userSubscription.getUser().getEmail());
        emailTask.setFromName("TimesPrime");
        emailTask.setFromEmail("info@timesprime.com");
        Map<String, String> map = Maps.newHashMap();
        map.put("name", userSubscription.getUser().getName());
        map.put("endDate", SDF.format(userSubscription.getEndDate()));
        emailTask.setContext(map);
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

    @Override
    public SMSTask getSubscriptionSuccessSMSTask(UserSubscriptionModel userSubscription) {
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber(userSubscription.getUser().getMobile());
        smsTask.setPartnerId("TimesSubscription");
        smsTask.setTemplateKey("SUBSCRIPTION_SUCCESS_SMS");
        Map<String, String> map = Maps.newHashMap();
        map.put("name", userSubscription.getUser().getName());
        map.put("endDate", SDF.format(userSubscription.getEndDate()));
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

}
