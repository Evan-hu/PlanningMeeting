package com.meeting.entity;

import java.util.List;
import java.util.Scanner;

/**
 * Created by Evan on 4/11/2016.
 */
public class Session {
    private List<Talk> Talks;
    private boolean FilleUp;

    public Session() {}

    public Session(List<Talk> Talks, boolean FilleUp) {
        this.Talks = Talks;
        this.FilleUp = FilleUp;
    }

    public List<Talk> getTalks() {
        return Talks;
    }

    public void setTalks(List<Talk> talks) {
        Talks = talks;
    }

    public boolean isFilleUp() {
        return FilleUp;
    }

    public void setFilleUp(boolean filleUp) {
        FilleUp = filleUp;
    }
}
