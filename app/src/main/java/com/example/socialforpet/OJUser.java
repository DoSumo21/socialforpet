package com.example.socialforpet;

public class OJUser {

    private   int resourceId;
    private   String name;
    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public OJUser(int resourceId, String name) {
        this.resourceId = resourceId;
        this.name = name;
    }






}
