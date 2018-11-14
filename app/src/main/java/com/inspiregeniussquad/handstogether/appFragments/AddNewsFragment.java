package com.inspiregeniussquad.handstogether.appFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.squareup.picasso.Picasso;

public class AddNewsFragment extends SuperFragment {

    private Spinner teamNameSpinner;
    private EditText nameEd, descEd, venueEd, videoUrlEd;
    private TextView eventDateTv, eventTimeTv;
    private RadioGroup newsVisibilityGrp;
    private RadioButton showNewsRb, hideNewsRb;
    private ImageButton posterImgBtn;
    private ImageView posterIv;
    private AppCompatButton publishNewsBtn;

    private String teamName, eventDesc, eventName, venueName, videoUrl, eventDate, eventTime;
    private Uri eventPoster;
    private boolean isNewsVisible, isFirstCompleted = false;
    private int teamId = 0;

    private static final int CHOOSE_FILE = 101;



    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.add_news_fragment, container, false));
    }

    private View initView(View view) {

        teamNameSpinner = view.findViewById(R.id.team_spinner);
        nameEd = view.findViewById(R.id.event_name);
        descEd = view.findViewById(R.id.event_desc);
        eventDateTv = view.findViewById(R.id.event_date);
        eventTimeTv = view.findViewById(R.id.event_time);
        venueEd = view.findViewById(R.id.event_venue);
        posterIv = view.findViewById(R.id.event_poster);
        videoUrlEd = view.findViewById(R.id.event_video_url);
        newsVisibilityGrp = view.findViewById(R.id.news_rgrb);
        showNewsRb = view.findViewById(R.id.visible_news);
        hideNewsRb = view.findViewById(R.id.invisible_news);
        posterImgBtn = view.findViewById(R.id.add_event_poster);

        publishNewsBtn = view.findViewById(R.id.publish_news);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFunctionsForClick(view);
            }
        };

        publishNewsBtn.setOnClickListener(clickListener);
        posterImgBtn.setOnClickListener(clickListener);
        eventDateTv.setOnClickListener(clickListener);
        eventTimeTv.setOnClickListener(clickListener);

        showNewsRb.setChecked(true);
        isNewsVisible = true;
        newsVisibilityGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.visible_news:
                        isNewsVisible = true;
                        break;
                    case R.id.invisible_news:
                        isNewsVisible = false;
                        break;
                }
            }
        });

        return view;
    }

    private void doFunctionsForClick(View view) {
        switch (view.getId()) {
            case R.id.publish_news:
                if (isAllDetailsGiven()) {
                    publishNews();
                }
                break;
            case R.id.add_event_poster:
                openGallery();
                break;
            case R.id.event_date:
                //todo add date via date picker
                break;
            case R.id.event_time:
                //todo add time via time picker
                break;
        }
    }

    private boolean isAllDetailsGiven() {
        if(teamId == 0) {
            showToast(getString(R.string.choose_team));
            return false;
        }

        eventName = nameEd.getText().toString().trim();
        if(TextUtils.isEmpty(eventName)){
            showToast(getString(R.string.event_name_empty));
            return false;
        }

        eventDesc = descEd.getText().toString().trim();
        if(TextUtils.isEmpty(eventDesc)){
            showToast(getString(R.string.event_desc_empty));
            return false;
        }

        eventDate = eventDateTv.getText().toString().trim();
        if(TextUtils.isEmpty(eventDate)){
            showToast(getString(R.string.event_dare_empty));
            return false;
        }

        eventTime = eventTimeTv.getText().toString().trim();
        if(TextUtils.isEmpty(eventTime)){
            showToast(getString(R.string.event_time_empty));
            return false;
        }

        venueName = venueEd.getText().toString().trim();
        if(TextUtils.isEmpty(venueName)){
            showToast(getString(R.string.event_venue_empty));
            return false;
        }

        if(eventPoster == null) {
            showToast(getString(R.string.event_poster_empty));
            return false;
        }

        videoUrl = videoUrlEd.getText().toString().trim();

        if(!showNewsRb.isChecked() && !hideNewsRb.isChecked()) {
            showToast(getString(R.string.empty_news_visiblity));
            return false;
        }

        return true;
    }

    private void openGallery(){
        if (dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED)) {
            galleryIntent();
        } else {
            showToast(getString(R.string.permissoin_denied_string));
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), CHOOSE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE) {
                eventPoster = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), eventPoster);
                    if (bitmap != null) {
                        Picasso.get().load(eventPoster).into(posterIv);
                    } else {
                        AppHelper.showToast(getActivity(), "Image bitmap null");
                    }
                } catch (Exception e) {
                    AppHelper.showToast(getActivity(), "Exception in parsing image");
                }

            }
        }
    }

    private void publishNews() {

    }


}
