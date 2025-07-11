package com.chat.server.security;

public class JwtUtils {
    private JwtUtils() {
    }

    public static String maskSubject(String subject) {
        if (subject == null || subject.length() < 4) {
            return "****";
        }

        return subject.substring(0, 2) + "***" + subject.charAt(subject.length() - 1);
    }
}
