package com.inspiregeniussquad.handstogether.appActivities;

import android.Manifest;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appUtils.ImageSaver;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

public class PosterViewActivity extends SuperCompatActivity {

    @BindView(R.id.poster_image)
    ImageView posterIv;

    @BindView(R.id.close_preview)
    ImageView closePreviewIv;

    @BindView(R.id.save_preview)
    ImageView savePreviewIv;

    @BindView(R.id.title)
    TextView posterTitleTv;

    private NewsFeedItems newsFeedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_view);

        if (getIntent().getExtras() != null) {
            newsFeedItems = gson.fromJson(getIntent().getStringExtra(Keys.NEWS_ITEM), NewsFeedItems.class);

            posterTitleTv.setText(newsFeedItems.geteName());
            Picasso.get().load(newsFeedItems.getPstrUrl()).into(posterIv);
        } else {
            finish();
        }
    }

    @OnClick({R.id.save_preview, R.id.close_preview})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.save_preview:
                checkAndSaveImage();
                break;
            case R.id.close_preview:
                finish();
                break;
        }
    }

    private void checkAndSaveImage() {
        if (isAboveM()) {
            permissionHelper.initPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
            if (permissionHelper.isAllPermissionAvailable()) {
                proceedSaving();
            } else {
                showToast(this, getString(R.string.permissoin_denied_string));
            }
        } else {
            proceedSaving();
        }
    }

    private void proceedSaving() {
//        new ImageSaver(this)
//                .setDirectoryName(getString(R.string.app_name))
//                .setFileName(newsFeedItems.geteName() + "_Poster.jpg")
//                .save(((BitmapDrawable) posterIv.getDrawable()).getBitmap());
        showToast(this, getString(R.string.function_not_set));
    }
}
