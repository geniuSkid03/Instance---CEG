package com.instance.ceg.appFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.instance.ceg.R;
import com.instance.ceg.appActivities.MainActivity;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appDialogs.AppDialog;

import java.util.Objects;

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
        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ((MainActivity) Objects.requireNonNull(getActivity())).changeTheme(isChecked);
            showToast("Function not set");
        });

        View.OnClickListener clickListener = v -> doFunctionsForClick(v);

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
                Uri uri = Uri.parse("market://details?id="+ Objects.requireNonNull(getContext()).getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                try{
                    startActivity(goToMarket);
                }catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+getContext().getPackageName())));
                }
                break;
            case R.id.report_issue:
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("text/plain");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"spartanzceg@gmail.com"});
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Error in INSTANCE application");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "Describe the way you faced problem");
                startActivity(Intent.createChooser(mailIntent, "Send email via..."));
                break;

        }
    }

}
