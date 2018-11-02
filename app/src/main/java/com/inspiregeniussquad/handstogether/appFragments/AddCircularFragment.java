package com.inspiregeniussquad.handstogether.appFragments;

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

import com.inspiregeniussquad.handstogether.R;

public class AddCircularFragment extends SuperFragment {

    private AppCompatButton publishCircularBtn;
    private RadioButton showBtn, hideBtn;
    private RadioGroup circularRgrp;
    private ImageView circularIv;
    private ImageButton loadCircularImgBtn;
    private EditText titleEd, descEd;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.add_circular_fragment, container, false));
    }

    private View initView(View view) {

        publishCircularBtn = view.findViewById(R.id.publish_circular);
        showBtn = view.findViewById(R.id.visible_circular);
        hideBtn = view.findViewById(R.id.invisible_circular);
        circularRgrp = view.findViewById(R.id.circular_visible_grp);
        circularIv = view.findViewById(R.id.circular_image);
        loadCircularImgBtn = view.findViewById(R.id.load_circular);
        titleEd = view.findViewById(R.id.circular_name);
        descEd = view.findViewById(R.id.circular_desc);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFunctionsForClick(v);
            }
        };

        loadCircularImgBtn.setOnClickListener(clickListener);
        publishCircularBtn.setOnClickListener(clickListener);


        return view;
    }

    private void doFunctionsForClick(View v) {
        switch (v.getId()) {
            case R.id.load_circular:
                break;
            case R.id.publish_circular:
                break;
        }
    }

}
