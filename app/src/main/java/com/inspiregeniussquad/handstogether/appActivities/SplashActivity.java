package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Admin;
import com.inspiregeniussquad.handstogether.appData.Clubs;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appStorage.TeamData;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

public class SplashActivity extends SuperCompatActivity {

    @BindView(R.id.logo)
    ImageView splashIv;

   /* @BindView(R.id.progress_bar)
    ProgressBar progressBar;*/

    @BindView(R.id.app_motto)
    TextView appMottoTv;

    @BindView(R.id.app_name)
    TextView appNameTv;

    @BindView(R.id.root_view)
    LinearLayout rootView;

    @BindView(R.id.splash_footer)
    LinearLayout splashFooterLayout;

    private ArrayList<TeamData> teamDataArrayList;
    private ArrayList<Clubs> clubsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_splash);

        teamDataArrayList = new ArrayList<>();
        clubsArrayList = new ArrayList<>();

        makeAnimations();
    }

    private void makeAnimations() {
//        splashIv.setVisibility(View.VISIBLE);
//        slideDown(splashIv, this);

        Animation pulse = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.pulse_anim);
        splashIv.startAnimation(pulse);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashFooterLayout.setVisibility(View.VISIBLE);
                slideUpView(splashFooterLayout, SplashActivity.this);
            }
        }, 1000);

        retrieveUserInfo();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                appNameTv.setVisibility(View.VISIBLE);
//                slideUpView(appNameTv, SplashActivity.this);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        appNameTv.setLayoutParams(new LinearLayout
//                                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//
//                        appMottoTv.setVisibility(View.VISIBLE);
//                        slideUpView(appMottoTv, SplashActivity.this);
//
//
//                    }
//                }, 1500);
//            }
//        }, 1000);
    }

    private void retrieveUserInfo() {
        adminDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("loading admin data");
                    parseUserInfo(dataSnapshot);
                } else {
                    onDataRetrived();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AppHelper.print("User info not loaded");
                AppHelper.print("Db Error: " + databaseError.getMessage());
            }
        });
    }

//    private void retrieveClubInfo() {
//        clubsDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    AppHelper.print("Clubs data exists and trying to retrieve!");
//                    retriveClubFromDb(dataSnapshot);
//                } else {
//                    AppHelper.print("No Clubs data found!");
//                    retriveTeamDatas();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                AppHelper.print("Error in retrieving club informations");
//                retriveTeamDatas();
//            }
//        });
//    }

//    private void retriveClubFromDb(DataSnapshot dataSnapshot) {
//        Map<String, Clubs> teamDataMap = (Map<String, Clubs>) dataSnapshot.getValue();
//
//        clubsArrayList.clear();
//
//        for (Map.Entry<String, Clubs> teamDataEntry : teamDataMap.entrySet()) {
//            Map map = (Map) teamDataEntry.getValue();
//
//            Clubs clubs = new Clubs();
//            clubs.setClubsId((String) map.get("clubsId"));
//            clubs.setClubsName((String) map.get("clubName"));
//            clubs.setClubsImgUrl((String) map.get("clubsImgUrl"));
//
//            clubsArrayList.add(clubs);
//
//            AppHelper.print("Retrived Clubs info: " + clubs.getClubsId() + "\t" + clubs.getClubsName() + "\t" + clubs.getClubsImgUrl());
//        }
//
//        insertClubsIntoDb(clubsArrayList);
//    }

//    private void insertClubsIntoDb(ArrayList<Clubs> clubsArrayList) {
//
//    }

    private void parseUserInfo(DataSnapshot dataSnapshot) {

        ArrayList<Admin> adminsArraylist = new ArrayList<>();

        Map<String, Admin> teamDataMap = (Map<String, Admin>) dataSnapshot.getValue();

        adminsArraylist.clear();

        AppHelper.print("Admins data\n----------------------------------------");

        for (Map.Entry<String, Admin> teamDataEntry : teamDataMap.entrySet()) {
            Map map = (Map) teamDataEntry.getValue();

            Admin admin = new Admin((String) map.get("name"), (String) map.get("mobile"),
                    (String) map.get("position"));

            AppHelper.print("Name: " + admin.getName());
            AppHelper.print("Mobile: " + admin.getMobile());
            AppHelper.print("Position: " + admin.getPosition());

            adminsArraylist.add(admin);
        }

        dataStorage.saveString(Keys.ADMIN_INFO, gson.toJson(adminsArraylist));

        AppHelper.print("Admin info available: " + dataStorage.isDataAvailable(Keys.ADMIN_INFO));

        for (Admin admin : adminsArraylist) {
            if (admin.getMobile().equalsIgnoreCase(dataStorage.getString(Keys.MOBILE))) {
                dataStorage.saveBoolean(Keys.IS_ADMIN, true);
                dataStorage.saveString(Keys.ADMIN_VALUE, admin.getPosition());
            }
        }

        onDataRetrived();
    }

