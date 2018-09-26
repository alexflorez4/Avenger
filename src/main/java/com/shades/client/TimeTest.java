package com.shades.client;

import org.apache.commons.lang3.time.FastDateFormat;

import java.sql.Timestamp;

public class TimeTest {

    public static final FastDateFormat AVE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    public static void main(String[] args) {

        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        Long time = System.currentTimeMillis();

        System.out.println("TimeStamp: " + timeStamp);
        System.out.println("Time: " + time);

        String afterFormat = AVE_FORMAT.format(time);
        System.out.println("After Format: " + afterFormat);
    }
}
