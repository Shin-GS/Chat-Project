package com.chat.server.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Base64Util {
    private Base64Util() {
    }

    public static String encode(String input) {
        if (input == null) {
            return null;
        }

        return encode(bytes(input));
    }

    public static String encode(byte[] input) {
        if (input == null) {
            return null;
        }

        return Base64.getEncoder().encodeToString(input);
    }

    public static String decodeToString(String base64) {
        byte[] bytes = decode(base64);
        return bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] decode(String base64) {
        if (base64 == null) {
            return null;
        }

        return Base64.getDecoder().decode(trim(base64));
    }

    public static String encodeUrlSafe(String input) {
        if (input == null) {
            return null;
        }

        return encodeUrlSafe(bytes(input));
    }

    public static String encodeUrlSafe(byte[] input) {
        if (input == null) {
            return null;
        }

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(input);
    }

    public static String decodeUrlSafeToString(String base64Url) {
        byte[] bytes = decodeUrlSafe(base64Url);
        return bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] decodeUrlSafe(String base64Url) {
        if (base64Url == null) {
            return null;
        }

        String normalized = normalizeUrlSafeBase64(trim(base64Url));
        return Base64.getUrlDecoder().decode(normalized);
    }

    private static String trim(String s) {
        return s.trim();
    }

    private static byte[] bytes(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private static String normalizeUrlSafeBase64(String s) {
        int rem = s.length() % 4;

        if (rem == 2) {
            return s + "==";
        }

        if (rem == 3) {
            return s + "=";
        }

        if (rem == 1) {
            throw new IllegalArgumentException("Invalid Base64 input length.");
        }

        return s;
    }
}
