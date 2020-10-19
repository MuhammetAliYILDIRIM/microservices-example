package com.may.accountservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashingUtil {

    private static final int STRENGTH = 12;

    private HashingUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String encode(String plainText) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(STRENGTH);
        return encoder.encode(plainText);
    }

    public static boolean matches(String plainText, String hashText) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(STRENGTH);
        return encoder.matches(plainText, hashText);
    }
}
