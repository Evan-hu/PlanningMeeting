package com.meeting.handle;

import com.meeting.entity.Sessiontype;
import com.meeting.entity.Talk;
import com.meeting.entity.Track;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Evan on 9/2/2016.
 */
public class Conference {
    private List<Track> tracks = new ArrayList<Track>();

    public List<String> scheduleTalks(List<Talk> talks) {
        List<String> results = new ArrayList<String>();
        if (talks.size() == 0) {
            results.add("No talks to scheduled");
        }
        try {
            double totalDuration = getSumDuration(talks);
            int numOfTracks = (totalDuration < Track.TotalMinPerTrack) ? 1 : (int) Math.ceil(totalDuration / Track.TotalMinPerTrack);
            tracks = new ArrayList<Track>();
            int maxSet = talks.size() > 6 ? 6 : talks.size() - 1;
            for (int i = 0; i < numOfTracks; i++) {
                tracks.add(new Track("Track " + i + 1));
                allocateSession(talks, i, Track.TotalMinInMorningSession, Sessiontype.MorningSession, maxSet);
                allocateSession(talks, i, Track.TotalMinInAfterNoonSession, Sessiontype.AfternoonSession, maxSet);
            }
            if (talks.size() > 0) {
                int remainingTalksDuration = getSumDuration(talks);
                for (;maxSet > 0; --maxSet) {
                    for (int index = 0; index < numOfTracks && talks.size() > 0; ++index) {
                        allocateSession(talks, index, Track.TotalMinInMorningSession, Sessiontype.MorningSession, maxSet);
                        allocateSession(talks, index, Track.TotalMinInAfterNoonSession, Sessiontype.AfternoonSession, maxSet);
                    }
                }
            }

            for (int i = 1; i < numOfTracks+1; i++) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE,0);
                SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
                results.add("Track "+ i);
                for (Talk talk : tracks.get(i-1).talksForSession(Sessiontype.MorningSession)) {
                    results.add(dateFormat.format(calendar.getTime())+ " " + talk.getTitle() + talk.DurationFormat());
                    calendar.add(Calendar.MINUTE, talk.getDuration());
                }
                results.add(dateFormat.format(Track.LunchPM)+ " Lunch");
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                for (Talk talk : tracks.get(i-1).talksForSession(Sessiontype.AfternoonSession)) {
                    results.add(dateFormat.format(calendar.getTime())+ " " + talk.getTitle() + talk.DurationFormat());
                    calendar.add(Calendar.MINUTE, talk.getDuration());
                }

                if (Track.FourPM.after(calendar.getTime())) {
                    results.add(dateFormat.format(Track.FourPM) + " Networking Event");
                } else {
                    results.add(dateFormat.format(Track.FivePM) + " Networking Event");
                }
                results.add(" ");
            }


        } catch(Exception e){
            System.out.println(e);
        }

        return results;
    }

    private int getSumDuration(List<Talk> talks) {
        int duration = 0;
        Iterator<Talk> itr = talks.iterator();
        while (itr.hasNext()) {
            duration += itr.next().getDuration();
        }
        return duration;
    }

    private void allocateSession(List<Talk> talks, int trackIndex, int totalNumOfMintues, Sessiontype sessionType, int maxset ) {
        if (tracks.get(trackIndex).talksExistForSession(sessionType)) {
            return ;
        }
        List<Talk> talksForSession = new ArrayList<Talk>();
        if (!GetAllTime(talks,totalNumOfMintues)) {
            talksForSession = lookForSession(talks, trackIndex, totalNumOfMintues, maxset);
            if (!talksForSession.isEmpty()) {
                tracks.get(trackIndex).addTalksToSession(sessionType, talksForSession);
                for (int i = 0; i < talksForSession.size(); i++) {
                    talks.remove(talksForSession.get(i));
                }
            }
        } else {
            for (int i=0; i<talks.size(); i++) {
                talksForSession.add((Talk) talks.get(i).clone());
            }
            tracks.get(trackIndex).addTalksToSession(sessionType, talksForSession);
            talks.clear();
        }
    }

    private List<Talk> lookForSession(List<Talk> talks, int trackIndex, int totalMintues, int maxSet) {
        List<Talk> combinations = new ArrayList<Talk>(talks.size());
        List<Talk> talksInSession = new ArrayList<Talk>(maxSet);
        boolean found = false;

        for (; maxSet > 0; maxSet--) {
            Iterator<Talk> itr = getCombinations(maxSet, trackIndex, combinations, talks).iterator();
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

    private List<Talk> getCombinations(int step, int arrayIndex, List<Talk> combination, List<Talk> talks) {
        if (0 == step) {
            return combination;
        }
        for (int i = arrayIndex; i < talks.size(); i++) {
            combination.add(talks.get(i));
            getCombinations(step-1, i+1, combination, talks);
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
