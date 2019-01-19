package com.shades.client;


import com.shades.utilities.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.regex.Pattern;

public class JavaTest {

    public static void main(String[] args) {

        /*String name = "Veronika G Hodsden";
        int index = StringUtils.lastIndexOfAny(name, " ");
        String first = StringUtils.substring(name, 0, index);
        String last = StringUtils.substring(name, index, name.length());
        System.out.println("First: " + first);
        System.out.println("Last: " + last);*/

        String x = "($4.70)";
        x  = StringUtils.substringBetween(x, "($",")");
        System.out.println(x);
    }
}
