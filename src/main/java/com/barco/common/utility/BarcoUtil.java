package com.barco.common.utility;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nabeel Ahmed
 */
@Component
public class BarcoUtil {

    public static Logger logger = LogManager.getLogger(BarcoUtil.class);

    public static Object NULL = null;
    public static String XRHK = "XRHK-Authorization";
    public static String XSHK = "XSHK-Authorization";
    public static String START_DATE = " 00:00:00";
    public static String END_DATE = " 23:59:59";
    public static String SIMPLE_DATE_PATTERN = "yyyy-MM-dd";
    public static String CONTENT_DISPOSITION ="Content-Disposition";
    public static String FILE_NAME_HEADER = "attachment; filename=";
    public static String SELECT = "select";
    public static String ERROR = "ERROR";
    public static String SUCCESS = "SUCCESS";
    public static String JOB_ADD = "Job-Add";
    private static final String[] IP_HEADERS = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"
    };

    public static void timeZoneUtil() {
        String[]ids = TimeZone.getAvailableIDs();
        for (String id:ids) {
            logger.info(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        logger.info("Total TimeZone ID " + ids.length);
    }

    private static String displayTimeZone(TimeZone tz) {
        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);
        String result = "";
        if (hours > 0) {
            result = String.format("(GMT+%d:%02d) %s :: %s", hours, minutes, tz.getID(), tz.getDisplayName());
        } else {
            result = String.format("(GMT%d:%02d) %s :: %s", hours, minutes, tz.getID(), tz.getDisplayName());
        }
        return result;
    }

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE).matcher(emailStr);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getRequestIP(HttpServletRequest request) {
        for (String header: IP_HEADERS) {
            String value = request.getHeader(header);
            logger.info(String.format("Header Name %s & Header Value %s.", header, value));
            if (value == null || value.isEmpty()) {
                continue;
            }
            String[] parts = value.split("\\s*,\\s*");
            return parts[0];
        }
        logger.info(String.format("Header Name Remote-Address & Header Value %s.", request.getRemoteAddr()));
        return request.getRemoteAddr();
    }

    public static Boolean hasKeyValue(JsonObject jsonObj, String key) {
        return ((jsonObj.has(key) && jsonObj.get(key) != null && !jsonObj.get(key).isJsonNull()) &&
            ((jsonObj.get(key).isJsonObject() && !jsonObj.getAsJsonObject(key).entrySet().isEmpty()) ||
            (jsonObj.get(key).isJsonArray() && 0 < jsonObj.getAsJsonArray(key).size()) ||
            (jsonObj.get(key).isJsonPrimitive() && isNotBlank(jsonObj.get(key).getAsString()))));
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

}