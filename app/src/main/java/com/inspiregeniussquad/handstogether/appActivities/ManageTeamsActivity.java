package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.MyViewPagerAdapter;
import com.inspiregeniussquad.handstogether.appFragments.AddTeamFragment;
import com.inspiregeniussquad.handstogether.appFragments.UpdateTeamFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Objects;

import butterknife.BindView;

public class ManageTeamsActivity extends SuperCompatActivity {

    @BindView(R.id.manage_teams_tab)
    TabLayout tabLayout;

    @BindView(R.id.manage_teams_viewpager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AddTeamFragment addTeamFragment;
    private UpdateTeamFragment updateTeamFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_teams);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //creating fragments
        if (addTeamFragment == null) {
            addTeamFragment = new AddTeamFragment();
        }

        if (updateTeamFragment == null) {
            updateTeamFragment = new UpdateTeamFragment();
        }

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(addTeamFragment, getString(R.string.add_team));
        adapter.addFragment(updateTeamFragment, getString(R.string.update_team));
        viewPager.setAdapter(adapter);
    }
}
