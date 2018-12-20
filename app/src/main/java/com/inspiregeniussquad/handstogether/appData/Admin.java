package com.inspiregeniussquad.handstogether.appData;

public class Admin {

    private String name, mobile, position;

    public Admin(String name, String mobile, String position) {
        this.name = name;
        this.mobile = mobile;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
