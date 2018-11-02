package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.MyViewPagerAdapter;
import com.inspiregeniussquad.handstogether.appFragments.AddNewsFragment;
import com.inspiregeniussquad.handstogether.appFragments.EditNewsFragment;

import butterknife.BindView;

public class ManageNewsActivity extends SuperCompatActivity {

    @BindView(R.id.manage_news_tab)
    TabLayout manageNewsTab;

    @BindView(R.id.manage_news_viewpager)
    ViewPager manageNewsViewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AddNewsFragment addNewsFragment;
    private EditNewsFragment editNewsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_news);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //creating fragments
        if (addNewsFragment == null) {
            addNewsFragment = new AddNewsFragment();
        }

        if (editNewsFragment == null) {
            editNewsFragment = new EditNewsFragment();
        }

        setupViewPager(manageNewsViewPager);

        manageNewsTab.setupWithViewPager(manageNewsViewPager);
    }

    private void setupViewPager(ViewPager manageNewsViewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(addNewsFragment, getString(R.string.add_news));
        adapter.addFragment(editNewsFragment, getString(R.string.manage_news));
        manageNewsViewPager.setAdapter(adapter);
    }
}
