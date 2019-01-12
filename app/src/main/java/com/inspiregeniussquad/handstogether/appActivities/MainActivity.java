package com.inspiregeniussquad.handstogether.appActivities;

import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.MyPagerAdapter;
import com.inspiregeniussquad.handstogether.appAdapters.NavMenuAdapter;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NavMenu;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appFragments.AboutFragment;
import com.inspiregeniussquad.handstogether.appFragments.AdminFragment;
import com.inspiregeniussquad.handstogether.appFragments.BookmarksFragment;
import com.inspiregeniussquad.handstogether.appFragments.ClubsFragment;
import com.inspiregeniussquad.handstogether.appFragments.HomeFragment;
import com.inspiregeniussquad.handstogether.appFragments.SettingsFragment;
import com.inspiregeniussquad.handstogether.appInterfaces.FragmentInterfaceListener;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.inspiregeniussquad.handstogether.appViews.NoSwipeViewPager;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends SuperCompatActivity {

    @BindView(R.id.nav_menu_recycler)
    RecyclerView navMenuRv;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    LinearLayout navigationView;

    @BindView(R.id.content_layout)
    LinearLayout contentLayout;

    @BindView(R.id.view_pager)
    NoSwipeViewPager noSwipeViewPager;

    @BindView(R.id.profile_image)
    CircularImageView imageView;

    @BindView(R.id.user_name)
    TextView nameTv;

    @BindView(R.id.edit)
    ImageView editIv;

    private long backPressed = 0;

    private MyPagerAdapter myPagerAdapter;

    private HomeFragment homeFragment;
    private ClubsFragment clubsFragment;
    private SettingsFragment settingsFragment;
    private AdminFragment adminFragment;
    private BookmarksFragment bookmarksFragment;
    private AboutFragment aboutFragment;

    private NavMenuAdapter navMenuAdapter;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (homeFragment != null && homeFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_HOME, homeFragment);
        }

        if (settingsFragment != null && settingsFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_SETTINGS, settingsFragment);
        }

        if (adminFragment != null && adminFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_ADMIN, adminFragment);
        }

        if (settingsFragment != null && settingsFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_SETTINGS, settingsFragment);
        }

        if (clubsFragment != null && clubsFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_CLUBS, clubsFragment);
        }

        if (bookmarksFragment != null) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_BOOKMARKS, bookmarksFragment);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setTitle(null);
        }


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

        loadPersonalData();

        dataStorage.saveBoolean(Keys.IS_ONLINE, true);
        dataStorage.saveBoolean(Keys.HOME_REFRESH_NEED, true);
    }

    private void loadFragments(Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (bundle != null) {
            homeFragment = (HomeFragment) fragmentManager.getFragment(bundle, Keys.FRAGMENT_HOME);
            clubsFragment = (ClubsFragment) fragmentManager.getFragment(bundle, Keys.FRAGMENT_CLUBS);
            adminFragment = (AdminFragment) fragmentManager.getFragment(bundle, Keys.FRAGMENT_ADMIN);
            settingsFragment = (SettingsFragment) fragmentManager.getFragment(bundle, Keys.FRAGMENT_SETTINGS);
            aboutFragment = (AboutFragment) fragmentManager.getFragment(bundle, Keys.FRAGMENT_ABOUT);
            bookmarksFragment = (BookmarksFragment) fragmentManager.getFragment(bundle, Keys.FRAGMENT_BOOKMARKS);
        }

        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        if (clubsFragment == null) {
            clubsFragment = new ClubsFragment();
        }
        if (adminFragment == null) {
            adminFragment = new AdminFragment();
        }
        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
        }
        if (aboutFragment == null) {
            aboutFragment = new AboutFragment();
        }

        if (bookmarksFragment == null) {
            bookmarksFragment = new BookmarksFragment();
        }

        homeFragment.setFragmentRefreshListener(new FragmentInterfaceListener() {
            @Override
            public void refreshFragments() {
                homeFragment.refreshHomeFragment();
            }
        });

        clubsFragment.setFragmentRefreshListener(new FragmentInterfaceListener() {
            @Override
            public void refreshFragments() {
                clubsFragment.refreshClubsFragment();
            }
        });

        bookmarksFragment.setFragmentInterfaceListener(new FragmentInterfaceListener() {
            @Override
            public void refreshFragments() {
                bookmarksFragment.refreshBookmarks();
            }
        });

        if (dataStorage.getBoolean(Keys.IS_ADMIN)) {
            adminFragment.setFragmentRefreshListener(new FragmentInterfaceListener() {
                @Override
                public void refreshFragments() {
                    adminFragment.refreshAdminsFragment();
                }
            });

        }

        myPagerAdapter.addFragment(Keys.FRAGMENT_HOME, homeFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_CLUBS, clubsFragment);

        if (dataStorage.getBoolean(Keys.IS_ADMIN)) {
            myPagerAdapter.addFragment(Keys.FRAGMENT_ADMIN, adminFragment);
        }
        myPagerAdapter.addFragment(Keys.FRAGMENT_BOOKMARKS, bookmarksFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_SETTINGS, settingsFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_ABOUT, aboutFragment);

        noSwipeViewPager.setAdapter(myPagerAdapter);
        noSwipeViewPager.setOffscreenPageLimit(5);
    }

    private void loadPersonalData() {
        editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateProfile();
            }
        });

        if (dataStorage.isDataAvailable(Keys.USER_DATA)) {
            Users users = gson.fromJson(dataStorage.getString(Keys.USER_DATA), Users.class);
            if (users != null) {
                nameTv.setText(users.getName());

                imageView.setBackground(users.getGender().equalsIgnoreCase(Keys.MALE) ?
                        ContextCompat.getDrawable(this, R.drawable.ic_man) : users.getGender().equalsIgnoreCase(Keys.FEMALE) ?
                        ContextCompat.getDrawable(this, R.drawable.ic_girl) : ContextCompat.getDrawable(this, R.drawable.gender_unspecified));
            }
        }

        AppHelper.print("IsAdmin: " + dataStorage.getBoolean(Keys.IS_ADMIN));
    }

    private void openUpdateProfile() {
        goTo(this, ProfileUpdatingActivity.class, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    private void setUpNavigationMenus() {
        ArrayList<NavMenu> navMenuArrayList = new ArrayList<>();

        navMenuArrayList.add(new NavMenu(getString(R.string.home), R.drawable.ic_dashboard, R.drawable.ic_dashboard_unselected));
        navMenuArrayList.add(new NavMenu(getString(R.string.clubs), R.drawable.ic_clubs, R.drawable.ic_clubs_unselected));

        if (dataStorage.getBoolean(Keys.IS_ADMIN)) {
            navMenuArrayList.add(new NavMenu(getString(R.string.admin_panel), R.drawable.ic_admin_panel, R.drawable.ic_admin_panel_unselected));
        }

        navMenuArrayList.add(new NavMenu(getString(R.string.bookmsrks), R.drawable.ic_bookmark_done, R.drawable.ic_bookmark_icon));
        navMenuArrayList.add(new NavMenu(getString(R.string.settings), R.drawable.ic_settings, R.drawable.ic_settings_unselected));
        navMenuArrayList.add(new NavMenu(getString(R.string.about), R.drawable.ic_about, R.drawable.ic_about_unselected));
        navMenuArrayList.add(new NavMenu(getString(R.string.sign_out), R.drawable.ic_sign_out, R.drawable.ic_sign_out_unselected));

        navMenuAdapter = new NavMenuAdapter(this, navMenuArrayList, new NavMenuAdapter.NavOnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                navMenuAdapter.setSelectedPosition(position);
                onNavItemClicked(position);
            }
        });
        navMenuRv.setAdapter(navMenuAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        navMenuRv.setLayoutManager(layoutManager);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);
//        navMenuRv.addItemDecoration(dividerItemDecoration);

        navMenuAdapter.setSelectedPosition(0);
    }

    private void onNavItemClicked(int position) {
        if (dataStorage.getBoolean(Keys.IS_ADMIN)) {
            switch (position) {
                case 0:
                    setAndShowFragment(Keys.FRAGMENT_HOME);
                    break;
                case 1:
                    setAndShowFragment(Keys.FRAGMENT_CLUBS);
                    break;
                case 2:
                    setAndShowFragment(Keys.FRAGMENT_ADMIN);
                    break;
                case 3:
                    setAndShowFragment(Keys.FRAGMENT_BOOKMARKS);
                    break;
                case 4:
                    setAndShowFragment(Keys.FRAGMENT_SETTINGS);
                    break;
                case 5:
                    setAndShowFragment(Keys.FRAGMENT_ABOUT);
                    break;
                case 6:
                    showSignoutAlert();
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    setAndShowFragment(Keys.FRAGMENT_HOME);
                    break;
                case 1:
                    setAndShowFragment(Keys.FRAGMENT_CLUBS);
                    break;
                case 2:
                    setAndShowFragment(Keys.FRAGMENT_BOOKMARKS);
                    break;
                case 3:
                    setAndShowFragment(Keys.FRAGMENT_SETTINGS);
                    break;
                case 4:
                    setAndShowFragment(Keys.FRAGMENT_ABOUT);
                    break;
                case 5:
                    showSignoutAlert();
                    break;
            }
        }
    }

    private void setAndShowFragment(String fragmentToShow) {
        int fragmentId = myPagerAdapter.getFragment(fragmentToShow);
        noSwipeViewPager.setCurrentItem(fragmentId, false);
        closeNavMenu();
        AppHelper.print("Setting fragment as: " + fragmentToShow);
    }

    private void closeNavMenu() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void showSignoutAlert() {
        closeNavMenu();

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getString(R.string.log_out_hint));
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataStorage.removeAllData();
                goTo(MainActivity.this, SplashActivity.class, true);
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

    private void refreshFragments() {
        Fragment currentVisibleFragment = getCurrentVisibleFragment();

        if (currentVisibleFragment != null) {
            if (currentVisibleFragment instanceof HomeFragment) {
                if (dataStorage.getBoolean(Keys.HOME_REFRESH_NEED)) {
                    homeFragment.refreshHomeFragment();
                }
            }
        }
    }

    private Fragment getCurrentVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
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

        if (noSwipeViewPager.getCurrentItem() > 0) {
            setAndShowFragment(Keys.FRAGMENT_HOME);
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

    public void changeTheme(boolean isChecked) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, isChecked);
        editor.apply();

        TaskStackBuilder.create(this).addNextIntent(new Intent(this, MainActivity.class)).addNextIntent(getIntent())
                .startActivities();

//        Intent intent = getIntent();
//        finish();
//
//        startActivity(intent);
    }

}
