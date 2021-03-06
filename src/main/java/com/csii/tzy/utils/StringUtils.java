package com.csii.tzy.utils;

import java.util.List;

/**
 * @author lipu@csii.com.cn
 * @since 2018-03-28
 */
public class StringUtils {

    public static String getNotNull(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    public static boolean isEmpty(String value) {
        if (value == null || value.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isEmpty(List<?> value) {
        if (value == null || value.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(List<?> value) {
        return !isEmpty(value);
    }
}
