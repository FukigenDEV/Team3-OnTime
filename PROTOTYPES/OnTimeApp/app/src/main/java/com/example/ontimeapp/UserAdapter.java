package com.example.ontimeapp;

public class UserAdapter {

    private String name;
    private String deviceToken;
    private String phone;

    public UserAdapter(){
    }

    public UserAdapter(String name, String deviceToken, String phone){
        this.name = name;
        this.deviceToken = deviceToken;
        this.phone = phone;

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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone(){
        return phone;
    }

}
