package com.inspiregeniussquad.handstogether.appFragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.CircularDataItems;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

public class AddCircularFragment extends SuperFragment {

    private AppCompatButton publishCircularBtn;
    private ImageView circularIv;
    private ImageButton loadCircularImgBtn;
    private EditText titleEd, descEd;

    private String title, description;
    private Uri circularImgUri, uploadedCircularImgUri;

    private static final int CHOOSE_FILE = 101;


    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.add_circular_fragment, container, false));
    }

    private View initView(View view) {

        publishCircularBtn = view.findViewById(R.id.publish_circular);
        circularIv = view.findViewById(R.id.circular_image);
        loadCircularImgBtn = view.findViewById(R.id.load_circular_image);
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
            case R.id.load_circular_image:
                openGallery();
                break;
            case R.id.publish_circular:
                if (isAllDetailsAvailable()) {
                    uploadCircularImage();
                }
                break;
        }
    }

    private void openGallery() {
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
                circularImgUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), circularImgUri);
                    if (bitmap != null) {
                        Glide.with(getContext()).load(circularImgUri).into(circularIv);
                    } else {
                        AppHelper.showToast(getActivity(), "Image bitmap null");
                    }
                } catch (Exception e) {
                    AppHelper.showToast(getActivity(), "Exception in parsing image");
                }

            }
        }
    }

    private boolean isAllDetailsAvailable() {
        title = titleEd.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            showToast(getString(R.string.enter_title_circular));
            return false;
        }

        description = descEd.getText().toString().trim();
        if (TextUtils.isEmpty(description)) {
            showToast(getString(R.string.enter_circular_desc));
            return false;
        }

        if (circularImgUri == null) {
            showToast(getString(R.string.enter_circular_image));
            return false;
        }

        return true;
    }

    private void uploadCircularImage() {
        String circularImageName = title + "_Circular";

        showProgress(getString(R.string.uploading_data));

        final StorageReference storageRef = storageReference.child("Circulars/" + circularImageName);

        uploadTask = storageRef.putFile(circularImgUri);

        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    showInfoAlert(getString(R.string.upload_failed));
                    AppHelper.print("Task unsuccessful!");
                    throw task.getException();
                }
                cancelProgress();

                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    uploadedCircularImgUri = task.getResult();
                    AppHelper.print("Upload " + uploadedCircularImgUri);
                    cancelProgress();

                    if (uploadedCircularImgUri != null) {
                        String photoStringLink = uploadedCircularImgUri.toString();
                        AppHelper.print("Uploaded Ciruclar Uri " + photoStringLink);

                        onImageUploaded(uploadedCircularImgUri);
                    } else {
                        cancelProgress();
                        AppHelper.print("Image uploaded but uri null");
                    }
                } else {
                    cancelProgress();
                    showInfoAlert(getString(R.string.upload_failed));
                }
            }
        });
    }

    private void onImageUploaded(Uri circularImgUri){
        showProgress(getString(R.string.updating_circular));

        CircularDataItems circularDataItems = getCircularItems(circularImgUri);

        circularDbReference.child(newsDbReference.push().getKey()).setValue(circularDataItems,
                new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                cancelProgress();

                if (databaseError == null) {
                    showSimpleAlert(getString(R.string.cirular_updated_success),
                            getString(R.string.ok), new SimpleAlert() {
                        @Override
                        public void onBtnClicked(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            showUpdatedCirularFeed();
                        }
                    });
                } else {
                    AppHelper.print("Database error: " + databaseError.getMessage());

                    showSimpleAlert(getString(R.string.db_error), getString(R.string.ok), new SimpleAlert() {
                        @Override
                        public void onBtnClicked(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            showUpdatedCirularFeed();
                        }
                    });
                }
            }
        });
    }

    private CircularDataItems getCircularItems(Uri circularImgUri){

        String pDate = AppHelper.getTodaysDate();
        String pTime = AppHelper.getCurrentTime();

        CircularDataItems circularDataItems = new CircularDataItems();

        circularDataItems.setcTitle(title);
        circularDataItems.setcDesc(description);
        circularDataItems.setCircularImgPath(circularImgUri.toString());
        circularDataItems.setpDate(pDate);
        circularDataItems.setpTime(pTime);
        circularDataItems.setPostedBy(dataStorage.getString(Keys.MOBILE));

        return circularDataItems;
    }

    private void showUpdatedCirularFeed(){
        clearViews();
    }

    private void clearViews() {
        titleEd.setText("");
        descEd.setText("");
        circularIv.setImageResource(0);
    }

}
