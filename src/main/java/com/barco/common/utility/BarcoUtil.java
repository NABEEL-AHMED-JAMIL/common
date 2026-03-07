package com.barco.common.utility;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collection;

/**
 * @author Nabeel Ahmed
 */
public final class BarcoUtil {

    public static final String Authorization = "Authorization";
    public static final String KID_ID = "kid";
    // Use for handle the request for the user by admin
    public static final String X_USER_ID = "X-User-Id";
    // Use for handle the request for the user by admin
    public static final String X_TENANT_ID = "X-Tenant-Id";

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE).matcher(emailStr);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNull(Object payload) {
        return payload == null || payload == "" ? true : false;
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNull(Long log) {
        if (log == null) {
            return true;
        }
        return false;
    }

    public static boolean isNull(String str) {
        return (str == null || str.trim().isEmpty()) ? true : false;
    }

    public static boolean isNull(Boolean bool) {
        return (bool == null) ? true : false;
    }

    public static boolean isNull(Double dou) {
        return (dou == null) ? true : false;
    }

    public static boolean isNull(Date dt) {
        if (dt == null) {
            return true;
        } else if (String.valueOf(dt) == null) {
            return true;
        } else if (String.valueOf(dt).trim().length() <= 0) {
            return true;
        }
        return false;
    }

    // New helper to check collections (lists) for null or empty
    public static boolean isNullOrEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    // New helper for convenience
    public static boolean isNotNull(Object payload) {
        return !isNull(payload);
    }

}