package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appDialogs.AppDialog;

public class SettingsFragment extends SuperFragment {

    private RelativeLayout faqLayout, termsAndCondsLayout, privacyLayout;
    private Switch nightModeSwitch;
    private TextView appVersionTv, rateUsTv, reportIssueTv;

    private AppDialog appDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        appDialog = new AppDialog(getContext());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_filter_search);
        searchMenuItem.setVisible(false);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.fragment_settings, container, false));
    }

    private View initView(View view) {

        faqLayout = view.findViewById(R.id.faq);
        privacyLayout = view.findViewById(R.id.privacy);
        termsAndCondsLayout = view.findViewById(R.id.terms);
        appVersionTv = view.findViewById(R.id.version_value);
        rateUsTv = view.findViewById(R.id.rate_app);
        reportIssueTv = view.findViewById(R.id.report_issue);

        nightModeSwitch = view.findViewById(R.id.night_mode_toggle);
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                ((MainActivity) Objects.requireNonNull(getActivity())).changeTheme(isChecked);
                showToast("Function not set");
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFunctionsForClick(v);
            }
        };

        faqLayout.setOnClickListener(clickListener);
        privacyLayout.setOnClickListener(clickListener);
        termsAndCondsLayout.setOnClickListener(clickListener);
        reportIssueTv.setOnClickListener(clickListener);
        rateUsTv.setOnClickListener(clickListener);

        return view;
    }

    private void doFunctionsForClick(View view) {
        switch (view.getId()) {
            case R.id.faq:
                showToast("Faq clicked");
                break;
            case R.id.privacy:
                if (appDialog != null && !appDialog.isShowing() && !isRemoving()) {
                    appDialog.loadUrl(Keys.URL_PRIVACY);
                }
                break;
            case R.id.terms:
                if (appDialog != null && !appDialog.isShowing() && !isRemoving()) {
                    appDialog.loadUrl(Keys.URL_TERMS);
                }
                break;
            case R.id.rate_app:
                showToast("Rate us clicked");
                break;
            case R.id.report_issue:
                showToast("Report Superclicked");
                break;

        }
    }

}
