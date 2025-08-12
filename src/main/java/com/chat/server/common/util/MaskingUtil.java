package com.chat.server.common.util;

public class MaskingUtil {
    private MaskingUtil() {
    }

    /**
     * 마스킹 규칙
     * - 길이 1: 그대로
     * - 길이 2: 첫 글자 + '*'
     * - 길이 3 이상: 첫 글자 + 중간 전부 '*' + 마지막 글자
     */
    public static String mask(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        int length = input.length();
        if (length == 1) {
            return input;
        }

        if (length == 2) {
            return input.charAt(0) + "*";
        }

        return input.charAt(0) + "*".repeat(length - 2) + input.charAt(length - 1);
    }
}
