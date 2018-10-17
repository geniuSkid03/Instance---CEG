package com.inspiregeniussquad.handstogether.appData;

public class Users {

    private String StudentID, name, email, mobile, gender;

    public Users() {

    }

    public Users(String name, String email, String mobile, String gender) {
//        this.StudentID = StudentID;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
    }

    public String getStudentID() {
        return StudentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
