package io.github.rovingsea.glancecorrection.core.util;

/**
 *
 * 
 *
 * @author Haixin Wu
 * @since 1.0.0
 */
public abstract class StringUtils {

    /**
     * 字符串驼峰转下划线格式
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String camelToUnderline(String param) {
        if (isBlank(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }


    /**
     * 判断字符串中是否全是空白字符
     *
     * @param cs 需要判断的字符串
     * @return 如果字符串序列是 null 或者全是空白，返回 true
     */
    public static boolean isBlank(CharSequence cs) {
        if (cs != null) {
            int length = cs.length();
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 首字母大写
     *
     * @param param 目标字符串
     * @return 首字母大写后的字符串
     */
    public static String firstToUpperCase(String param) {
        if (isBlank(param)) {
            return "";
        }
        return param.substring(0, 1).toUpperCase() + param.substring(1);
    }

}
