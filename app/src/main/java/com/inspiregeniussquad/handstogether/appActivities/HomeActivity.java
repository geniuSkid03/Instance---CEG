package com.inspiregeniussquad.handstogether.appActivities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.ExpandableListAdapter;
import com.inspiregeniussquad.handstogether.appAdapters.MyPagerAdapter;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NavMenuModels;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appFragments.AchievementsFragment;
import com.inspiregeniussquad.handstogether.appFragments.CulturalsFragment;
import com.inspiregeniussquad.handstogether.appFragments.HomeFragment;
import com.inspiregeniussquad.handstogether.appFragments.NonTechFragment;
import com.inspiregeniussquad.handstogether.appFragments.OthersFragment;
import com.inspiregeniussquad.handstogether.appFragments.SocialFragment;
import com.inspiregeniussquad.handstogether.appFragments.SportsFragment;
import com.inspiregeniussquad.handstogether.appFragments.TechFragment;
import com.inspiregeniussquad.handstogether.appInterfaces.FragmentInterfaceListener;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.inspiregeniussquad.handstogether.appViews.NoSwipeViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends SuperCompatActivity {


    @BindView(R.id.expandable_list)
    ExpandableListView expandableListView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.content_layout)
    LinearLayout contentLayout;

    @BindView(R.id.view_pager)
    NoSwipeViewPager noSwipeViewPager;

    private long backPressed = 0;
    private int lastExpandedPosition = -1;

    private ExpandableListAdapter expandableListAdapter;
    private NavMenuModels menuOne, menuTwo, menuThree, menuFour, menuFive;

    private List<NavMenuModels> headerList;
    private List<NavMenuModels> childModelsList;
    private HashMap<NavMenuModels, List<NavMenuModels>> childsList;

    private MyPagerAdapter myPagerAdapter;

    private HomeFragment homeFragment;
    private CulturalsFragment culturalsFragment;
    private TechFragment techFragment;
    private NonTechFragment nonTechFragment;
    private SocialFragment socialFragment;
    private SportsFragment sportsFragment;
    private AchievementsFragment achievementsFragment;
    private OthersFragment othersFragment;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (homeFragment != null && homeFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_HOME, homeFragment);
        }

        if (culturalsFragment != null && culturalsFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_CULTURALS, culturalsFragment);
        }

        if (techFragment != null && techFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_TECH, techFragment);
        }

        if (nonTechFragment != null && nonTechFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_NONTECH, nonTechFragment);
        }

        if (socialFragment != null && socialFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_SOCIAL, socialFragment);
        }

        if (sportsFragment != null && sportsFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_SPORTS, sportsFragment);
        }

        if (achievementsFragment != null && achievementsFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_ACHIEVEMENTS, achievementsFragment);
        }

        if (othersFragment != null && othersFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_OTHERS, othersFragment);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        headerList = new ArrayList<>();
        childModelsList = new ArrayList<>();
        childsList = new HashMap<>();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        //to load fragments for navigation drawers
        loadFragments(savedInstanceState);

        //adding menus to navigation drawer
        setUpNavigationMenus();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                contentLayout.setX(navigationView.getWidth() * slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        loadPersonalData(navigationView);
    }

    private void loadPersonalData(NavigationView navigationView) {
        View view = navigationView.getHeaderView(0);

        ImageView imageView = view.findViewById(R.id.profile_image); //for setting dp
        TextView nameTv = view.findViewById(R.id.user_name); //for name
        TextView emailTv = view.findViewById(R.id.user_email); //for email

        nameTv.setText("GnanaPriya");

//        if (dataStorage.isDataAvailable(Keys.USER_DATA)) {
//            Users users = gson.fromJson(dataStorage.getString(Keys.USER_DATA), Users.class);
//            if (users != null) {
//                nameTv.setText(users.getName());
//                emailTv.setText(users.getEmail());
//                dataStorage.saveString(Keys.USER_NAME, users.getName());
//                dataStorage.saveString(Keys.MOBILE, users.getMobile());
//                dataStorage.saveString(Keys.USER_EMAIL, users.getEmail());
//            }
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpNavigationMenus() {
        //creating navigation menu
        menuOne = new NavMenuModels(R.drawable.ic_dashboard, "Home", false, false);

        addToHeaderList(menuOne);

        menuTwo = new NavMenuModels(R.drawable.ic_clubs, "Clubs", false, true);

        NavMenuModels subMenuOne = new NavMenuModels(R.drawable.ic_sub_teams, "Tech", true, false);
        NavMenuModels subMenuTwo = new NavMenuModels(R.drawable.ic_sub_teams, "Non-tech", true, false);
        NavMenuModels subMenuThree = new NavMenuModels(R.drawable.ic_sub_teams, "Social", true, false);
        NavMenuModels subMenuFour = new NavMenuModels(R.drawable.ic_sub_teams, "Sports", true, false);
        NavMenuModels subMenuFive = new NavMenuModels(R.drawable.ic_sub_teams, "Cultural", true, false);

        childModelsList.add(subMenuOne);
        childModelsList.add(subMenuTwo);
        childModelsList.add(subMenuThree);
        childModelsList.add(subMenuFour);
        childModelsList.add(subMenuFive);

        addToHeaderList(menuTwo);
        addToChildList(menuTwo, childModelsList);

        menuThree = new NavMenuModels(R.drawable.ic_upcoming, "Achievements", false, false);
        addToHeaderList(menuThree);

        menuFour = new NavMenuModels(R.drawable.ic_others, "Others", false, false);
        addToHeaderList(menuFour);

        menuFive = new NavMenuModels(R.drawable.ic_sign_out, "Log out", false, false);
        addToHeaderList(menuFive);

        //generating expandable list view
        expandableListAdapter = new ExpandableListAdapter(this, headerList, childsList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                checkAndShowFragmentForGroups(groupPosition, expandableListAdapter.getGroup(groupPosition).hasChildren);
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                checkAndShowFragmentForChild(groupPosition, childPosition);
                expandableListView.collapseGroup(groupPosition);
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                expandableListView.setSelection(groupPosition);
            }
        });

    }

    private void addToHeaderList(NavMenuModels menu) {
//        if (!menu.hasChildren) {
        headerList.add(menu);
//        }
    }

    private void addToChildList(NavMenuModels menu, List<NavMenuModels> childModelsList) {
        if (menu.hasChildren) {
            childsList.put(menu, childModelsList);
        }
    }

    private void checkAndShowFragmentForChild(int groupPosition, int childPosition) {
        switch (groupPosition) {
            case 0:
                // dashboard has no child
                break;
            case 1:
                //calls menu has child
                switch (childPosition) {
                    case 0:
                        setAndShowFragment(Keys.FRAGMENT_TECH);
                        break;
                    case 1:
                        setAndShowFragment(Keys.FRAGMENT_NONTECH);
                        break;
                    case 2:
                        setAndShowFragment(Keys.FRAGMENT_SOCIAL);
                        break;
                    case 3:
                        setAndShowFragment(Keys.FRAGMENT_SPORTS);
                        break;
                    case 4:
                        setAndShowFragment(Keys.FRAGMENT_CULTURALS);
                        break;
                }
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    private void setAndShowFragment(String fragmentToShow) {
        if (!TextUtils.isEmpty(fragmentToShow)) {
            int fragmentId = myPagerAdapter.getFragment(fragmentToShow);
            noSwipeViewPager.setCurrentItem(fragmentId, false);
            setSelectedFragmentAs(fragmentId);

            AppHelper.print("Setting fragment as: " + fragmentToShow);
        }
    }

    private void setSelectedFragmentAs(int fragmentId) {
        //todo show selected fragment highlight color by referring fragmentID
        closeNavMenu();

    }

    private void closeNavMenu() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void checkAndShowFragmentForGroups(int groupPosition, boolean hasChildren) {
        if (!hasChildren) {
            switch (groupPosition) {
                case 0:
                    setAndShowFragment(Keys.FRAGMENT_HOME);
                    break;
                case 2:
                    setAndShowFragment(Keys.FRAGMENT_ACHIEVEMENTS);
                    break;
                case 3:
                    setAndShowFragment(Keys.FRAGMENT_OTHERS);
                    break;
                case 4:
                    showSnack(getString(R.string.function_not_set));
                    //showSignoutAlert();
                    break;
            }
        }
    }

    private void showSignoutAlert() {
        closeNavMenu();

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Are you sure, you want to sign out?");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataStorage.removeAllData();
                goTo(HomeActivity.this, MobileNumberActivity.class, true);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if (!alertDialog.isShowing() && !isFinishing()) {
            alertDialog.show();
        }
    }

    private void loadFragments(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            homeFragment = (HomeFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_HOME);
            culturalsFragment = (CulturalsFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_CULTURALS);
            nonTechFragment = (NonTechFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_NONTECH);
            techFragment = (TechFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_TECH);
            socialFragment = (SocialFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_SOCIAL);
            sportsFragment = (SportsFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_SPORTS);
            achievementsFragment = (AchievementsFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_ACHIEVEMENTS);
            othersFragment = (OthersFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_OTHERS);
        }

        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }

        if (culturalsFragment == null) {
            culturalsFragment = new CulturalsFragment();
        }

        if (nonTechFragment == null) {
            nonTechFragment = new NonTechFragment();
        }

        if (techFragment == null) {
            techFragment = new TechFragment();
        }

        if (socialFragment == null) {
            socialFragment = new SocialFragment();
        }

        if (sportsFragment == null) {
            sportsFragment = new SportsFragment();
        }

        if (achievementsFragment == null) {
            achievementsFragment = new AchievementsFragment();
        }

        if (othersFragment == null) {
            othersFragment = new OthersFragment();
        }

        homeFragment.setFragmentRefreshListener(new FragmentInterfaceListener() {
            @Override
            public void refreshFragments() {
                homeFragment.refreshHomeFragment();
            }
        });

        myPagerAdapter.addFragment(Keys.FRAGMENT_HOME, homeFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_CULTURALS, culturalsFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_NONTECH, nonTechFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_TECH, techFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_SOCIAL, socialFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_SPORTS, sportsFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_ACHIEVEMENTS, achievementsFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_OTHERS, othersFragment);

        noSwipeViewPager.setAdapter(myPagerAdapter);
        noSwipeViewPager.setOffscreenPageLimit(8);
    }

    private void refreshFragments() {
        Fragment currentVisibleFragment = getCurrentVisibleFragment();

        if (currentVisibleFragment != null) {
            if (currentVisibleFragment instanceof HomeFragment) {
                homeFragment.refreshHomeFragment();
            }
        }
    }

    private Fragment getCurrentVisibleFragment() {
        FragmentManager fragmentManager = HomeActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.getUserVisibleHint())
                return fragment;
        }
        return null;
    }

    private void performOnResumeOperations() {
        dataStorage.saveBoolean(Keys.IS_ONLINE, true);

        //default fragment
        setAndShowFragment(Keys.FRAGMENT_HOME);

        //refreshing fragments after a second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshFragments();
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        performOnResumeOperations();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeNavMenu();
            return;
        }

        if (backPressed == 0) {
            backPressed = System.currentTimeMillis();
            showSnack(getString(R.string.press_again_to_exit));
        } else if (System.currentTimeMillis() - backPressed <= 3500) {
            super.onBackPressed();
        } else {
            backPressed = 0;
            onBackPressed();
        }

    }
}
