package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.inspiregeniussquad.handstogether.R;

public class VideoViewActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener youtubeInitListener;

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    String videoUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_view);

        if(getIntent().getExtras() != null) {
            videoUrl = getIntent().getStringExtra("video_id");
        } else {
            finish();
        }

        youTubePlayerView = findViewById(R.id.youtube_view);
        youtubeInitListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoUrl);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog(VideoViewActivity.this, RECOVERY_DIALOG_REQUEST).show();
                } else {
                    String errorMessage = String.format(
                            getString(R.string.error_player), youTubeInitializationResult.toString());
                    Toast.makeText(VideoViewActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        };
        youTubePlayerView.initialize(getString(R.string.youtube_api_key), youtubeInitListener);

    }

//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//        if (!b) {
//
//            // loadVideo() will auto play video
//            // Use cueVideo() method, if you don't want to play it automatically
//            youTubePlayer.loadVideo("_oEA18Y8gM0");
//
//            // Hiding player controls
//            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
//        }
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider,
//                                        YouTubeInitializationResult youTubeInitializationResult) {
//        if (youTubeInitializationResult.isUserRecoverableError()) {
//            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
//        } else {
//            String errorMessage = String.format(
//                    getString(R.string.error_player), youTubeInitializationResult.toString());
//            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RECOVERY_DIALOG_REQUEST) {
//            // Retry initialization if user performed a recovery action
//            getYouTubePlayerProvider().initialize(getString(R.string.youtube_api_key), this);
//        }
//    }
//
//    private YouTubePlayer.Provider getYouTubePlayerProvider() {
//        return (YouTubePlayerView) findViewById(R.id.youtube_view);
//    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
