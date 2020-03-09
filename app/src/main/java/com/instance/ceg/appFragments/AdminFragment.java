package com.instance.ceg.appFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.instance.ceg.R;
import com.instance.ceg.appActivities.ManageAdminsActivity;
import com.instance.ceg.appActivities.ManageCircularActivity;
import com.instance.ceg.appActivities.ManageClubsActivity;
import com.instance.ceg.appActivities.ManageNewsActivity;
import com.instance.ceg.appActivities.ManageTeamsActivity;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appInterfaces.FragmentInterfaceListener;
import com.instance.ceg.appUtils.AppHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class AdminFragment extends SuperFragment {

    private CardView newsCv, circularCv, manageTeamsCv, manageAdminsCv, manageClubsCv;

    private FragmentInterfaceListener fragmentRefreshListener;


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
        return initView(inflater.inflate(R.layout.fragment_admin, container, false));
    }

    private View initView(View view) {

        newsCv = view.findViewById(R.id.add_news);
        circularCv = view.findViewById(R.id.add_circular);
        manageTeamsCv = view.findViewById(R.id.manage_teams);
        manageAdminsCv = view.findViewById(R.id.admin_manage_card);
        manageClubsCv = view.findViewById(R.id.manage_clubs);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View.OnClickListener clickListener = this::doActionForClick;

        newsCv.setOnClickListener(clickListener);
        circularCv.setOnClickListener(clickListener);
        manageAdminsCv.setOnClickListener(clickListener);
        manageTeamsCv.setOnClickListener(clickListener);
        manageClubsCv.setOnClickListener(clickListener);

        adminValue = dataStorage.getString(Keys.ADMIN_VALUE);
        updateUi(adminValue);

        AppHelper.printLog("Admin value: "+adminValue);
    }

    private void updateUi(String adminValue) {
        switch (adminValue) {
            case "0": //geniuS Admin
            case "1": //super admin
                newsCv.setVisibility(View.VISIBLE);
                circularCv.setVisibility(View.VISIBLE);
                manageAdminsCv.setVisibility(View.VISIBLE);
                manageTeamsCv.setVisibility(View.VISIBLE);
                manageClubsCv.setVisibility(View.VISIBLE);
                break;
            case "2": //news admin
                newsCv.setVisibility(View.VISIBLE);
                circularCv.setVisibility(View.GONE);
                manageAdminsCv.setVisibility(View.VISIBLE);
                manageTeamsCv.setVisibility(View.VISIBLE);
                manageClubsCv.setVisibility(View.VISIBLE);
                break;
            case "3": //news editor
                newsCv.setVisibility(View.VISIBLE);
                circularCv.setVisibility(View.GONE);
                manageAdminsCv.setVisibility(View.GONE);
                manageTeamsCv.setVisibility(View.GONE);
                manageClubsCv.setVisibility(View.GONE);
                break;
            case "4": //circular admin
                newsCv.setVisibility(View.GONE);
                circularCv.setVisibility(View.VISIBLE);
                manageAdminsCv.setVisibility(View.VISIBLE);
                manageTeamsCv.setVisibility(View.GONE);
                manageClubsCv.setVisibility(View.GONE);
                break;
            case "5": //circular editor
                newsCv.setVisibility(View.GONE);
                circularCv.setVisibility(View.VISIBLE);
                manageAdminsCv.setVisibility(View.GONE);
                manageTeamsCv.setVisibility(View.GONE);
                manageClubsCv.setVisibility(View.GONE);
                break;
        }
    }

    private void doActionForClick(View view) {
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
            case R.id.manage_clubs:
                openActivity(4);
                break;
        }
    }

    private void openActivity(int key) {
        switch (key) {
            case 0:
                goTo(getContext(), ManageCircularActivity.class, false);
                break;
            case 1:
                goTo(getContext(), ManageNewsActivity.class, false);
                break;
            case 2:
                goTo(getContext(), ManageTeamsActivity.class, false);
                break;
            case 3:
                goTo(getContext(), ManageAdminsActivity.class, false);
                break;
            case 4:
                goTo(getContext(), ManageClubsActivity.class, false);
                break;
        }
    }

    private boolean isGeniuSAdmin() {
        return adminValue != null && adminValue.equals("0");
    }

    private boolean isSuperAdmin() {
        return adminValue != null && adminValue.equals("1");
    }

    private boolean isNewsAdmin() {
        return adminValue != null && adminValue.equals("2");
    }

    private boolean isNewsManager() {
        return adminValue != null && adminValue.equals("3");
    }

    private boolean isCircularAdmin() {
        return adminValue != null && adminValue.equals("4");
    }

    private boolean isCircularManager() {
        return adminValue != null && adminValue.equals("5");
    }

    private boolean isEditor() {
        return adminValue != null && adminValue.equals("6");
    }

    private String adminValue;

    public void refreshAdminsFragment() {
        adminValue = dataStorage.getString(Keys.ADMIN_VALUE);
    }

    public void setFragmentRefreshListener(FragmentInterfaceListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public FragmentInterfaceListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (getFragmentRefreshListener() != null && getView() != null) {
                getFragmentRefreshListener().refreshFragments();
            }
        }
    }

}
