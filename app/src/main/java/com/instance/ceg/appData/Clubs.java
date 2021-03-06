package com.instance.ceg.appData;

public class Clubs {

    private String clubsName;
    private String clubsImgUrl;
    private String clubsId;

    public Clubs() {

    }

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

    public String getClubsId() {
        return clubsId;
    }

    public void setClubsId(String clubsId) {
        this.clubsId = clubsId;
    }
}
