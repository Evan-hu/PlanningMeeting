package com.meeting.entity;

import javax.xml.datatype.Duration;

/**
 * Created by Evan on 4/11/2016.
 */
public class Talk {
    private String title;
    private int duration;
    private boolean scheduled;

    public Talk() {
    }

    public String getTitle() {
        return title;
    }

    private String DurationFormat() {
        return duration == 5 ? "Lightning" : duration + "min";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return duration;
    }

    public void setTime(int duration) {
        this.duration = duration;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }
}