package com.meeting.handle;

import com.meeting.entity.Talk;

import javax.sound.midi.Track;
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
            double totalDuration = 0;
            Iterator<Talk> itr = talks.iterator();
            while (itr.hasNext()) {
                totalDuration += itr.next().getTime();
            }
        } catch(Exception e){
            System.out.println("test");
        }
    }

}
