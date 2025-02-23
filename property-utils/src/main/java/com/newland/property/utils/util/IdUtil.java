package com.newland.property.utils.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.util.Random;

public class IdUtil {
    public static final Random random = new Random();

    public static String generateId(String prefix) {
        return prefix + System.currentTimeMillis() + String.format("%05d", random.nextInt(99999));
    }

    public static void main(String[] args) {
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        stringEncryptor.setAlgorithm("PBEWithMD5AndDES");
        stringEncryptor.setPassword("password");
        System.out.println(stringEncryptor.encrypt("Macau@WBH_newlandgo2021"));
        System.out.println(stringEncryptor.decrypt("iWM+ei+H5S94W9vuQMX0RYSymWiybFU3"));
    }
}
