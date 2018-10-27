package com.inspiregeniussquad.handstogether.appData;

import java.util.ArrayList;

public class Team {

    private String name, desc, motto;
    private int membersCount;
    private ArrayList<TeamMembers> teamMembers;
    private String logoUri;

    public Team() {

    }

    public Team(String name, String motto, String logoUri) {
        this.motto = motto;
        this.logoUri = logoUri;
        this.name = name;
    }

    public String getTeamName() {
        return name;
    }


    public String getTeamDesc() {
        return desc;
    }


    public String getTeamMotto() {
        return motto;
    }


    public String getTeamLogoUri() {
        return logoUri;
    }

    public void setTeamName(String name) {
        this.name = name;
    }

    public void setTeamDesc(String desc) {
        this.desc = desc;
    }

    public void setTeamMotto(String motto) {
        this.motto = motto;
    }

    public int getTeamMembersCount() {
        return membersCount;
    }

    public void setTeamMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public ArrayList<TeamMembers> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(ArrayList<TeamMembers> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public void setTeamLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }
}
