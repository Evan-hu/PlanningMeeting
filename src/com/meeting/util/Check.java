package com.meeting.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Evan on 9/2/2016.
 */
public class Check {

    public static int getDuration(String time) {
        if ("lightning".equals(time))
            return 5;
        else if (timeType(time))
            return Integer.parseInt(time.substring(0,2));
        else
            return 0;
    }

    public static boolean hasNumberic(String str) {
        Pattern pattern = Pattern.compile(".*\\d+.*");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches())
            return false;
        return true;
    }

    public static boolean timeType(String str) {
        Pattern pattern = Pattern.compile("\\d+min");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches())
            return false;
        return true;
    }
}
