package com.til.prime.timesSubscription.service.impl;

import com.google.common.collect.Maps;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.SMSTask;
import com.til.prime.timesSubscription.enums.TaskPriorityEnum;
import com.til.prime.timesSubscription.model.BackendSubscriptionUserModel;
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
        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_SUCCESS_EMAIL_TEMPLATE_KEY));
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setEmailId(userSubscription.getUser().getEmail());
        emailTask.setFromName(GlobalConstants.PRIME_COMM_FROM_NAME);
        emailTask.setFromEmail(GlobalConstants.PRIME_COMM_FROM_EMAIL);
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
        smsTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        smsTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_SUCCESS_SMS_TEMPLATE_KEY));
        Map<String, String> map = Maps.newHashMap();
        map.put("name", userSubscription.getUser().getName());
        map.put("endDate", SDF.format(userSubscription.getEndDate()));
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

    @Override
    public SMSTask getBackendActivationSMSTask(BackendSubscriptionUserModel user) {
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber(user.getMobile());
        smsTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        smsTask.setTemplateKey(properties.getProperty(GlobalConstants.BACKEND_ACTIVATION_SMS_TEMPLATE_KEY));
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", user.getFirstName());
        map.put("url", user.getShortenedUrl());
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

    @Override
    public EmailTask getBackendActivationEmailTask(BackendSubscriptionUserModel user) {
        EmailTask emailTask = new EmailTask();
        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.BACKEND_ACTIVATION_EMAIL_TEMPLATE_KEY));
        emailTask.setCtaKey(properties.getProperty(GlobalConstants.BACKEND_ACTIVATION_EMAIL_CTA_KEY));
        emailTask.setGroup(properties.getProperty(GlobalConstants.BACKEND_ACTIVATION_EMAIL_GROUP_KEY));
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setEmailId(user.getEmail());
        emailTask.setFromName(GlobalConstants.PRIME_COMM_FROM_NAME);
        emailTask.setFromEmail(GlobalConstants.PRIME_COMM_FROM_EMAIL);
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", user.getFirstName());
        map.put("url", user.getShortenedUrl());
        emailTask.setContext(map);
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

}
