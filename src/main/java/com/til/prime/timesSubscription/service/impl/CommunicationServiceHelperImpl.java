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

    @Override
    public SMSTask getFreeTrialSubscriptionSMSTask(UserSubscriptionModel userSubscriptionModel) {
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber(userSubscriptionModel.getUser().getMobile());
        smsTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        smsTask.setTemplateKey(properties.getProperty(GlobalConstants.FREE_TRAIL_ACTIVATION_SMS_TEMPLATE_KEY));
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("endDate", SDF.format(userSubscriptionModel.getEndDate()));
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

    @Override
    public EmailTask getFreeTrialSubscriptionEmailTask(UserSubscriptionModel userSubscriptionModel) {
        EmailTask emailTask = new EmailTask();
        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.FREE_TRAIL_ACTIVATION_EMAIL_TEMPLATE_KEY));
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setEmailId(userSubscriptionModel.getUser().getEmail());
        emailTask.setFromName(GlobalConstants.PRIME_COMM_FROM_NAME);
        emailTask.setFromEmail(GlobalConstants.PRIME_COMM_FROM_EMAIL);
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("endDate", SDF.format(userSubscriptionModel.getEndDate()));
        emailTask.setContext(map);
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

    @Override
    public SMSTask getFreeTrialExpiryReminderSMSTask(UserSubscriptionModel userSubscriptionModel, Long days) {
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber(userSubscriptionModel.getUser().getMobile());
        smsTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        smsTask.setTemplateKey(properties.getProperty(GlobalConstants.FREE_TRAIL_EXPIRY_REMINDER_SMS_TEMPLATE_KEY));
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("subscriptionAmount", String.valueOf(userSubscriptionModel.getSubscriptionVariant().getPrice()));
        map.put("daysRemaining", String.valueOf(days));
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

    @Override
    public EmailTask getFreeTrialExpiryReminderEmailTask(UserSubscriptionModel userSubscriptionModel, Long days) {
        EmailTask emailTask = new EmailTask();
        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.FREE_TRAIL_EXPIRY_REMINDER_EMAIL_TEMPLATE_KEY));
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setEmailId(userSubscriptionModel.getUser().getEmail());
        emailTask.setFromName(GlobalConstants.PRIME_COMM_FROM_NAME);
        emailTask.setFromEmail(GlobalConstants.PRIME_COMM_FROM_EMAIL);
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("subscriptionAmount", String.valueOf(userSubscriptionModel.getSubscriptionVariant().getPrice()));
        map.put("daysRemaining", String.valueOf(days));
        emailTask.setContext(map);
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

    @Override
    public SMSTask getFreeTrialExpiredSMSTask(UserSubscriptionModel userSubscriptionModel) {
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber(userSubscriptionModel.getUser().getMobile());
        smsTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        smsTask.setTemplateKey(properties.getProperty(GlobalConstants.FREE_TRAIL_EXPIRED_SMS_TEMPLATE_KEY));
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("subscriptionAmount", String.valueOf(userSubscriptionModel.getSubscriptionVariant().getPrice()));
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

    @Override
    public EmailTask getFreeTrialExpiredEmailTask(UserSubscriptionModel userSubscriptionModel) {
        EmailTask emailTask = new EmailTask();
        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.FREE_TRAIL_EXPIRED_EMAIL_TEMPLATE_KEY));
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setEmailId(userSubscriptionModel.getUser().getEmail());
        emailTask.setFromName(GlobalConstants.PRIME_COMM_FROM_NAME);
        emailTask.setFromEmail(GlobalConstants.PRIME_COMM_FROM_EMAIL);
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("subscriptionAmount", String.valueOf(userSubscriptionModel.getSubscriptionVariant().getPrice()));
        emailTask.setContext(map);
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

    @Override
    public SMSTask getFirstTimePurchaseSMSTask(UserSubscriptionModel userSubscriptionModel) {
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber(userSubscriptionModel.getUser().getMobile());
        smsTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        smsTask.setTemplateKey(properties.getProperty(GlobalConstants.FIRST_TIME_PURCHASE_SMS_TEMPLATE_KEY));
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("endDate", SDF.format(userSubscriptionModel.getEndDate()));
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

    @Override
    public EmailTask getFirstTimePurchaseEmailTask(UserSubscriptionModel userSubscriptionModel) {
        EmailTask emailTask = new EmailTask();
        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.FIRST_TIME_PURCHASE_EMAIL_TEMPLATE_KEY));
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setEmailId(userSubscriptionModel.getUser().getEmail());
        emailTask.setFromName(GlobalConstants.PRIME_COMM_FROM_NAME);
        emailTask.setFromEmail(GlobalConstants.PRIME_COMM_FROM_EMAIL);
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("endDate", SDF.format(userSubscriptionModel.getEndDate()));
        emailTask.setContext(map);
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

    @Override
    public SMSTask getSubscriptionRenewedSMSTask(UserSubscriptionModel userSubscriptionModel) {
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber(userSubscriptionModel.getUser().getMobile());
        smsTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        smsTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_RENEWED_SMS_TEMPLATE_KEY));
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("endDate", SDF.format(userSubscriptionModel.getEndDate()));
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

    @Override
    public EmailTask getSubscriptionRenewedEmailTask(UserSubscriptionModel userSubscriptionModel) {
        EmailTask emailTask = new EmailTask();
        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_RENEWED_EMAIL_TEMPLATE_KEY));
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setEmailId(userSubscriptionModel.getUser().getEmail());
        emailTask.setFromName(GlobalConstants.PRIME_COMM_FROM_NAME);
        emailTask.setFromEmail(GlobalConstants.PRIME_COMM_FROM_EMAIL);
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("endDate", SDF.format(userSubscriptionModel.getEndDate()));
        emailTask.setContext(map);
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

    @Override
    public SMSTask getSubscriptionExtensionSMSTask(UserSubscriptionModel userSubscriptionModel) {
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber(userSubscriptionModel.getUser().getMobile());
        smsTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        smsTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_EXTENDED_SMS_TEMPLATE_KEY));
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("endDate", SDF.format(userSubscriptionModel.getEndDate()));
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

    @Override
    public EmailTask getSubscriptionExtensionEmailTask(UserSubscriptionModel userSubscriptionModel) {
        EmailTask emailTask = new EmailTask();
        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_EXTENDED_EMAIL_TEMPLATE_KEY));
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setEmailId(userSubscriptionModel.getUser().getEmail());
        emailTask.setFromName(GlobalConstants.PRIME_COMM_FROM_NAME);
        emailTask.setFromEmail(GlobalConstants.PRIME_COMM_FROM_EMAIL);
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("endDate", SDF.format(userSubscriptionModel.getEndDate()));
        emailTask.setContext(map);
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

    @Override
    public SMSTask getSubscriptionExpiredSMSTask(UserSubscriptionModel userSubscriptionModel) {
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber(userSubscriptionModel.getUser().getMobile());
        smsTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        smsTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_EXPIRED_SMS_TEMPLATE_KEY));
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("renewalAmount", String.valueOf(userSubscriptionModel.getSubscriptionVariant().getPrice()));
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

    @Override
    public EmailTask getSubscriptionExpiredEmailTask(UserSubscriptionModel userSubscriptionModel) {
        EmailTask emailTask = new EmailTask();
        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_EXPIRED_EMAIL_TEMPLATE_KEY));
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setEmailId(userSubscriptionModel.getUser().getEmail());
        emailTask.setFromName(GlobalConstants.PRIME_COMM_FROM_NAME);
        emailTask.setFromEmail(GlobalConstants.PRIME_COMM_FROM_EMAIL);
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("renewalAmount", String.valueOf(userSubscriptionModel.getSubscriptionVariant().getPrice()));
        emailTask.setContext(map);
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

    @Override
    public SMSTask getSubscriptionRenewalReminderSMSTask(UserSubscriptionModel userSubscriptionModel, boolean autoDebit) {
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber(userSubscriptionModel.getUser().getMobile());
        smsTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("endDate", SDF.format(userSubscriptionModel.getEndDate()));
        map.put("renewalAmount", String.valueOf(userSubscriptionModel.getSubscriptionVariant().getPrice()));
        smsTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_EXPIRY_REMINDER_SMS_TEMPLATE_KEY));
        if(autoDebit){
            smsTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_EXPIRY_AUTO_DEBIT_REMINDER_SMS_TEMPLATE_KEY));
            map.put("pmtInstrument", userSubscriptionModel.getPaymentMethod());
        }
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return smsTask;
    }

    @Override
    public EmailTask getSubscriptionRenewalReminderEmailTask(UserSubscriptionModel userSubscriptionModel, boolean autoDebit) {
        EmailTask emailTask = new EmailTask();
        emailTask.setPartnerId(GlobalConstants.PARTNER_ID_FOR_COMMUNICATION);
        emailTask.setEmailId(userSubscriptionModel.getUser().getEmail());
        emailTask.setFromName(GlobalConstants.PRIME_COMM_FROM_NAME);
        emailTask.setFromEmail(GlobalConstants.PRIME_COMM_FROM_EMAIL);
        Map<String, String> map = Maps.newHashMap();
        map.put("firstName", userSubscriptionModel.getUser().getName());
        map.put("endDate", SDF.format(userSubscriptionModel.getEndDate()));
        map.put("renewalAmount", String.valueOf(userSubscriptionModel.getSubscriptionVariant().getPrice()));

        emailTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_EXPIRY_REMINDER_EMAIL_TEMPLATE_KEY));
        if(autoDebit){
            emailTask.setTemplateKey(properties.getProperty(GlobalConstants.SUBSCRIPTION_EXPIRY_AUTO_DEBIT_REMINDER_EMAIL_TEMPLATE_KEY));
            map.put("pmtInstrument", userSubscriptionModel.getPaymentMethod());
        }

        emailTask.setContext(map);
        emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        return emailTask;
    }

}
