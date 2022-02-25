package com.barco.common.utility;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.apache.logging.log4j.util.Strings.isNotBlank;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nabeel Ahmed
 */
@Component
@Scope("prototype")
public class BarcoUtil {

    public Logger logger = LogManager.getLogger(BarcoUtil.class);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE).matcher(emailStr);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean hasKeyValue(JsonObject jsonObj, String key) {
        return ((jsonObj.has(key) && jsonObj.get(key) != null && !jsonObj.get(key).isJsonNull()) &&
            ((jsonObj.get(key).isJsonObject() && !jsonObj.getAsJsonObject(key).entrySet().isEmpty()) ||
            (jsonObj.get(key).isJsonArray() && 0 < jsonObj.getAsJsonArray(key).size()) ||
            (jsonObj.get(key).isJsonPrimitive() && isNotBlank(jsonObj.get(key).getAsString()))));
    }

    public static boolean isNull(Object object) {
        if (object == null) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Long log) {
        if (log == null) {
            return true;
        }
        return false;
    }

    public static boolean isNull(String str) {
        if (str == null || str.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Boolean bool) {
        if (bool == null) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Double dou) {
        if (dou == null) {
            return true;
        }
        return false;
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