package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.inspiregeniussquad.handstogether.R;

import butterknife.BindView;
import butterknife.OnClick;

public class AdminActivity extends SuperCompatActivity {

    @BindView(R.id.add_news)
    CardView newsCv;

    @BindView(R.id.add_circular)
    CardView circularCv;

    @BindView(R.id.manage_teams)
    CardView manageTeamsCv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    @OnClick({R.id.add_circular, R.id.add_news, R.id.manage_teams})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.add_circular:
                openActivity(0);
                break;
            case R.id.add_news:
                openActivity(1);
                break;
            case R.id.manage_teams:
                openActivity(2);
                break;
        }
    }

    private void openActivity(int key) {
        switch (key) {
            case 0:
                goTo(this, UpdateNewsActivity.class, false);
                break;
            case 1:
                goTo(this, UpdateCircularActivity.class, false);
                break;
            case 2:
                goTo(this, ManageTeamsActivity.class, false);
                break;
        }
    }
}
