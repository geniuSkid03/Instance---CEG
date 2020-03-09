package com.instance.ceg.appActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.instance.ceg.R;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.NewsFeedItems;
import com.instance.ceg.appHelpers.DbHelper;
import com.instance.ceg.appUtils.AppHelper;
import com.instance.ceg.appViews.CircularImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
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
//    private ImageLoader imageLoader;

    @BindView(R.id.watch_video)
    AppCompatButton watchVideoBtn;

    @BindView(R.id.bookmark_fab)
    AppCompatButton bookmarkFab;
    @BindView(R.id.share_fab) AppCompatButton shareFab;
    @BindView(R.id.venue_tv) TextView venueTv;

      /*  private ArrayList<TeamData> teamDataArrayList;
    private String teamLogoUrl;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item_view_new);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            updateUi(toShowNewsItem);
        } else {
            AppHelper.print("To Show Item empty");
        }

        bookmarkFab.setOnClickListener(v ->
                openWithImageTransition(NewsItemViewActivity.this, PosterViewActivity.class, false, posterIv1,
                        Keys.NEWS_ITEM, gson.toJson(toShowNewsItem)));

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

        posterIv1.setOnClickListener(v -> openWithImageTransition(NewsItemViewActivity.this, PosterViewActivity.class, false, posterIv1,
                Keys.NEWS_ITEM, gson.toJson(toShowNewsItem)));

        watchVideoBtn.setOnClickListener(v -> goTo(NewsItemViewActivity.this, VideoViewActivity.class, false, "video_id", toShowNewsItem.getVidUrl()));

        shareFab.setOnClickListener(v -> {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Glide.with(this).asBitmap().load(toShowNewsItem.getPstrUrl()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Intent shareIntent;
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Share.png";
                    OutputStream out = null;
                    File file = new File(path);
                    try {
                        out = new FileOutputStream(file);
                        resource.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    path = file.getPath();
                    Uri bmpUri = Uri.parse("file://" + path);
                    shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Check this event " + toShowNewsItem.geteName() + "\n" + "on: " + toShowNewsItem.geteTime() + " at " + toShowNewsItem.geteVenue());
                    shareIntent.setType("image/png");
                    startActivity(Intent.createChooser(shareIntent, "Share with"));
                }
            });
        });
    }

    private void checkAndAddToBookmark() {
        new DbHelper().getUserBookmarks(dataStorage, new DbHelper.BookmarkCallback() {
            @Override
            public void onLoaded(List<String> bookmarkList) {
                if (bookmarkList != null && bookmarkList.size() > 0) {
                    if (bookmarkList.contains(toShowNewsItem.getNfId())) {
                        removeFromBookmarks();
                    } else {
                        addToBookmarks();
                    }
                } else {
                    addToBookmarks();
                }
            }

            @Override
            public void onError() {
                cancelProgress();
                AppHelper.print(getString(R.string.some_error_occurred));
                AppHelper.showToast(NewsItemViewActivity.this, getString(R.string.some_error_occurred));
            }
        });
    }

    private void removeFromBookmarks() {
        AppHelper.print("Removing from bookmarks");
        new DbHelper().removeFromBookmark(toShowNewsItem.getNfId(), dataStorage, new DbHelper.UserDbCallback() {
            @Override
            public void onUpdated() {
                AppHelper.print("Removed from bookmarks");
                cancelProgress();
                //bookmarkFab.setImageDrawable(ContextCompat.getDrawable(NewsItemViewActivity.this, R.drawable.ic_bookmark_outine));
                AppHelper.showToast(NewsItemViewActivity.this, getString(R.string.removed_from_bookmarks));
                AppHelper.print(getString(R.string.removed_from_bookmarks));
            }

            @Override
            public void onFailed() {
                cancelProgress();
                AppHelper.showToast(NewsItemViewActivity.this, getString(R.string.some_error_occurred));
                AppHelper.print(getString(R.string.some_error_occurred));
            }
        });
    }

    private void addToBookmarks() {
        AppHelper.print("Adding to bookmarks");
        new DbHelper().addToBookmarks(dataStorage, toShowNewsItem.getNfId(), new DbHelper.UserDbCallback() {
            @Override
            public void onUpdated() {
                AppHelper.print("Added to bookmarks");
                cancelProgress();
                AppHelper.showToast(NewsItemViewActivity.this, getString(R.string.added_to_bookmarks));
                //bookmarkFab.setImageDrawable(ContextCompat.getDrawable(NewsItemViewActivity.this, R.drawable.ic_bookmark_filled));
            }

            @Override
            public void onFailed() {
                cancelProgress();
                AppHelper.showToast(NewsItemViewActivity.this, getString(R.string.some_error_occurred));
                AppHelper.print(getString(R.string.some_error_occurred));
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

        watchVideoBtn.setVisibility(TextUtils.isEmpty(newsFeedItems.getVidUrl()) ? View.GONE : View.VISIBLE);

        Glide.with(this).load(newsFeedItems.getPstrUrl()).into(posterIv1);
        Glide.with(this).load(newsFeedItems.gettLogo()).into(teamLogo2Iv);

        infoDescTv.setText(newsFeedItems.geteDesc());
        titleTv.setText(newsFeedItems.geteName());
        dateTv.setText(newsFeedItems.geteDate());
        timeTv.setText(newsFeedItems.geteTime());
        venueTv.setText(newsFeedItems.geteVenue());

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

        //checkBookmark();
    }

    private void checkBookmark() {
        new DbHelper().getUserBookmarks(dataStorage, new DbHelper.BookmarkCallback() {
            @Override
            public void onLoaded(List<String> bookmarkList) {
                if (bookmarkList != null && bookmarkList.size() > 0) {
                   // bookmarkFab.setImageDrawable(bookmarkList.contains(toShowNewsItem.getNfId()) ?
                     //       ContextCompat.getDrawable(NewsItemViewActivity.this, R.drawable.ic_bookmark_filled) :
                       //     ContextCompat.getDrawable(NewsItemViewActivity.this, R.drawable.ic_bookmark_outine));
                } else {
                    //bookmarkFab.setImageDrawable(ContextCompat.getDrawable(NewsItemViewActivity.this, R.drawable.ic_bookmark_outine));
                }
            }

            @Override
            public void onError() {
                //bookmarkFab.setImageDrawable(ContextCompat.getDrawable(NewsItemViewActivity.this, R.drawable.ic_bookmark_outine));
            }
        });
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

