package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.inspiregeniussquad.handstogether.R;

import butterknife.BindView;
import butterknife.OnClick;

public class AdminActivity extends SuperCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.add_news)
    CardView newsCv;

    @BindView(R.id.add_circular)
    CardView circularCv;

    @BindView(R.id.manage_teams)
    CardView manageTeamsCv;

    @BindView(R.id.admin_manage_card)
    CardView manageAdminsCv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.admin_panel));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.add_circular, R.id.add_news, R.id.manage_teams, R.id.admin_manage_card})
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
            case R.id.admin_manage_card:
                openActivity(3);
                break;

        }
    }

    private void openActivity(int key) {
        switch (key) {
            case 0:
                goTo(this, ManageCircularActivity.class, false);
                break;
            case 1:
                goTo(this, ManageNewsActivity.class, false);
                break;
            case 2:
                goTo(this, ManageTeamsActivity.class, false);
                break;
            case 3:
                goTo(this, ManageAdminsActivity.class, false);
                break;
        }
    }
}
