package com.mc.app.hotel.common.util;

import android.text.TextUtils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {

    public static final String ALPHANUMERIC_PATTER = "^[a-zA-Z0-9]*$";


    /**
     * 判断数据是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        if (null == str || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isNull(CharSequence str) {
        if (null == str || str.toString().length() < 1 || "null".equals(str.toString())) {
            return true;
        }
        return false;
    }

    /**
     * @param obj
     * @return
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection<?>)
            return ((Collection<?>) obj).isEmpty();

        if (obj instanceof Map<?, ?>)
            return ((Map<?, ?>) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    /**
     * 判断数据是否为空 “null”字符串不做空处理
     *
     * @param data
     * @return
     */
    public static boolean isBlank(String data) {
        return data == null || data.trim().isEmpty();
    }

    /**
     * 判断是否为纯数字字母英文字符
     *
     * @param content
     * @return
     */
    public static boolean isAlphanumeric(String content) {
        if (content == null) return false;

        return content.matches(ALPHANUMERIC_PATTER);
    }

    // 过滤特殊字符
    public static String stringFilter(String str) throws PatternSyntaxException {
        try {
            // 只允许字母和数字
            // String regEx = "[^a-zA-Z0-9]";
            // 清除掉所有特殊字符
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);

            str = m.replaceAll("").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 获取字符串
     *
     * @param string
     * @return
     */
    public static String getString(String string) {
        return string == null ? "" : string;
    }

    public static String getString(Object string) {
        return string == null ? "" : string.toString();
    }


    /**
     * 获取字符串，空显示“未设置”
     *
     * @param content
     * @return
     */
    public static String returnNotSetIfEmpty(String content) {
        return isBlank(content) ? "未设置" : content;
    }

    /**
     * 空返回0
     *
     * @param s
     * @return
     */
    public static int getStringLength(String s) {
        return s == null ? 0 : s.length();
    }


    /**
     * 将名字中间的字符转为*
     *
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static String string2Asterisk(String str, int start, int end) {
        if (!TextUtils.isEmpty(str) && str.length() >= end) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if ((i >= start && i < end) ||
                        (i >= 1 && str.length() == 2)) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return str + "*";
    }

    public static String string2MidAsterisk(String str) {
        StringBuilder sb = new StringBuilder(str);
        if (StringUtil.isNull(str))
            return "***";
        if (str.length() == 1)
            return str + "*";
        if (str.length() == 2)
            return str.substring(0, 1) + "*";
        if (str.length() == 3)
            return sb.replace(1, 2, "*").toString();
        else
            return str.substring(0, 1) + "*" + str.substring(str.length() - 1);
    }

    public static boolean isJsonFormat(String msg) {
        return !isBlank(msg) && ((msg.startsWith("{") && msg.endsWith("}")) || (msg.startsWith("[") && msg.endsWith("]")));
    }

}
