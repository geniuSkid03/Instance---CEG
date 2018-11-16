package com.inspiregeniussquad.handstogether.appUtils;

import android.content.Context;

import com.inspiregeniussquad.handstogether.appData.Keys;

public class TeamDataHelper {

    private Context context;
    private String tName;

    public TeamDataHelper(Context context) {
        this.context = context;
    }

    public  String getTeamName(int id) {
        switch (id) {
            case 0:
                tName = Keys.TEAM_SPARTANZ;
                break;
            case 1:
                tName = Keys.TEAM_SPARTANZ;
                break;
            case 2:
                tName = Keys.TEAM_SPARTANZ;
                break;
            case 3:
                tName = Keys.TEAM_SPARTANZ;
                break;
            case 4:
                tName = Keys.TEAM_SPARTANZ;
                break;
            case 5:
                tName = Keys.TEAM_SPARTANZ;
                break;
            case 6:
                tName = Keys.TEAM_SPARTANZ;
                break;
            case 7:
                tName = Keys.TEAM_SPARTANZ;
                break;
            case 8:
                tName = Keys.TEAM_SPARTANZ;
                break;
            case 9:
                tName = Keys.TEAM_SPARTANZ;
                break;
        }
        return tName;
    }

}
