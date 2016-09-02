package com.meeting.util;

/**
 * Created by Evan on 9/2/2016.
 */
public class Format {
    public static String stringArrayToString(String [] str) {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < str.length - 1; i++)
            bf.append(str[i] + " ");
        return bf.toString();
    }
}
