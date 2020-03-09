package com.instance.ceg.appActivities;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.instance.ceg.R;
import com.instance.ceg.appAdapters.MyViewPagerAdapter;
import com.instance.ceg.appFragments.AddNewsFragment;
import com.instance.ceg.appFragments.EditNewsFragment;
import com.instance.ceg.appInterfaces.FragmentInterfaceListener;

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
    protected void onResume() {
        super.onResume();

        if(editNewsFragment != null && editNewsFragment.isAdded()) editNewsFragment.refreshPostedNews();
    }

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

        editNewsFragment.setFragmentInterfaceListener(new FragmentInterfaceListener() {
            @Override
            public void refreshFragments() {
                editNewsFragment.refreshPostedNews();
            }
        });

        setupViewPager(manageNewsViewPager);

        manageNewsTab.setupWithViewPager(manageNewsViewPager);
    }

    private void setupViewPager(ViewPager manageNewsViewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(addNewsFragment, getString(R.string.add_news));
        adapter.addFragment(editNewsFragment, getString(R.string.manage_news));
        manageNewsViewPager.setAdapter(adapter);
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
