package com.inspiregeniussquad.handstogether.appDialogs;

import android.app.Dialog;
import android.content.Context;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Admin;
import com.inspiregeniussquad.handstogether.appData.Keys;

import java.util.Objects;

public class AddAdminDialog extends Dialog {

    private EditText adminNameEd, adminMobileEd;
    private RadioGroup desgntRgrp;
    private RadioButton newsAdminRb, superAdminRb, editorRb, circularAdminRb, circularManagerRb, newsManagerRb;
    private FloatingActionButton saveFab;

    private AdminListener adminListener;

    private int designation = -1;


    public AddAdminDialog(final Context context, final AdminListener adminListener) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        this.adminListener = adminListener;

        View addAdminView = LayoutInflater.from(context).inflate(R.layout.dialog_add_admin, null);

        adminNameEd = addAdminView.findViewById(R.id.admin_name);
        adminMobileEd = addAdminView.findViewById(R.id.admin_mobile);
        desgntRgrp = addAdminView.findViewById(R.id.admin_desg_grp);
        newsAdminRb = addAdminView.findViewById(R.id.news_admin_rb);
        superAdminRb = addAdminView.findViewById(R.id.super_admin_rb);
        editorRb = addAdminView.findViewById(R.id.editor_rb);
        circularAdminRb = addAdminView.findViewById(R.id.circular_admin_rb);
        circularManagerRb = addAdminView.findViewById(R.id.circular_admin_rb_2);
        newsManagerRb = addAdminView.findViewById(R.id.news_admin_rb_2);

        saveFab = addAdminView.findViewById(R.id.save_admin);

        desgntRgrp.clearCheck();
        desgntRgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.super_admin_rb:
                        designation = 1;
                        break;
                    case R.id.news_admin_rb:
                        designation = 2;
                        break;
                    case R.id.news_admin_rb_2:
                        designation = 3;
                        break;
                    case R.id.circular_admin_rb:
                        designation = 4;
                        break;
                    case R.id.circular_admin_rb_2:
                        designation = 5;
                        break;
                    case R.id.editor_rb:
                        designation = 6;
                        break;
                }
            }
        });

        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adminName = adminNameEd.getText().toString().trim();
                String adminMobile = adminMobileEd.getText().toString().trim();

                if (TextUtils.isEmpty(adminMobile)) {
                    adminMobileEd.setError(context.getString(R.string.enter_mobile));
                    return;
                }

                if (TextUtils.isEmpty(adminName)) {
                    adminNameEd.setError(context.getString(R.string.enter_name));
                    return;
                }

                if (designation == -1) {
                    Toast.makeText(context, context.getString(R.string.choose_position), Toast.LENGTH_SHORT).show();
                    return;
                }

                adminListener.onOkClicked(new Admin(adminName, adminMobile, String.valueOf(designation)));
                dismiss();
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(addAdminView, layoutParams);

        setCancelable(true);
    }

    public void clearValues() {
        adminNameEd.setText("");
        adminMobileEd.setText("");
        desgntRgrp.clearCheck();
        designation = -1;
    }

    public void prepareUi(String adminType) {
        switch (adminType) {
            case Keys.SUPER_ADMIN:
                superAdminRb.setVisibility(View.VISIBLE);
                editorRb.setVisibility(View.VISIBLE);
                circularAdminRb.setVisibility(View.VISIBLE);
                newsAdminRb.setVisibility(View.VISIBLE);
                newsManagerRb.setVisibility(View.VISIBLE);
                circularManagerRb.setVisibility(View.VISIBLE);
                break;
            case Keys.CIRCULAR_ADMIN:
                superAdminRb.setVisibility(View.GONE);
                editorRb.setVisibility(View.GONE);
                circularAdminRb.setVisibility(View.VISIBLE);
                newsAdminRb.setVisibility(View.GONE);
                newsManagerRb.setVisibility(View.GONE);
                circularManagerRb.setVisibility(View.VISIBLE);
                break;
            case Keys.NEWS_ADMIN:
                newsManagerRb.setVisibility(View.VISIBLE);
                circularManagerRb.setVisibility(View.GONE);
                superAdminRb.setVisibility(View.GONE);
                editorRb.setVisibility(View.VISIBLE);
                circularAdminRb.setVisibility(View.GONE);
                newsAdminRb.setVisibility(View.VISIBLE);
                break;
            case Keys.GENIUS_ADMIN:
                superAdminRb.setVisibility(View.VISIBLE);
                editorRb.setVisibility(View.VISIBLE);
                circularAdminRb.setVisibility(View.VISIBLE);
                newsAdminRb.setVisibility(View.VISIBLE);
                newsManagerRb.setVisibility(View.VISIBLE);
                circularManagerRb.setVisibility(View.VISIBLE);
                break;
        }
    }

    public interface AdminListener {
        void onOkClicked(Admin admin);
    }

}
