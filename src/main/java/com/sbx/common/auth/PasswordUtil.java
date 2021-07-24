package com.sbx.common.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private PasswordUtil() {
    }

    /**
     * 加密
     * @param plaintext 明文
     * @return 加密结果
     */
    public static String encrypt(Object plaintext){
        return passwordEncoder.encode(String.valueOf(plaintext));
    }

    /**
     * 比较
     * @param plaintext 明文
     * @param cipherText 数据库密文
     * @return 返回结果
     */
    public static Boolean matches(String plaintext, String cipherText){
        return passwordEncoder.matches(plaintext, cipherText);
    }

}
