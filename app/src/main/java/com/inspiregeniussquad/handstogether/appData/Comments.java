package com.inspiregeniussquad.handstogether.appData;

public class Comments {

    private String cName, cDate, cTime, cmnt, cImg;

    public Comments(){

    }

    public Comments(String cName, String cDate, String cTime, String cmnt, String cImg) {
        this.cName = cName;
        this.cDate = cDate;
        this.cTime = cTime;
        this.cmnt = cmnt;
        this.cImg = cImg;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getCmnt() {
        return cmnt;
    }

    public void setCmnt(String cmnt) {
        this.cmnt = cmnt;
    }

    public String getcImg() {
        return cImg;
    }

    public void setcImg(String cImg) {
        this.cImg = cImg;
    }
}
