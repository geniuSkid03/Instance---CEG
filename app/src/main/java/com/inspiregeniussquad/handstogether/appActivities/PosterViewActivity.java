package com.inspiregeniussquad.handstogether.appActivities;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.CircularDataItems;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.inspiregeniussquad.handstogether.appViews.ZoomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class PosterViewActivity extends SuperCompatActivity {

    @BindView(R.id.poster_image)
    ZoomImageView posterIv;

  /*  @BindView(R.id.close_preview)
    ImageView closePreviewIv;

    @BindView(R.id.save_preview)
    ImageView savePreviewIv;*/

    @BindView(R.id.image_placeholder)
    AVLoadingIndicatorView loadingIv;

    @BindView(R.id.save_fab)
    FloatingActionButton savePreviewIv;

    private NewsFeedItems newsFeedItems;
    private CircularDataItems circularDataItems;
    private ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_poster_view);

        if (getIntent().getExtras() != null) {
            newsFeedItems = gson.fromJson(getIntent().getStringExtra(Keys.NEWS_ITEM), NewsFeedItems.class);
            circularDataItems = gson.fromJson(getIntent().getStringExtra(Keys.CIRCULAR_ITEM), CircularDataItems.class);
        } else {
            dataStorage.saveBoolean(Keys.HOME_REFRESH_NEED, false);
            supportFinishAfterTransition();
        }

        imageLoader = ImageLoader.getInstance();

        if(newsFeedItems != null) {
            AppHelper.print("Poster url: "+newsFeedItems.getPstrUrl());

            Glide.with(this).load(newsFeedItems.getPstrUrl()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    posterIv.setImageDrawable(resource);
                    loadingIv.setVisibility(View.GONE);
                    loadingIv.hide();
                }
            });
            return;
        }

        if(circularDataItems != null) {
            AppHelper.print("Poster url: "+circularDataItems.getCircularImgPath());

            Glide.with(this).load(circularDataItems.getCircularImgPath()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    posterIv.setImageDrawable(resource);
                    loadingIv.setVisibility(View.GONE);
                    loadingIv.hide();
                }
            });
            return;
        }


//        File posterImage = DiskCacheUtils.findInCache(newsFeedItems.getPstrUrl(), imageLoader.getDiskCache());
//        if (posterImage != null && posterImage.exists()) {
//            AppHelper.print("Image file not null and found");
//            Picasso.get().load(posterImage).fit().into(posterIv, new Callback() {
//                @Override
//                public void onSuccess() {
//                    AppHelper.print("Image file found, success");
//                    posterIv.setVisibility(View.VISIBLE);
//                    loadingIv.hide();
//                    loadingIv.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    AppHelper.print("Image file error, error");
//                    posterIv.setVisibility(View.GONE);
//                    loadingIv.setVisibility(View.VISIBLE);
//                    loadingIv.show();
//                }
//            });
//        } else {
//            AppHelper.print("Image file not found");
//            imageLoader.loadImage(newsFeedItems.getPstrUrl(), new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//                    AppHelper.print("Image file loading started");
//                    posterIv.setVisibility(View.GONE);
//                    loadingIv.setVisibility(View.VISIBLE);
//                    loadingIv.show();
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    AppHelper.print("Image file loading failed");
//                    posterIv.setVisibility(View.GONE);
//                    loadingIv.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    AppHelper.print("Image file loading completed");
//                    posterIv.setVisibility(View.VISIBLE);
//                    loadingIv.setVisibility(View.GONE);
//                    loadingIv.hide();
//                    Picasso.get().load(imageUri).fit().into(posterIv);
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//                    AppHelper.print("Image file loading cancelled");
//
//                }
//            });
//        }
    }

    @OnClick({R.id.save_fab/*, R.id.close_preview*/})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.save_fab:
                checkAndSaveImage();
                break;
//            case R.id.close_preview:
////                finish();
//                dataStorage.saveBoolean(Keys.HOME_REFRESH_NEED, false);
//                supportFinishAfterTransition();
//                break;
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
