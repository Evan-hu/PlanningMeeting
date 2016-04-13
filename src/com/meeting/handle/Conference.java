package com.meeting.handle;

import com.meeting.entity.Sessiontype;
import com.meeting.entity.Talk;
import com.meeting.entity.Track;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Evan on 4/11/2016.
 */
public class Conference {
    private List<Track> tracks = new ArrayList<Track>();

    public void ScheduleTalks(List<Talk> talks) {
        if (talks.size() == 0) {
            System.out.println("No talks to scheduled");
            return ;
        }
        try {
            double totalDuration = getSumDuration(talks);
            int numOfTracks = (totalDuration < Track.TotalMinPerTrack) ? 1 : (int) Math.ceil(totalDuration / Track.TotalMinPerTrack);
            tracks = new ArrayList<Track>();
            int maxSet = talks.size() > 6 ? 6 : talks.size() - 1;
            for (int i = 0; i < numOfTracks; i++) {
                tracks.add(new Track("Track " + i + 1));
                AllocateSession(talks, i, Track.TotalMinInMorningSession, Sessiontype.MorningSession, maxSet);
                AllocateSession(talks, i, Track.TotalMinInAfterNoonSession, Sessiontype.AfternoonSession, maxSet);
            }
            if (talks.size() > 0) {
                int remainingTalksDuration = getSumDuration(talks);
                for (;maxSet > 0; --maxSet) {
                    for (int index = 0; index < numOfTracks && talks.size() > 0; ++index) {
                        AllocateSession(talks, index, Track.TotalMinInMorningSession, Sessiontype.MorningSession, maxSet);
                        AllocateSession(talks, index, Track.TotalMinInAfterNoonSession, Sessiontype.AfternoonSession, maxSet);
                    }
                }
            }

            for (int i = 1; i < numOfTracks+1; i++) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE,0);
                SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
                System.out.println("Track "+ i);
                for (Talk talk : tracks.get(i-1).TalksForSession(Sessiontype.MorningSession)) {
                    System.out.println(dateFormat.format(calendar.getTime())+ " " + talk.getTitle() + talk.DurationFormat());
                    calendar.add(Calendar.MINUTE, talk.getDuration());
                }
                System.out.println(dateFormat.format(Track.LunchPM)+ " Lunch");
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                for (Talk talk : tracks.get(i-1).TalksForSession(Sessiontype.AfternoonSession)) {
                    System.out.println(dateFormat.format(calendar.getTime())+ " " + talk.getTitle() + talk.DurationFormat());
                }

                if (Track.FourPM.after(calendar.getTime())) {
                    System.out.println(dateFormat.format(Track.FourPM) + " Networking Event");
                } else if (Track.FivePM.after(calendar.getTime()) && calendar.getTime().after(Track.FourPM)) {
                    System.out.println(dateFormat.format(calendar.getTime()) + " Networking Event");
                } else {
                    System.out.println(dateFormat.format(Track.FivePM) + " Networking Event");
                }
                System.out.println("\n\n");
            }


        } catch(Exception e){
            System.out.println(e);
        }
    }

    private int getSumDuration(List<Talk> talks) {
        int duration = 0;
        Iterator<Talk> itr = talks.iterator();
        while (itr.hasNext()) {
            duration += itr.next().getDuration();
        }
        return duration;
    }

    private void AllocateSession(List<Talk> talks, int trackIndex, int totalNumOfMintues, Sessiontype sessionType, int maxset ) {
        if (tracks.get(trackIndex).TalksExistForSession(sessionType)) {
            return ;
        }
        List<Talk> talksForSession = new ArrayList<Talk>();
        if (!GetAllTime(talks,totalNumOfMintues)) {
            talksForSession = LookForSession(talks, trackIndex, totalNumOfMintues, maxset);
            if (!talksForSession.isEmpty()) {
                tracks.get(trackIndex).AddTalksToSession(sessionType, talksForSession);
                for (int i = 0; i < talksForSession.size(); i++) {
                    talks.remove(talksForSession.get(i));
                }
            }
        } else {
            for (int i=0; i<talks.size(); i++) {
                talksForSession.add((Talk) talks.get(i).clone());
            }
            tracks.get(trackIndex).AddTalksToSession(sessionType, talksForSession);
            talks.clear();
        }
    }

    private List<Talk> LookForSession(List<Talk> talks, int trackIndex, int totalMintues, int maxSet) {
        List<Talk> combinations = new ArrayList<Talk>(talks.size());
        List<Talk> talksInSession = new ArrayList<Talk>(maxSet);
        boolean found = false;

        for (; maxSet > 0; maxSet--) {
            Iterator<Talk> itr = GetCombinations(maxSet, trackIndex, combinations, talks).iterator();
            while (itr.hasNext()) {
                talksInSession.clear();
                int availableMin = totalMintues;

                while (itr.hasNext()) {
                    Talk talk = itr.next();
                    availableMin -= talk.getDuration();
                    talksInSession.add(talk);
                    if (availableMin == 0) {
                        found = true;
                        break;
                    }
                    if (availableMin < 0) {
                        break;
                    }
                }
                if (found) {
                    break;
                } else {
                    availableMin = totalMintues;
                }
            }
            if (found)
                break;
        }
        return talksInSession;
    }

    private List<Talk> GetCombinations(int step, int arrayIndex, List<Talk> combination, List<Talk> talks) {
        if (0 == step) {
            return combination;
        }
        for (int i = arrayIndex; i < talks.size(); i++) {
            combination.add(talks.get(i));
            GetCombinations(step-1, i+1, combination, talks);
        }
        return combination;
    }


    public boolean GetAllTime(List<Talk> talks, int tag) {
        int tempDuration = 0;
        for (Talk talk: talks) {
            tempDuration += talk.getDuration();
        }
        return tempDuration < tag ? true: false;
    }

}
