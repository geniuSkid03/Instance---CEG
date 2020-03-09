package com.instance.ceg.appData;

public class Admin {

    private String name, mobile, position, designation;

    public Admin(String name, String mobile, String position, String designation) {
        this.name = name;
        this.mobile = mobile;
        this.position = position;
        this.designation = designation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
