package com.example.ontimeapp;

public class UserAdapter {

    private String name;
    private String deviceToken;
    private String[] tasks;

    public UserAdapter(){
    }

    public UserAdapter(String name, String deviceToken, String[] tasks){
        this.name = name;
        this.deviceToken = deviceToken;
        this.tasks = tasks;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken(){
        return deviceToken;
    }

    public void setTasks(String[] tasks) {this.tasks = tasks;}

    public String[] getTasks() {return tasks;}
}
