package com.meeting.Main;

import com.meeting.entity.Talk;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        System.out.print("Please Input meeting: ");
        String line = "";
        List<Talk> mList = new LinkedList<Talk>();
        Scanner input = new Scanner(System.in);
        input.useDelimiter("\n");
        try {
            while(input.hasNextLine()) {
                line = input.nextLine();
                if (line.length() != 0) {
                    Talk meeting = new Talk();
                    if (line.endsWith("lightning")) {

                    }
                } else {
                    System.out.println("The content is empty!");
                    break;
                }
            }
        } catch (Exception e) {while(input.hasNextLine())
            System.out.println(e);
            System.out.println("Error, please try again!");
        }
        System.out.println("Input over!");
    }


}
