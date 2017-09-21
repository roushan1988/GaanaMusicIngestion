package com.til.prime.timesSubscription.util;

public class ExceptionUtils {
    public static RuntimeException wrapInRuntimeExceptionIfNecessary(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        } else {
            return new RuntimeException(throwable);
        }
    }

    public static String getStackTrace(Throwable th) {
        return org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(th);
    }
}
