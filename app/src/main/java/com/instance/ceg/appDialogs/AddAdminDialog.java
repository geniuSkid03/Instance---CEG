package com.instance.ceg.appDialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.instance.ceg.R;
import com.instance.ceg.appData.Admin;
import com.instance.ceg.appData.Keys;

import java.util.Objects;

public class AddAdminDialog extends Dialog {

    private EditText adminNameEd, adminMobileEd;
    private RadioGroup desgntRgrp;
    private RadioButton newsAdminRb, superAdminRb, circularAdminRb;
    private RadioButton newsEditorRb, circularEditorRb, geniuSbtn;
    private FloatingActionButton saveFab;

    private AdminListener adminListener;

    private String designation = "";
    private int position = -1;


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
        circularAdminRb = addAdminView.findViewById(R.id.circular_admin_rb);
        newsEditorRb = addAdminView.findViewById(R.id.news_editor_rb);
        circularEditorRb = addAdminView.findViewById(R.id.circular_editor_rb);
        geniuSbtn = addAdminView.findViewById(R.id.genius_admin_rb);

        adminMobileEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        saveFab = addAdminView.findViewById(R.id.save_admin);

        desgntRgrp.clearCheck();
        desgntRgrp.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId) {
                case R.id.genius_admin_rb:
                    position = 0;
                    designation = "geniuS Admin";
                    break;
                case R.id.super_admin_rb:
                    position = 1;
                    designation = "Super Admin";
                    break;
                case R.id.news_admin_rb:
                    position = 2;
                    designation = "NewsFeed Admin";
                    break;
                case R.id.news_editor_rb:
                    position = 3;
                    designation = "NewsFeed Editor";
                    break;
                case R.id.circular_admin_rb:
                    position = 4;
                    designation = "Circular Admin";
                    break;
                case R.id.circular_editor_rb:
                    position = 5;
                    designation = "Circular Editor";
                    break;
            }
        });

        saveFab.setOnClickListener(view -> {
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

            if (position == -1) {
                Toast.makeText(context, context.getString(R.string.choose_position), Toast.LENGTH_SHORT).show();
                return;
            }

            adminListener.onOkClicked(new Admin(adminName, adminMobile, String.valueOf(position), designation));
            dismiss();
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(addAdminView, layoutParams);

        setCancelable(true);
    }

    public void clearValues() {
        adminNameEd.setText("");
        adminMobileEd.setText("");
        desgntRgrp.clearCheck();
        position = -1;
        designation = "";
    }

    public void prepareUi(String adminType) {
        switch (adminType) {
            case Keys.CIRCULAR_ADMIN:
                superAdminRb.setVisibility(View.GONE);
                circularAdminRb.setVisibility(View.VISIBLE);
                newsAdminRb.setVisibility(View.GONE);
                newsEditorRb.setVisibility(View.GONE);
                circularEditorRb.setVisibility(View.VISIBLE);
                break;
            case Keys.NEWS_ADMIN:
                superAdminRb.setVisibility(View.GONE);
                circularAdminRb.setVisibility(View.GONE);
                circularEditorRb.setVisibility(View.GONE);
                newsAdminRb.setVisibility(View.VISIBLE);
                newsEditorRb.setVisibility(View.VISIBLE);
                break;
            case Keys.GENIUS_ADMIN:
            case Keys.SUPER_ADMIN:
                superAdminRb.setVisibility(View.VISIBLE);
                circularAdminRb.setVisibility(View.VISIBLE);
                circularEditorRb.setVisibility(View.VISIBLE);
                newsAdminRb.setVisibility(View.VISIBLE);
                newsEditorRb.setVisibility(View.VISIBLE);
                break;
        }
    }

    public interface AdminListener {
        void onOkClicked(Admin admin);
    }

}
