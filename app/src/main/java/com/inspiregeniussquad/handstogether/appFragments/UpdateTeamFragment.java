package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.inspiregeniussquad.handstogether.R;
import com.mikhaellopez.circularimageview.CircularImageView;

public class UpdateTeamFragment extends SuperFragment {

    private LinearLayout spinnerViewLayout, contentViewLayout;
    private Spinner teamNameSpinner;
    private AppCompatButton updateTeamBtn;
    private CircularImageView teamLogoCiv;
    private EditText teamNameEd, teamMottoEd;
    private ImageButton loadTeamImageBtn;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.update_team_fragment, container, false));
    }

    private View initView(View view) {

        spinnerViewLayout = view.findViewById(R.id.team_spinner_container);
        contentViewLayout = view.findViewById(R.id.team_update_content);
        teamNameSpinner = view.findViewById(R.id.team_spinner);
        teamLogoCiv = view.findViewById(R.id.team_logo);
        loadTeamImageBtn = view.findViewById(R.id.load_team_image);
        teamNameEd = view.findViewById(R.id.team_name);
        teamMottoEd = view.findViewById(R.id.team_motto);
        updateTeamBtn = view.findViewById(R.id.update_team_btn);

        View.OnClickListener clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doFunctionsForClick(v);
            }
        };

        updateTeamBtn.setOnClickListener(clickListener);
        loadTeamImageBtn.setOnClickListener(clickListener);

        return view;
    }

    private void doFunctionsForClick(View v) {
        switch (v.getId()){
            case R.id.update_team_btn:
                showToast("Function not set");
                break;
            case R.id.load_team_image:
                showToast("Function not set");
                break;
        }
    }
}
