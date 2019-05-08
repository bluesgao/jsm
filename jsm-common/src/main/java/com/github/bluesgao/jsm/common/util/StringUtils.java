package com.github.bluesgao.jsm.common.util;

public class StringUtils {
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isBlank(final CharSequence cs) {
        final int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        return (cs1 == null) ? (cs2 == null) : cs1.equals(cs2);
    }

    public static String trim(final String str) {
        return (str == null) ? null : str.trim();
    }
}
