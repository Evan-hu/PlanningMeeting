package com.meeting.entity;

import java.util.List;

/**
 * Created by Evan on 9/2/2016.
 */
public class Session {
    private List<Talk> Talks;
    private boolean FillUp;

    public Session() {}

    public Session(List<Talk> Talks, boolean FillUp) {
        this.Talks = Talks;
        this.FillUp = FillUp;
    }

    public List<Talk> getTalks() {
        return Talks;
    }

    public void setTalks(List<Talk> talks) {
        Talks = talks;
    }

    public boolean isFillUp() {
        return FillUp;
    }

    public void setFillUp(boolean FillUp) {
        FillUp = FillUp;
    }
}
