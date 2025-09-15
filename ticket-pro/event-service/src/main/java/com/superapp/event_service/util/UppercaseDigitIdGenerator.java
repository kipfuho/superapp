package com.superapp.event_service.util;

import java.security.SecureRandom;

public class UppercaseDigitIdGenerator implements IdGenerator {
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final int length;

    public UppercaseDigitIdGenerator(int length) {
        this.length = length;
    }

    @Override
    public String generateId() {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARSET.charAt(RANDOM.nextInt(CHARSET.length())));
        }
        return sb.toString();
    }
}