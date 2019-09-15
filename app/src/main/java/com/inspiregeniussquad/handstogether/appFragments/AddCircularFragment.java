package com.inspiregeniussquad.handstogether.appFragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    private ImageView circularIv, pdfIv;
    private ImageButton loadCircularImgBtn, loadPdfFileBtn;
    private EditText titleEd, descEd;

    private String title, description;
    private Uri circularImgUri = null, uploadedUri = null;

    private static final int CHOOSE_FILE = 101;
    private static final int PDF_REQ = 102;

    private Uri pdfUri = null;

    private RadioGroup uploadRdGrp;
    private RadioButton pdfRb, imgRb;

    private int toUpload = -1;
    private Uri toUploadUri = null;

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
        loadPdfFileBtn = view.findViewById(R.id.load_pdf_file);
        pdfIv = view.findViewById(R.id.pdf_file);
        uploadRdGrp = view.findViewById(R.id.upload_grp);
        imgRb = view.findViewById(R.id.img_rb);
        pdfRb = view.findViewById(R.id.pdf_rb);

        uploadRdGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.img_rb:
                        toUpload = 0;
                        break;
                    case R.id.pdf_rb:
                        toUpload = 1;
                        break;
                }
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFunctionsForClick(v);
            }
        };

        loadCircularImgBtn.setOnClickListener(clickListener);
        publishCircularBtn.setOnClickListener(clickListener);
        loadPdfFileBtn.setOnClickListener(clickListener);

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
            case R.id.load_pdf_file:
                openToLoadPdf();
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

    private void openToLoadPdf() {
        if (dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED)) {
            pdfIntent();
        } else {
            showToast(getString(R.string.need_storage_permission));
        }
    }

    private void pdfIntent() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PDF_REQ);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_FILE:
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
                    break;
                case PDF_REQ:
                    if (data != null) {
                        pdfUri = data.getData();
                        pdfIv.setImageResource(R.drawable.pdf_icon);
                    } else {
                        AppHelper.print("Data null onActivityResult");
                        showToast("Please select a file...");
                    }
                    break;
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

        if (toUpload == -1) {
            showToast(getString(R.string.choose_upload_type));
            return false;
        }

        if (toUpload == 1 && pdfUri == null) {
            showToast(getString(R.string.choose_pdf));
            return false;
        }

        if (toUpload == 0 && circularImgUri == null) {
            showToast(getString(R.string.choose_circular_image));
            return false;
        }

        return true;
    }

    private StorageReference storageRef;

    private void uploadCircularImage() {
        String circularImageName = title + "_Circular";

        switch (toUpload) {
            case 0: //image
                storageRef = storageReference.child("Circulars/images/" + circularImageName);
                toUploadUri = circularImgUri;
                break;
            case 1: //pdf
                storageRef = storageReference.child("Circulars/pdf/" + circularImageName);
                toUploadUri = pdfUri;
                break;
        }

        uploadTask = storageRef.putFile(toUploadUri);

        showProgress(getString(R.string.uploading_data));

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
                    uploadedUri = task.getResult();
                    AppHelper.print("Upload " + uploadedUri);
                    cancelProgress();

                    if (uploadedUri != null) {
                        String photoStringLink = uploadedUri.toString();
                        AppHelper.print("Uploaded Uri " + photoStringLink);

                        onImageUploaded(uploadedUri);
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

    private void onImageUploaded(Uri circularImgUri) {
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

    private CircularDataItems getCircularItems(Uri circularImgUri) {

        String pDate = AppHelper.getTodaysDate();
        String pTime = AppHelper.getCurrentTime();

        CircularDataItems circularDataItems = new CircularDataItems();

        circularDataItems.setcTitle(title);
        circularDataItems.setcDesc(description);
        if(toUpload == 0) {
            circularDataItems.setCircularImgPath(circularImgUri.toString());
        } else if (toUpload == 1) {
            circularDataItems.setPdfPath(circularImgUri.toString());
        }
        circularDataItems.setpDate(pDate);
        circularDataItems.setpTime(pTime);
        circularDataItems.setPostedBy(dataStorage.getString(Keys.MOBILE));

        return circularDataItems;
    }

    private void showUpdatedCirularFeed() {
        clearViews();
    }

    private void clearViews() {
        titleEd.setText("");
        descEd.setText("");
        circularIv.setImageResource(0);
        pdfUri = null;
        circularImgUri = null;
        pdfIv.setImageResource(0);
    }

}
