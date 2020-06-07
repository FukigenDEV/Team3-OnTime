package com.example.ontimeapp;

public class Taken {
    private String taak;
    private String isFinished;

    public Taken() {
    }

    public Taken(String taak, String isFinished) {
        this.taak = taak;
        this.isFinished = isFinished;
    }

    public String getTaak() {
        return taak;
    }

    public void setTaak(String taak) {
        this.taak = taak;
    }

    public String getisFinished() {
        return isFinished;
    }

    public void setFinished(String finished) {
        isFinished = finished;
    }

}
