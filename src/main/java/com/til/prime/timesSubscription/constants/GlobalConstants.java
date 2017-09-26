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
    public static final int ORDER_ID_LENGTH = 32;
    public static final String PAYMENT_REFERENCE_NA = "NA";
    public static final String PAYMENT_METHOD_NA = "NA";
    public static final int API_RETRY_COUNT = 3;
    public static final int DB_RETRY_COUNT = 3;
    public static final int SINGLE_TRY = 1;
    public static final String VALIDATION_FAILURE = "Certain Validations Failed";
    public static final String EXCEPTION_MESSAGE = "Oops! Something wrong occured";
    public static final String EXCEPTION = "Exception";
    public static final int MAX_DAYS_DIFF_FOR_NEW_SUBSCRIPTION_PURCHASE = (365*2)+90;
    public static final String PAYMENTS_SECRET_KEY = "payments.secret.key";
    public static final long MAX_SUBSCRIPTION_EXTENSION_DAYS = 180l;
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String SUCCESS = "SUCCESS";
    public static final String OK = "OK";
    public static final Gson gson = new Gson();
    public static final Long CRON_BATCH_PROCESSING_COUNT= 500L;
    public static final String EMAIL_PROTOCOL= "email.protocol";
    public static final String EMAIL_HOST= "email.host";
    public static final String EMAIL_PORT= "email.port";
    public static final String EMAIL_USERNAME= "email.username";
    public static final String EMAIL_PASSWORD= "email.password";
    public static final String PAYMENTS_RENEW_SUBSCRIPTION_URL_KEY = "payments.renew.subscription.url";
}
