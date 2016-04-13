package com.meeting.Main;

import com.meeting.entity.Talk;
import com.meeting.handle.Conference;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConferenceScheduler {

    public static void main(String[] args) {
        System.out.print("Please Input meeting(End with Double Enter): ");
        String line = "";
        List<Talk> talks = new ArrayList<Talk>();
        Scanner input = new Scanner(System.in);
        try {
            while(input.hasNextLine()) {
                line = input.nextLine();
                if (line.length() != 0) {
                    Talk meeting = new Talk();
                    String [] tokens = line.split(" ");
                    int duration = GetDuration(tokens[tokens.length - 1]);
                    String title = StringArrayToString(tokens);
                    if (hasNumberic(title)) {
                        System.out.println("Error: talk title has number in it. Please input again !");
                        continue;
                    } else if (duration == 0) {
                        System.out.println("Error: Can't identify the meeting time. Please input again !");
                        continue;
                    }
                    talks.add(new Talk(StringArrayToString(tokens), GetDuration(tokens[tokens.length - 1])));
                } else {
                    break;
                }
            }
            Conference conference = new Conference();
            conference.ScheduleTalks(talks);
        } catch (Exception e) {while(input.hasNextLine())
            System.out.println(e);
            System.out.println("Error, please try again!");
        }
    }


    public static String StringArrayToString(String [] str) {
        String result = "";
        for (int i = 0; i < str.length - 1; i++)
            result += str[i] + " ";
        return result;
    }

    public static int GetDuration(String time) {
        if ("lightning".equals(time))
            return 5;
        else if (TimeType(time))
            return Integer.parseInt(time.substring(0,2));
        else
            return 0;
    }

    public static boolean hasNumberic(String str) {
        Pattern pattern = Pattern.compile("\\d+.*");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches())
            return false;
        return true;
    }

    public static boolean TimeType(String str) {
        Pattern pattern = Pattern.compile("\\d+min");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches())
            return false;
        return true;
    }


}
