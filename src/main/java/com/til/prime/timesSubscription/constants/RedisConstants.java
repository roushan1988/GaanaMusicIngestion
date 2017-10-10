package com.til.prime.timesSubscription.constants;

public class RedisConstants {
    public static final String SSO_AUTH_CACHE_KEY = "ssoau";
    public static final String PRIME_STATUS_CACHE_KEY = "pst";
    public static final Long SSO_AUTH_CACHE_EXPIRY_SECS = 900L;
    public static final Long PRIME_STATUS_CACHE_EXPIRY_SECS = 10*365*24*60*60L;
}
