package com.inspiregeniussquad.handstogether.appData;

public class TeamMembers {

    private String teamMemberName, teamMemberPosition;

    public TeamMembers() {

    }

    public TeamMembers(String teamMemberName, String teamMemberPosition) {
        this.teamMemberName = teamMemberName;
        this.teamMemberPosition = teamMemberPosition;
    }

    public String getTeamMemberName() {
        return teamMemberName;
    }

    public void setTeamMemberName(String teamMemberName) {
        this.teamMemberName = teamMemberName;
    }

    public String getTeamMemberPosition() {
        return teamMemberPosition;
    }

    public void setTeamMemberPosition(String teamMemberPosition) {
        this.teamMemberPosition = teamMemberPosition;
    }
}
