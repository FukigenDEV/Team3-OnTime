package com.example.ontimeapp;

public class Alarm {

    private String name;
    private String date;
    private String time;
    private long milis;

    public Alarm(){
    }
    public Alarm(String name, String date, String time, long milis){
        this.name = name;
        this.date = date;
        this.time = time;
        this.milis = milis;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setMilis(long milis) {
        this.milis = milis;
    }

    public long getMilis() {
        return milis;
    }
}
