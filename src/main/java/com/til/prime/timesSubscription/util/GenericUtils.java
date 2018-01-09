package com.til.prime.timesSubscription.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericUtils {
    private static final Pattern VALID_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validEmail(CharSequence obj) {
        if (StringUtils.isNotEmpty(obj) && VALID_EMAIL_REGEX.matcher(obj).find()) {
            return true;
        }
        return false;
    }

    public static boolean validMobile(CharSequence obj) {
        if (StringUtils.isNotEmpty(obj)) {
            Pattern pattern = Pattern.compile("\\d{10}");
            Matcher matcher = pattern.matcher(obj.toString());
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }
}
