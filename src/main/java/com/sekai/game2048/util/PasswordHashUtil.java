package com.sekai.game2048.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

public final class PasswordHashUtil {

    private static final String PREFIX = "sha256$";
    private static final int SALT_LENGTH = 16;

    private PasswordHashUtil() {
    }

    public static String hash(String rawPassword) {
        String salt = RandomStringUtils.randomAlphanumeric(SALT_LENGTH);
        return PREFIX + salt + "$" + DigestUtils.sha256Hex(salt + rawPassword);
    }

    public static boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }
        if (!isHashed(storedPassword)) {
            return rawPassword.equals(storedPassword);
        }
        String[] parts = storedPassword.split("\\$");
        return parts.length == 3 && DigestUtils.sha256Hex(parts[1] + rawPassword).equals(parts[2]);
    }

    public static boolean isHashed(String password) {
        return password != null && password.startsWith(PREFIX);
    }
}
