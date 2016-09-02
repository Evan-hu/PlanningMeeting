package com.meeting.entity;


/**
 * Created by Evan on 9/2/2016.
 */
public class Talk implements Cloneable  {
    private String Title;
    private int Duration;
    private boolean Scheduled;

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

    public Talk(String Title, int Duration) {
        this.Title = Title;
        this.Duration = Duration;
    }

    public String getTitle() {
        return Title;
    }

    public String DurationFormat() {
        return getDuration() == 5 ? "Lightning" : getDuration() + "min";
    }

    public void setTitle(String title) {
        this.Title = Title;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int Duration) {
        this.Duration = Duration;
    }

    public boolean isScheduled() {
        return Scheduled;
    }

    public void setScheduled(boolean Scheduled) {
        this.Scheduled = Scheduled;
    }
}
