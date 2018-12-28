package com.inspiregeniussquad.handstogether.appActivities;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.inspiregeniussquad.handstogether.appUtils.ImageSaver;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

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

    @BindView(R.id.image_placeholder)
    ImageView loadingIv;

    private NewsFeedItems newsFeedItems;
    private ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_view);

        if (getIntent().getExtras() != null) {
            newsFeedItems = gson.fromJson(getIntent().getStringExtra(Keys.NEWS_ITEM), NewsFeedItems.class);
        } else {
            dataStorage.saveBoolean(Keys.HOME_REFRESH_NEED, false);
            finish();
        }

        imageLoader = ImageLoader.getInstance();

        File posterImage = DiskCacheUtils.findInCache(newsFeedItems.getPstrUrl(), imageLoader.getDiskCache());
        if (posterImage != null && posterImage.exists()) {
            Picasso.get().load(posterImage).fit().into(posterIv, new Callback() {
                @Override
                public void onSuccess() {
                    posterIv.setVisibility(View.VISIBLE);
                    loadingIv.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    posterIv.setVisibility(View.GONE);
                    loadingIv.setVisibility(View.VISIBLE);
                }
            });
        } else {
            imageLoader.loadImage(newsFeedItems.getPstrUrl(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    posterIv.setVisibility(View.GONE);
                    loadingIv.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    posterIv.setVisibility(View.GONE);
                    loadingIv.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    posterIv.setVisibility(View.VISIBLE);
                    loadingIv.setVisibility(View.GONE);
                    Picasso.get().load(imageUri).fit().into(posterIv);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
    }

    @OnClick({R.id.save_preview, R.id.close_preview})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.save_preview:
                checkAndSaveImage();
                break;
            case R.id.close_preview:
//                finish();
                dataStorage.saveBoolean(Keys.HOME_REFRESH_NEED, false);
                supportFinishAfterTransition();
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
        posterIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = posterIv.getDrawingCache();

        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + getString(R.string.app_name) + File.separator + getString(R.string.posters));
        if (!file.exists()) {
            file.mkdirs();
        }

        String ImgFile = newsFeedItems.geteName() + "_" + new Date().getTime() + ".jpg";
        File toWriteFile = new File(file, ImgFile);

        if (toWriteFile.exists()) {
            toWriteFile.delete();
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(toWriteFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            showToast(PosterViewActivity.this, "Poster saved to: " + toWriteFile.getAbsolutePath());
            refreshGallery(toWriteFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshGallery(File toWriteFile) {
        MediaScannerConnection.scanFile(this,
                new String[]{toWriteFile.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        AppHelper.print("ExternalStorage Scanned " + path);
                        AppHelper.print("ExternalStorage -> uri=" + uri);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        dataStorage.saveBoolean(Keys.HOME_REFRESH_NEED, false);
        supportFinishAfterTransition();
    }
}