//    public void retriveTeamDatas() {
//        teamDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    AppHelper.print("Team data exists and trying to retrive!");
//                    retriveDataFromDb(dataSnapshot);
//                } else {
//                    AppHelper.print("No team data found!");
//                    onDataRetrived();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                AppHelper.print("Error in retriving team data!");
//                onDataRetrived();
//            }
//        });
//    }

//    private void retriveDataFromDb(DataSnapshot dataSnapshot) {
//        Map<String, TeamData> teamDataMap = (Map<String, TeamData>) dataSnapshot.getValue();
//
//        teamDataArrayList.clear();
//
//        for (Map.Entry<String, TeamData> teamDataEntry : teamDataMap.entrySet()) {
//            Map map = (Map) teamDataEntry.getValue();
//
//            TeamData teamData = new TeamData();
//
//            teamData.setTeamName((String) map.get("teamName"));
//            teamData.setTeamMotto((String) map.get("teamMotto"));
//            teamData.setTeamLogoUrl((String) map.get("teamLogoUri"));
//
//            teamDataArrayList.add(teamData);
//
//            AppHelper.print("Retrived team info: " + teamData.getTeamName() + "\t" + teamData.getTeamMotto() + "\t" + teamData.getTeamLogoUrl());
//        }
//
//        insertIntoDb(teamDataArrayList);
//    }
//
//    private void insertIntoDb(final ArrayList<TeamData> teamDataArrayList) {
//
//        new TeamDbHelper(SplashActivity.this, teamDataArrayList, new TeamDbHelper.Action() {
//
//            @Override
//            public void onStart() {
//                AppHelper.print("Starting to insert team datas");
////                progressBar.setIndeterminate(false);
//            }
//
//            @Override
//            public void onUpdate(int progress) {
//                AppHelper.print("team datas update in progress: " + progress);
////                progressBar.setProgress(progress);
//            }
//
//            @Override
//            public void onEnd() {
//                AppHelper.print("team datas update completed");
//                onDataRetrived();
//            }
//        }).run();
//    }

    private  void onDataRetrived() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashIv.clearAnimation();
                checkDataAndOpen();
            }
        }, 2000);
    }

//    private void animateText(TextView view, boolean show) {
//        if(show) {
//            view.animate().translationY(view.getHeight())
//                    .setInterpolator(new AccelerateInterpolator(1));
//        } else {
//            view.animate().translationY(0).setInterpolator
//                    (new DecelerateInterpolator(1)).start();
//        }
//    }
//    private void animateView(final TextView textView) {
//        textView.animate()
//                .translationY(0)
//                .alpha(0.0f)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        textView.setVisibility(View.VISIBLE);
//                    }
//                });
//    }
//
//    public void animateViewFromBottomToTop(final View view){
//
//        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                final int TRANSLATION_Y = view.getHeight();
//                view.setTranslationY(TRANSLATION_Y);
//                view.setVisibility(View.GONE);
//                view.animate()
//                        .translationYBy(-TRANSLATION_Y)
//                        .setDuration(500)
//                        .setStartDelay(200)
//                        .setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationStart(final Animator animation) {
//                                view.setVisibility(View.VISIBLE);
//
//                                Toast.makeText(SplashActivity.this, "Anomation started", Toast.LENGTH_SHORT).show();
//
//                            }
//                        })
//                        .start();
//            }
//        });
//    }

//    public static void slideToBottom(View view){
//        TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
//        animate.setDuration(2000);
//        animate.setFillAfter(true);
//        view.startAnimation(animate);
//        view.setVisibility(View.VISIBLE);
//    }
//
//    public static void slideToTop(final View view){
// editingImageContainer.setDrawingCacheEnabled(true);
//        Bitmap bitmap = editingImageContainer.getDrawingCache();
//
//        File file = new File(Environment.getExternalStorageDirectory()
//                + File.separator + getString(R.string.app_name) + File.separator + getString(R.string.edited));
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
//        String ImgFile = fileName + ".jpg";
//        File toWriteFile = new File(file, ImgFile);
//
//        if (toWriteFile.exists()) {
//            toWriteFile.delete();
//        }
//
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(toWriteFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//            fileOutputStream.close();
//            isSaved = true;
//            showToast(EditorActivity.this, "Meme saved to: " + toWriteFile.getAbsolutePath());
//            refreshGallery(toWriteFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////
////        view.animate()
////                .setDuration(3000)
////                .alpha(0)
////                .se
////
//        TranslateAnimation animate = new TranslateAnimation(0,0,view.getHeight(),0);
////        animate.setDuration(3000);
////        animate.setFillAfter(true);
////        view.startAnimation(animate);
////        view.setVisibility(View.VISIBLE);
//
//        view.setAnimation(animate);
//        view.animate()
//                .setDuration(3000)
//                .translationY(view.getHeight())
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        super.onAnimationStart(animation);
//
//                        view.setVisibility(View.VISIBLE);
//                    }
//                });
//
//    }
}
