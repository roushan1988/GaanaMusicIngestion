package com.til.prime.timesSubscription.constants;

public class RedisConstants {
    public static final String SSO_AUTH_CACHE_KEY = "ssoau";
    public static final String PRIME_STATUS_CACHE_KEY = "pst";
    public static final String USER_DETAILS_CACHE_KEY = "usr";
    public static final Long SSO_AUTH_CACHE_EXPIRY_SECS = 900L;
    public static final Long PRIME_STATUS_CACHE_EXPIRY_SECS = 15*24*60*60L;
    public static final Long USER_DETAILS_CACHE_EXPIRY_SECS = 15*24*60*60L;
}
