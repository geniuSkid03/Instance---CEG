package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;

public class NewsItemViewActivity extends SuperCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.event_poster)
    ImageView posterIv;

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

    @BindView(R.id.team_logo)
    CircularImageView teamLogoIv;

    @BindView(R.id.team_logo2)
    CircularImageView teamLogo2Iv;

    private NewsFeedItems toShowNewsItem;

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

        if (toShowNewsItem != null) {
            getSupportActionBar().setTitle(toShowNewsItem.geteName());
            //todo show ui for news item view
//            updateUi(toShowNewsItem);
        } else {
            AppHelper.print("To Show Item empty");
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

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
        });
    }

    private void showContentImg() {
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
    }

//    private void updateUi(NewsFeedItems toShowNewsItem) {
//        infoDescTv.setText(toShowNewsItem.getEventDesc());
//        titleTv.setText(toShowNewsItem.getTitle());
//        dateTv.setText(toShowNewsItem.getPostedDate());
//        timeTv.setText(toShowNewsItem.getPostedTime());
//
//        teamLogoIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.logo_spartanz));
//        teamLogo2Iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.logo_spartanz));
//
//        switch (toShowNewsItem.getId()) {
//            case 0:
//                posterIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.informals));
//                break;
//            case 1:
//                posterIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.intra_vareity_sho));
//                break;
//            case 2:
//                posterIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.twelve_years_ceg_spartans));
//                break;
//            case 3:
//                posterIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.mime_performace));
//                break;
//        }
//    }

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

