package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.MyViewPagerAdapter;
import com.inspiregeniussquad.handstogether.appFragments.AddCircularFragment;
import com.inspiregeniussquad.handstogether.appFragments.ManageCircularFragment;

import butterknife.BindView;

public class ManageCircularActivity extends SuperCompatActivity {

    @BindView(R.id.manage_circular_tab)
    TabLayout manageCircularTab;

    @BindView(R.id.manage_circular_viewpager)
    ViewPager manageCircularViewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AddCircularFragment addCircularFragment;
    private ManageCircularFragment manageCircularFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_circular);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //creating fragments
        if (addCircularFragment == null) {
            addCircularFragment = new AddCircularFragment();
        }

        if (manageCircularFragment == null) {
            manageCircularFragment = new ManageCircularFragment();
        }

        setupViewPager(manageCircularViewPager);

        manageCircularTab.setupWithViewPager(manageCircularViewPager);
    }

    private void setupViewPager(ViewPager manageCircularViewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(addCircularFragment, getString(R.string.new_circular));
        adapter.addFragment(manageCircularFragment, getString(R.string.manage_circulars));
        manageCircularViewPager.setAdapter(adapter);
    }
}
