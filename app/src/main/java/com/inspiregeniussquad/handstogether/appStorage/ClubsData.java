package com.inspiregeniussquad.handstogether.appStorage;

import android.arch.persistence.room.Entity;

@Entity
public class ClubsData {

    private String clubsId;
    private String clubsName;
    private String clubsImgUrl;

    public ClubsData() {

    }

    public String getClubsId() {
        return clubsId;
    }

    public void setClubsId(String clubsId) {
        this.clubsId = clubsId;
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
