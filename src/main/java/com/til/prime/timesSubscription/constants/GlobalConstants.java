package com.til.prime.timesSubscription.constants;

import com.google.gson.Gson;

public class GlobalConstants {
    public static final String UTF_8 = "UTF-8";
    public static final String TYPE = "type";
    public static final String CHANNEL = "channel";
    public static final String SSOID = "ssoid";
    public static final String STATUS = "status";
    public static final String PLATFORM = "platform";
    public static final String SSO_VALIDATION_API_TYPE = "json";
    public static final String SSO_VALIDATE_API_URL_KEY = "sso.validate.ticket.url";
    public static final String SSO_UPDATE_PROFILE_URL_KEY = "sso.update.profile.url";
    public static final String TP_SITE_ID_KEY = "sso.tp.site.id";
    public static final String TP_CHANNEL_KEY = "sso.tp.channel";
    public static final String PAYMENTS_ENCRYPTION_KEY= "payments.encryption.key";
    public static final String TICKETID = "ticketId";
    public static final String SITEID = "siteId";
    public static final int ORDER_ID_LENGTH = 24;
    public static final String PAYMENT_REFERENCE_NA = "NA";
    public static final String PAYMENT_METHOD_NA = "NA";
    public static final int API_RETRY_COUNT = 3;
    public static final int DB_RETRY_COUNT = 3;
    public static final int SINGLE_TRY = 1;
    public static final String VALIDATION_FAILURE = "Certain Validations Failed";
    public static final String EXCEPTION_MESSAGE = "Oops! Something went wrong. Please try again.";
    public static final String EXCEPTION = "EXCEPTION";
    public static final int MAX_DAYS_DIFF_FOR_NEW_SUBSCRIPTION_PURCHASE = (365*2)+90;
    public static final String PAYMENTS_SECRET_KEY = "payments.secret.key";
    public static final long MAX_SUBSCRIPTION_EXTENSION_DAYS = 365l;
    public static final long MAX_BACKEND_SUBSCRIPTION_DAYS = 365l;
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String SUCCESS = "SUCCESS";
    public static final String OK = "OK";
    public static final Gson gson = new Gson();
    public static final Long CRON_BATCH_PROCESSING_COUNT= 500L;
    public static final String PAYMENTS_RENEW_SUBSCRIPTION_URL_KEY = "payments.renew.subscription.url";
    public static final String PAYMENTS_REFUND_URL_KEY = "payments.refund.url";
    public static final int PAYMENTS_SUCCESS_STATUS_CODE = 2000;
    public static final String SMS_ROUTING_KEY = "sms.routing.key";
    public static final String EMAIL_ROUTING_KEY = "email.routing.key";
    public static final String PAYMENTS_REQ_DELIM = "|";
    public static final String CRON_ENABLED = "cron.enabled";
    public static final String PARTNER_ID_FOR_COMMUNICATION = "TimesSubscription";
    public static final String MOBILE_UPDATE_TEMPLATE_KEY = "mobile.update.template.key";
    public static final String MOBILE_UPDATE_TEMPLATE_GROUP = "mobile.update.template.group";
    public static final String MOBILE_UPDATE_TEMPLATE_CTA_KEY = "mobile.update.template.cta.key";
    public static final String EMAIL_FOR_COMMUNICATION = "help@timesprime.com";
    public static final String EMAIL_NAME_FOR_COMMUNICATION = "Times Prime";
    public static final int BACKEND_ACTIVATION_CODE_LENGTH = 15;
    public static final String SUBSCRIPTION_SUCCESS_SMS_TEMPLATE_KEY = "subscription.success.sms.key";
    public static final String SUBSCRIPTION_SUCCESS_EMAIL_TEMPLATE_KEY = "subscription.success.email.key";
    public static final String BACKEND_ACTIVATION_SMS_TEMPLATE_KEY = "backend.activation.sms.key";
    public static final String BACKEND_ACTIVATION_EMAIL_TEMPLATE_KEY = "backend.activation.sms.key";
    public static final String BACKEND_ACTIVATION_EMAIL_GROUP_KEY = "backend.activation.email.group.key";
    public static final String BACKEND_ACTIVATION_EMAIL_CTA_KEY = "backend.activation.email.cta.key";
    public static final String URL_SHORTENING_API_KEY = "url.shortening.api";
    public static final String PRIME_BACKEND_ACTIVATION_URL_KEY = "prime.backend.activation.url";
    public static final String PRIME_COMM_FROM_NAME = "TimesPrime";
    public static final String PRIME_COMM_FROM_EMAIL = "info@timesprime.com";

    public static final String FREE_TRAIL_ACTIVATION_SMS_TEMPLATE_KEY = "free.trail.activation.sms.key";
    public static final String FIRST_TIME_PURCHASE_SMS_TEMPLATE_KEY = "first.time.purchase.sms.key";
    public static final String FREE_TRAIL_EXPIRED_SMS_TEMPLATE_KEY = "free.trail.expired.sms.key";
    public static final String SUBSCRIPTION_EXPIRED_SMS_TEMPLATE_KEY = "subscription.expired.sms.key";
    public static final String SUBSCRIPTION_RENEWED_SMS_TEMPLATE_KEY = "subscription.renewed.sms.key";
    public static final String SUBSCRIPTION_EXTENDED_SMS_TEMPLATE_KEY = "subscription.extended.sms.key";

    public static final String FREE_TRAIL_EXPIRY_REMINDER_SMS_TEMPLATE_KEY = "free.trail.expiry.reminder.sms.key";
    public static final String SUBSCRIPTION_EXPIRY_REMINDER_SMS_TEMPLATE_KEY = "subscription.expiry.reminder.sms.key";
    public static final String SUBSCRIPTION_EXPIRY_AUTO_DEBIT_REMINDER_SMS_TEMPLATE_KEY = "subscription.expiry.auto.debit.reminder.sms.key";

    public static final String FREE_TRAIL_ACTIVATION_EMAIL_TEMPLATE_KEY = "free.trail.activation.email.key";
    public static final String FIRST_TIME_PURCHASE_EMAIL_TEMPLATE_KEY = "first.time.purchase.email.key";
    public static final String FREE_TRAIL_EXPIRED_EMAIL_TEMPLATE_KEY = "free.trail.expired.email.key";
    public static final String SUBSCRIPTION_EXPIRED_EMAIL_TEMPLATE_KEY = "subscription.expired.email.key";
    public static final String SUBSCRIPTION_RENEWED_EMAIL_TEMPLATE_KEY = "subscription.renewed.email.key";
    public static final String SUBSCRIPTION_EXTENDED_EMAIL_TEMPLATE_KEY = "subscription.extended.email.key";

    public static final String FREE_TRAIL_EXPIRY_REMINDER_EMAIL_TEMPLATE_KEY = "free.trail.expiry.reminder.email.key";
    public static final String SUBSCRIPTION_EXPIRY_REMINDER_EMAIL_TEMPLATE_KEY = "subscription.expiry.reminder.email.key";
    public static final String SUBSCRIPTION_EXPIRY_AUTO_DEBIT_REMINDER_EMAIL_TEMPLATE_KEY = "subscription.expiry.auto.debit.reminder.email.key";

}
