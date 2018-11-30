package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appActivities.AdminActivity;
import com.inspiregeniussquad.handstogether.appActivities.ProfileUpdatingActivity;

public class SettingsFragment extends SuperFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_filter_search);
        searchMenuItem.setVisible(false);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.fragment_settings, container, false));
    }

    private View initView(View view) {

        LinearLayout updateProfileLayout = view.findViewById(R.id.update_profile);
        LinearLayout adminPanelLayout = view.findViewById(R.id.admin_panel);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.update_profile:
                        openActivity(0);
                        break;
                    case R.id.admin_panel:
                        openActivity(1);
                        break;
                }
            }
        };

        updateProfileLayout.setOnClickListener(clickListener);
        adminPanelLayout.setOnClickListener(clickListener);

        return view;
    }

    private void openActivity(int key) {
        switch (key) {
            case 0:
                goTo(getActivity(), ProfileUpdatingActivity.class, false);
                break;
            case 1:
                goTo(getActivity(), AdminActivity.class, false);
                break;
        }
    }
}
