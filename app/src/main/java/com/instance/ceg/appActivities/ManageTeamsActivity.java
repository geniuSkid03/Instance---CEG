package com.instance.ceg.appActivities;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.instance.ceg.R;
import com.instance.ceg.appAdapters.MyViewPagerAdapter;
import com.instance.ceg.appFragments.AddTeamFragment;
import com.instance.ceg.appFragments.UpdateTeamFragment;

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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    if(addTeamFragment != null && addTeamFragment.isAdded()) addTeamFragment.refresh();
                } else {
                    if(updateTeamFragment != null && updateTeamFragment.isAdded()) updateTeamFragment.refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(addTeamFragment, getString(R.string.add_team));
        adapter.addFragment(updateTeamFragment, getString(R.string.update_team));
        viewPager.setAdapter(adapter);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
