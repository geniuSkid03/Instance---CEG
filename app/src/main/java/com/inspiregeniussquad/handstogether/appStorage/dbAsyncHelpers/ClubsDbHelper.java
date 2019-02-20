package com.inspiregeniussquad.handstogether.appStorage.dbAsyncHelpers;

import android.content.Context;
import android.os.AsyncTask;

import com.inspiregeniussquad.handstogether.appStorage.AppDbs;
import com.inspiregeniussquad.handstogether.appStorage.ClubsData;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class ClubsDbHelper  {


    private Context context;
    private ArrayList<ClubsData> clubsDataArrayList;
    private AppDbs appDbs;
    private ClubsRetrivalAction clubsRetrivalAction;




    public class ClubsRetrivalTask extends AsyncTask<Void, Void, ClubsData[]>  {

        public ClubsRetrivalTask(Context context, ClubsRetrivalAction clubsRetrivalAction) {
            ClubsDbHelper.this.context = context;
            ClubsDbHelper.this.clubsRetrivalAction = clubsRetrivalAction;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ClubsData[] doInBackground(Void... voids) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
                AppHelper.print("Error in retrieving club information from db");
            }

            return null;
        }

        @Override
        protected void onPostExecute(ClubsData[] clubsData) {
            super.onPostExecute(clubsData);

            if(ClubsDbHelper.this.clubsRetrivalAction != null) {
                ClubsDbHelper.this.clubsRetrivalAction.onDataReceived(new ArrayList<>(Arrays.asList(clubsData)));
            }
        }
    }

    public interface ClubsRetrivalAction {
        void onDataReceived(ArrayList<ClubsData> clubsDataArrayList);
        void onError();
    }
}
