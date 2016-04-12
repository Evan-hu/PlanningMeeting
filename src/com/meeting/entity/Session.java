package com.meeting.entity;

import java.util.List;

/**
 * Created by Evan on 4/11/2016.
 */
public class Session {
    private List<Talk> Talks;
    private boolean FillUp;

    public Session() {}

    public Session(List<Talk> Talks, boolean FilleUp) {
        this.Talks = Talks;
        this.FillUp = FilleUp;
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

    public void setFillUp(boolean fillUp) {
        FillUp = fillUp;
    }
}
