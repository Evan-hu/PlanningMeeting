package com.meeting.Main;

import com.meeting.entity.Talk;
import com.meeting.handle.Conference;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ConferenceScheduler {

    public static void main(String[] args) {
        System.out.print("Please Input meeting: ");
        String line = "";
        List<Talk> talks = new ArrayList<Talk>();
        Scanner input = new Scanner(System.in);
        try {
            while(input.hasNextLine()) {
                line = input.nextLine();
                if (line.length() != 0) {
                    Talk meeting = new Talk();
                    String [] tokens = line.split(" ");
//                    int duration = GetDuration(tokens[tokens.length - 1]);
//                    String title = StringArrayToString(tokens);
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
            result += str[i];
        return result;
    }

    public static int GetDuration(String time) {
        if ("lightning".equals(time))
            return 5;
        else
            return Integer.parseInt(time.substring(0,2));
    }


}
