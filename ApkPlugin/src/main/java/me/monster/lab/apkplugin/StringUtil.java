package me.monster.lab.apkplugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @description
 * @author: Created jiangjiwei in 2019-12-20 14:31
 */
public class StringUtil {
    static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    static String getTimeDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        return format.format(new Date(System.currentTimeMillis()));
    }

    static String getTimeMinute() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);
        return format.format(new Date(System.currentTimeMillis()));
    }
}
