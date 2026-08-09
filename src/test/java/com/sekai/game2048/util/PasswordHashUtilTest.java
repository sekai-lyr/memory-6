package com.sekai.game2048.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHashUtilTest {

    @Test
    void hashAndMatchPassword() {
        String hash = PasswordHashUtil.hash("sekai2048");

        assertTrue(PasswordHashUtil.isHashed(hash));
        assertTrue(PasswordHashUtil.matches("sekai2048", hash));
        assertFalse(PasswordHashUtil.matches("wrong", hash));
    }
}
