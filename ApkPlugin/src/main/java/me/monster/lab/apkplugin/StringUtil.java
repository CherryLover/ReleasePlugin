package me.monster.lab.apkplugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @description
 * @author: Created jiangjiwei in 2019-12-20 14:31
 */
public class StringUtil {
    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        return format.format(new Date(System.currentTimeMillis()));
    }
}
