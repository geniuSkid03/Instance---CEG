package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.MyViewPagerAdapter;
import com.inspiregeniussquad.handstogether.appFragments.AddCircularFragment;
import com.inspiregeniussquad.handstogether.appFragments.ManageCircularFragment;
import com.inspiregeniussquad.handstogether.appInterfaces.FragmentInterfaceListener;

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

        manageCircularFragment.setFragmentInterfaceListener(new FragmentInterfaceListener() {
            @Override
            public void refreshFragments() {
                manageCircularFragment.refreshCirculars();
            }
        });

        setupViewPager(manageCircularViewPager);

        manageCircularTab.setupWithViewPager(manageCircularViewPager);
    }

    private void setupViewPager(ViewPager manageCircularViewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(addCircularFragment, getString(R.string.new_circular));
        adapter.addFragment(manageCircularFragment, getString(R.string.manage_circulars));
        manageCircularViewPager.setAdapter(adapter);
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
