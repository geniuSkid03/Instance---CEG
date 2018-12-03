package com.inspiregeniussquad.handstogether.appStorage.dbAsyncHelpers;

import android.content.Context;
import android.os.AsyncTask;

import com.inspiregeniussquad.handstogether.appStorage.AppDbs;
import com.inspiregeniussquad.handstogether.appStorage.TeamData;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;

public class TeamDbHelper extends AsyncTask<Void, Integer, Void> {

    private Action action;
    private ArrayList<TeamData> teamDataArrayList;
    private Context context;
    private AppDbs appDbs;


    public TeamDbHelper(Context context, ArrayList<TeamData> teamDataArrayList, Action action) {
        this.context = context;
        this.teamDataArrayList = teamDataArrayList;
        this.action = action;

        appDbs = AppDbs.getTeamDao(context);
    }

    public TeamDbHelper run() {
        executeOnExecutor(THREAD_POOL_EXECUTOR);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (action != null) {
            action.onStart();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (action != null) {
            action.onUpdate(values[0]);
        }

    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            if (appDbs != null) {

                if(teamDataArrayList.size() != 0) {
                    for (int i=0; i<teamDataArrayList.size(); i++) {
                        appDbs.teamDao().insert(teamDataArrayList.get(i));
                    }
                }
//                appDbs.teamDao().insertAll(teamDataArrayList);
            } else {
                AppHelper.print("Team db null");
            }
        } catch (Exception e) {
            AppHelper.print(e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (action != null) {
            action.onEnd();
        }
    }

    public interface Action {
        void onStart();

        void onUpdate(int progress);

        void onEnd();
    }
}
