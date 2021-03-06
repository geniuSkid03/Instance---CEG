package com.instance.ceg.appDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.instance.ceg.R;
import com.instance.ceg.appData.Clubs;
import com.instance.ceg.appData.DataStorage;
import com.instance.ceg.appData.Keys;

import java.util.Objects;

public class AddClubDialog extends Dialog {

    private ImageView clubImageIv, imgChooserIv;
    private FloatingActionButton okBtn;
    private EditText clubNameEd;

    private String clubName = "", clubImgUrl = "";

    private AddClubListener addClubListener;
    private DataStorage dataStorage;

    private Activity activity;

    public AddClubDialog(final Activity activity, final DataStorage dataStorage, final AddClubListener addClubListener) {
        super(activity.getApplicationContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        this.activity = activity;
        this.dataStorage = dataStorage;
        this.addClubListener = addClubListener;

        final View addAdminView = LayoutInflater.from(activity).inflate(R.layout.dialog_add_clubs, null);

        clubNameEd = addAdminView.findViewById(R.id.club_name);
        clubImageIv = addAdminView.findViewById(R.id.club_image);
        imgChooserIv = addAdminView.findViewById(R.id.club_image_choose);
        okBtn = addAdminView.findViewById(R.id.save_club);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.club_image_choose:
                        if (dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED)) {
                            openGallery();
                        } else {
                            showToast(activity.getString(R.string.permissoin_denied_string));
                        }
                        break;
                    case R.id.save_club:
                        clubName = clubNameEd.getText().toString().trim();

                        if (TextUtils.isEmpty(clubName)) {
                            showToast("Enter Club's name");
                            return;
                        }

                        if (TextUtils.isEmpty(clubImgUrl)) {
                            showToast("Choose Club's representation image");
                            break;
                        }

                        addClubListener.onOkClicked(new Clubs(clubName, clubImgUrl));
                        dismiss();

                        break;
                }
            }
        };

        imgChooserIv.setOnClickListener(clickListener);
        okBtn.setOnClickListener(clickListener);

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    public void setImageUrl(String path) {
        if (path != null && clubImageIv != null) {
            clubImgUrl = path;
            Glide.with(activity).load(path).into(clubImageIv);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public void clearViews() {
        clubNameEd.setText("");
        clubImageIv.setImageResource(0);
        clubImgUrl = "";
        clubName = "";
    }

    public interface AddClubListener {
        void onOkClicked(Clubs clubs);
    }
}
