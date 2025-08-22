package com.chat.server.common.util;

public final class VOUtil {
    private VOUtil() {
    }

    public static Long positive(Long v,
                                String name) {
        if (v == null || v <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }

        return v;
    }
}
