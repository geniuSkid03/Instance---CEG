package com.inspiregeniussquad.handstogether.appAdapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> fragmentNames;
    private ArrayList<Fragment> fragmentList;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentNames = new ArrayList<>();
        fragmentList = new ArrayList<>();
    }

    //for adding fragments
    public void addFragment(String fragmentName, Fragment fragment) {
        fragmentNames.add(fragmentName);
        fragmentList.add(fragment);
    }

    //for retriving fragments
    public int getFragment(String fragmentName) {
        for (int i = 0; i < fragmentNames.size(); i++) {
            if (fragmentNames.get(i).equals(fragmentName)) {
                return i;
            }
        }
        return 0;
    }

    //to get pageTitle
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentNames.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
