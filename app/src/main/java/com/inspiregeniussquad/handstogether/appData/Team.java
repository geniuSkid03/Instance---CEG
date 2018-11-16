package com.inspiregeniussquad.handstogether.appData;

import java.util.ArrayList;

public class Team {

    private String name, desc, motto;
        private ArrayList<TeamMembers> teamMembers;
    private String logoUri, membersCount;
    private String email, website, fbLink;

    public Team(){

    }

    public Team(String name, String motto, String desc, String logoUri, ArrayList<TeamMembers> teamMembers, String membersCount) {
        this.motto = motto;
        this.logoUri = logoUri;
        this.desc = desc;
        this.name = name;
        this.membersCount = membersCount;
        this.teamMembers = teamMembers;
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

    public String getTeamMembersCount() {
        return membersCount;
    }

    public void setTeamMembersCount(String membersCount) {
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
