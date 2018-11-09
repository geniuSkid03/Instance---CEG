package com.inspiregeniussquad.handstogether.appFragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
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

public class AddNewsFragment extends SuperFragment {

    private Spinner teamNameSpinner;
    private EditText nameEd, descEd, venueEd, videoUrlEd;
    private TextView eventDateTv, eventTimeTv;
    private RadioGroup newsVisibilityGrp;
    private RadioButton showNewsRb, hideNewsRb;
    private ImageButton posterImgBtn;
    private ImageView posterIv;
    private AppCompatButton addTeamBtn;

    private String teamName, eventDesc, eventName, venueName, videoUrl, eventDate, eventTime;
    private Uri eventPoster;
    private boolean isNewsVisible, isFirstCompleted = false;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.add_news_fragment, container, false));
    }

    private View initView(View view) {

        addTeamBtn = view.findViewById(R.id.next_btn);
        addTeamBtn.setText(getString(R.string.next));

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFunctionsForClick(view);
            }
        };

        addTeamBtn.setOnClickListener(clickListener);

        return view;
    }

    private void doFunctionsForClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                if(isFirstCompleted) {
                   showTeamMemberView();
                } else {
                    hideTeamMemberView();
                }
                break;
        }
    }

    private void showTeamMemberView() {
        addTeamBtn.setText(getString(R.string.add_team));
        //hide team member name entering view
    }

    private void hideTeamMemberView() {
        addTeamBtn.setText(getString(R.string.next));
        isFirstCompleted = true;
        //get all details about team members and hide team member name view
        //after getting all values hide those views and show the view for entering member name and spinner each
    }
}
