package com.meeting.entity;


/**
 * Created by Evan on 4/11/2016.
 */
public class Talk implements Cloneable  {
    private String title;
    private int duration;
    private boolean scheduled;

    public Talk() {
    }

    public Object clone() {
        Talk talk = null;
        try {
            talk = (Talk) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return talk;
    }

    public Talk(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String DurationFormat() {
        return getDuration() == 5 ? "Lightning" : getDuration() + "min";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }
}
