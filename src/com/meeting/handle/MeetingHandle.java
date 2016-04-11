package com.meeting.handle;

import com.meeting.entity.Talk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Evan on 4/11/2016.
 */
public class MeetingHandle {

    public boolean hasNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

//    public Talk

}
