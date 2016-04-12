package com.meeting.handle;

import com.meeting.entity.Sessiontype;
import com.meeting.entity.Talk;
import com.meeting.entity.Track;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Evan on 4/11/2016.
 */
public class Conference {
    private List<Track> tracks;

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

        } catch(Exception e){
            System.out.println("test");
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

        for (int i = 0; i < talks.size(); i++) {
            talks.remove(talks.get(i));
        }

    }

    private List<Talk> LookForSession(List<Talk> talks, int trackIndex, int totalMintues, int maxSet) {
        List<Talk> combinations = new ArrayList<Talk>(talks.size());
        List<Talk> talksInSession = new ArrayList<Talk>(maxSet);
        Iterator<Talk> itr = GetCombinations(maxSet, 0, combinations, talks).iterator();
        while (itr.hasNext()) {
            talksInSession.clear();
            boolean found = false;
            int availableMin = totalMintues;

        }

        return talksInSession;
    }

    private List<Talk> GetCombinations(int step, int arrayIndex, List<Talk> combination, List<Talk> talks) {
        if (0 == step)
            return combination;

        for (int i = arrayIndex; i < talks.size(); i++) {
            combination.add(talks.get(i));
            Iterator<Talk> itr = GetCombinations(step - 1, i + 1, combination, talks).iterator();
            while (itr.hasNext()) {
                return (List<Talk>) itr.next();
            }
            combination.remove(combination.size() - 1);
        }

        return null;
    }


}
