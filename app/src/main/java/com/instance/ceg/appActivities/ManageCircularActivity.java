package com.instance.ceg.appActivities;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.instance.ceg.R;
import com.instance.ceg.appAdapters.MyViewPagerAdapter;
import com.instance.ceg.appFragments.AddCircularFragment;
import com.instance.ceg.appFragments.ManageCircularFragment;
import com.instance.ceg.appInterfaces.FragmentInterfaceListener;

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
    protected void onResume() {
        super.onResume();

        if(manageCircularFragment != null && manageCircularFragment.isAdded())
            manageCircularFragment.refreshCirculars();
    }

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
