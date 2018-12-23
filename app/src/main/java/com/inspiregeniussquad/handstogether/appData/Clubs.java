package com.inspiregeniussquad.handstogether.appData;

public class Clubs {

    private String clubsName;
    private String clubsImgUrl;

    public Clubs(String clubsName, String clubsImgUrl) {
        this.clubsImgUrl = clubsImgUrl;
        this.clubsName = clubsName;
    }

    public String getClubsName() {
        return clubsName;
    }

    public void setClubsName(String clubsName) {
        this.clubsName = clubsName;
    }

    public String getClubsImgUrl() {
        return clubsImgUrl;
    }

    public void setClubsImgUrl(String clubsImgUrl) {
        this.clubsImgUrl = clubsImgUrl;
    }
}
