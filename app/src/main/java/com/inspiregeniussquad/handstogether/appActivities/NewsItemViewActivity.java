package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.inspiregeniussquad.handstogether.appViews.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;

public class NewsItemViewActivity extends SuperCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

   /* @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;*/

    /*@BindView(R.id.event_poster)
    ImageView posterIv;*/

    @BindView(R.id.event_poster_1)
    ImageView posterIv1;

    @BindView(R.id.nested_scrollview)
    NestedScrollView nestedScrollView;

    @BindView(R.id.desc_view)
    TextView infoDescTv;

    @BindView(R.id.title)
    TextView titleTv;

    @BindView(R.id.date)
    TextView dateTv;

    @BindView(R.id.time)
    TextView timeTv;

   /* @BindView(R.id.team_logo)
    CircularImageView teamLogoIv;*/

    @BindView(R.id.team_logo2)
    CircularImageView teamLogo2Iv;

    private NewsFeedItems toShowNewsItem;
    private ImageLoader imageLoader;

      /*  private ArrayList<TeamData> teamDataArrayList;
    private String teamLogoUrl;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item_view_new);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (getIntent().getExtras() != null) {
            String item = getIntent().getStringExtra(Keys.NEWS_ITEM);
            toShowNewsItem = gson.fromJson(item, NewsFeedItems.class);
            AppHelper.print("Received intent data");
        } else {
            showSnack("Sorry, Cannot View item currently!");
            finish();
        }

        imageLoader = ImageLoader.getInstance();


        if (toShowNewsItem != null) {
            getSupportActionBar().setTitle(toShowNewsItem.geteName());
            updateUi(toShowNewsItem);
        } else {
            AppHelper.print("To Show Item empty");
        }

        /*appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    showContentImg();
                } else if (isShow) {
                    isShow = false;
                    hideContentImg();
                }
            }
        });*/

        posterIv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWithImageTransition(NewsItemViewActivity.this, PosterViewActivity.class, false, posterIv1,
                        Keys.NEWS_ITEM, gson.toJson(toShowNewsItem));
            }
        });
    }

   /* private void showContentImg() {
        teamLogoIv.startAnimation(scaleDownAnim);
        teamLogoIv.setVisibility(View.INVISIBLE);

        teamLogo2Iv.startAnimation(scaleUpAnim);
        teamLogo2Iv.setVisibility(View.VISIBLE);
    }

    private void hideContentImg() {
        teamLogoIv.startAnimation(scaleUpAnim);
        teamLogoIv.setVisibility(View.VISIBLE);

        teamLogo2Iv.startAnimation(scaleDownAnim);
        teamLogo2Iv.setVisibility(View.INVISIBLE);
    }*/


    private void updateUi(final NewsFeedItems newsFeedItems) {

        Glide.with(this).load(newsFeedItems.getPstrUrl()).into(posterIv1);
        Glide.with(this).load(newsFeedItems.gettLogo()).into(teamLogo2Iv);

//        File posterImage = DiskCacheUtils.findInCache(newsFeedItems.getPstrUrl(), imageLoader.getDiskCache());
//        if (posterImage != null && posterImage.exists()) {
////            Picasso.get().load(posterImage).fit().into(posterIv);
//            Picasso.get().load(posterImage).fit().into(posterIv1);
//        } else {
//            imageLoader.loadImage(newsFeedItems.getPstrUrl(), new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////                    Picasso.get().load(imageUri).fit().into(posterIv);
//                    Picasso.get().load(imageUri).fit().into(posterIv1);
//                    newsFeedItems.setPosterUri(imageUri);
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//
//                }
//            });
//        }

        infoDescTv.setText(newsFeedItems.geteDesc());
        titleTv.setText(newsFeedItems.geteName());
        dateTv.setText(newsFeedItems.geteDate());
        timeTv.setText(newsFeedItems.geteTime());

       /* new TeamDbHelper(this, true, new TeamDbHelper.RetrivalAction(){

            @Override
            public void onStart() {
                showProgress(getString(R.string.loading));
            }

            @Override
            public void onEnd(ArrayList<TeamData> teamDataArrayList) {
                NewsItemViewActivity.this.teamDataArrayList = teamDataArrayList;
                cancelProgress();
            }
        });

        if(teamDataArrayList != null && teamDataArrayList.size() > 0) {
            for(TeamData teamData : teamDataArrayList) {

                AppHelper.print("Team name: "+teamData.getTeamName());

                if(teamData.getTeamName().equalsIgnoreCase(newsFeedItems.gettName())) {
                    teamLogoUrl = teamData.getTeamLogoUrl();
                }
            }
        }

        imageLoader.loadImage(newsFeedItems.getPosterUri(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                Picasso.get().load(imageUri).into(teamLogoIv);
                Picasso.get().load(imageUri).into(teamLogo2Iv);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
*/
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
}

