package com.ifuture.iread.util;

import java.util.UUID;

/**
 * Created by maofn on 2017/3/14.
 */
public class UUIDUtil {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    }

}
