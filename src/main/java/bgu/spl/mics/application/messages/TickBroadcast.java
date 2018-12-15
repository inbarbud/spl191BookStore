package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    int time;

    public TickBroadcast(int currentTime){
        time=currentTime;
    }

    public int getTime(){
        return time;
    }
}
