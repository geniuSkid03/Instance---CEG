package com.instance.ceg.appData;

import java.util.ArrayList;

public class Team {

   private String tName, tMotto, tLogo, tMemCount, tDesc, tId;
   private ArrayList<TeamMembers> teamMembers;
   private String tClubId, tClubName;
   private String tFounded, tMemId;

   public Team() {

   }

    public Team(String tName, String dummy) {
       this.tName = tName;
    }

    public Team(String tName, String tMotto, String tLogo, String tMemCount,
                String tDesc, String tId, String tClubId, String tClubName) {
        this.tName = tName;
        this.tMotto = tMotto;
        this.tLogo = tLogo;
        this.tMemCount = tMemCount;
        this.tDesc = tDesc;
        this.tId = tId;
        this.tClubId = tClubId;
        this.tClubName = tClubName;
    }

    public Team(String tName, String tMotto, String tLogo, String tMemCount,
                String tDesc, String tId, String tClubId, String tClubName,
                String tFounded, String tMemId) {
        this.tName = tName;
        this.tMotto = tMotto;
        this.tLogo = tLogo;
        this.tMemCount = tMemCount;
        this.tDesc = tDesc;
        this.tId = tId;
        this.tClubId = tClubId;
        this.tClubName = tClubName;
        this.tFounded = tFounded;
        this.tMemId = tMemId;
    }


    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String gettMotto() {
        return tMotto;
    }

    public void settMotto(String tMotto) {
        this.tMotto = tMotto;
    }

    public String gettLogo() {
        return tLogo;
    }

    public void settLogo(String tLogo) {
        this.tLogo = tLogo;
    }

    public String gettMemCount() {
        return tMemCount;
    }

    public void settMemCount(String tMemCount) {
        this.tMemCount = tMemCount;
    }

    public String gettDesc() {
        return tDesc;
    }

    public void settDesc(String tDesc) {
        this.tDesc = tDesc;
    }

    public ArrayList<TeamMembers> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(ArrayList<TeamMembers> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public String gettClubId() {
        return tClubId;
    }

    public void settClubId(String tClubId) {
        this.tClubId = tClubId;
    }

    public String gettClubName() {
        return tClubName;
    }

    public void settClubName(String tClubName) {
        this.tClubName = tClubName;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String gettFounded() {
        return tFounded;
    }

    public void settFounded(String tFounded) {
        this.tFounded = tFounded;
    }

    public String gettMemId() {
        return tMemId;
    }

    public void settMemId(String tMemId) {
        this.tMemId = tMemId;
    }
}
