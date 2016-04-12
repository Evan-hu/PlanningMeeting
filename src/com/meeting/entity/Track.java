package com.meeting.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 4/11/2016.
 */
public class Track {
    private static int SessionStartAt = 9;
    private static int SessionEndsAt = 17;
    private static int LunchHour = 12;
    public static int MinutesPerHour = 60;

    public static int TotalMinPerTrack = (SessionStartAt - SessionEndsAt - 1) * MinutesPerHour;
    public static int TotalMinInMorningSession = MinutesPerHour * (LunchHour - SessionStartAt);
    public static int TotalMinInAfterNoonSession = MinutesPerHour * (SessionEndsAt - LunchHour);

    private String Id;
    private Map<Sessiontype, Session> Sessions;

    public Track(String id) {
        Id = id;
        Sessions = new HashMap<Sessiontype, Session>();
    }

    public boolean TalksExistForSession(Sessiontype sessiontype) {
        return Sessions.containsKey(sessiontype) && Sessions.get(sessiontype).isFillUp();
    }

    public void AddTalksToSession(Sessiontype sessiontype, List<Talk> talksForSession) {
        Sessions.put(sessiontype, new Session(talksForSession,true));
    }

    public List<Talk> TalksForSession(Sessiontype sessiontype) {
        if (Sessions.containsKey(sessiontype)) {
            if (Sessions.get(sessiontype).isFillUp()) {
                return Sessions.get(sessiontype).getTalks();
            }
        }
        return new ArrayList<Talk>();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Map<Sessiontype, Session> getSessions() {
        return Sessions;
    }

    public void setSessions(Map<Sessiontype, Session> sessions) {
        Sessions = sessions;
    }
}
