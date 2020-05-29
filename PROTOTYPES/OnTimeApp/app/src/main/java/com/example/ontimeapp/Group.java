package com.example.ontimeapp;

public class Group {

    private String groupId;
    private String groupName;

    public Group(){
    }

    public Group(String groupId, String groupName){
        this.groupId = groupId;
        this.groupName = groupName;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getGroupId() {
        return groupId;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
