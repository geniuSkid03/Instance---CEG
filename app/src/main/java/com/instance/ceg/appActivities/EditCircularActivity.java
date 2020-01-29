package com.instance.ceg.appActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.instance.ceg.R;
import com.instance.ceg.appData.CircularDataItems;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appHelpers.DbHelper;
import com.instance.ceg.appUtils.AppHelper;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class EditCircularActivity extends SuperCompatActivity {

    private AppCompatButton publishCircularBtn;
    private ImageView circularIv, pdfIv;
    private ImageButton loadCircularImgBtn, loadPdfFileBtn;
    private EditText titleEd, descEd;

    private String title, description, cId, circularImgPath, pdfPath;
    private Uri circularImgUri = null, uploadedUri = null;

    private static final int CHOOSE_FILE = 101;
    private static final int PDF_REQ = 102;

    private Uri pdfUri = null;

    private RadioGroup uploadRdGrp;
    private RadioButton pdfRb, imgRb;

    private int toUpload = -1;
    private Uri toUploadUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_circular);

        publishCircularBtn = findViewById(R.id.publish_circular);
        circularIv = findViewById(R.id.circular_image);
        loadCircularImgBtn = findViewById(R.id.load_circular_image);
        titleEd = findViewById(R.id.circular_name);
        descEd = findViewById(R.id.circular_desc);
        loadPdfFileBtn = findViewById(R.id.load_pdf_file);
        pdfIv = findViewById(R.id.pdf_file);
        uploadRdGrp = findViewById(R.id.upload_grp);
        imgRb = findViewById(R.id.img_rb);
        pdfRb = findViewById(R.id.pdf_rb);

        uploadRdGrp.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.img_rb:
                    toUpload = 0;
                    break;
                case R.id.pdf_rb:
                    toUpload = 1;
                    break;
            }
        });

        View.OnClickListener clickListener = this::doFunctionsForClick;

        loadCircularImgBtn.setOnClickListener(clickListener);
        publishCircularBtn.setOnClickListener(clickListener);
        loadPdfFileBtn.setOnClickListener(clickListener);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String bundleStr = bundle.getString("circular_items");
            if (!TextUtils.isEmpty(bundleStr)) {
                CircularDataItems circularDataItems = new Gson().fromJson(bundleStr, CircularDataItems.class);
                updateUi(circularDataItems);
            }
        }
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

    private boolean isAllDetailsAvailable() {
        title = titleEd.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            showToast(this, getString(R.string.enter_title_circular));
            return false;
        }

        description = descEd.getText().toString().trim();
        if (TextUtils.isEmpty(description)) {
            showToast(this, getString(R.string.enter_circular_desc));
            return false;
        }

        if (isAttachmentEdited) {
            if (toUpload == -1) {
                showToast(this, getString(R.string.choose_upload_type));
                return false;
            }

            if (toUpload == 1 && pdfUri == null) {
                showToast(this, getString(R.string.choose_pdf));
                return false;
            }

            if (toUpload == 0 && circularImgUri == null) {
                showToast(this, getString(R.string.choose_circular_image));
                return false;
            }
        }

        return true;
    }

    private void openGallery() {
        if (dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED)) {
            galleryIntent();
        } else {
            showToast(this, getString(R.string.permissoin_denied_string));
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
            showToast(this, getString(R.string.need_storage_permission));
        }
    }

    private void pdfIntent() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PDF_REQ);
    }

    private boolean isAttachmentEdited = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_FILE:
                    circularImgUri = data.getData();
                    isAttachmentEdited = true;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditCircularActivity.this.getContentResolver(), circularImgUri);
                        if (bitmap != null) {
                            Glide.with(EditCircularActivity.this).load(circularImgUri).into(circularIv);
                        } else {
                            AppHelper.showToast(EditCircularActivity.this, "Image bitmap null");
                        }
                    } catch (Exception e) {
                        AppHelper.showToast(EditCircularActivity.this, "Exception in parsing image");
                    }
                    break;
                case PDF_REQ:
                    if (data != null) {
                        isAttachmentEdited = true;
                        pdfUri = data.getData();
                        pdfIv.setImageResource(R.drawable.pdf_icon);
                    } else {
                        AppHelper.print("Data null onActivityResult");
                        showToast(EditCircularActivity.this, "Please select a file...");
                    }
                    break;
            }
        }
    }

    private void updateUi(CircularDataItems circularDataItems) {
        cId = circularDataItems.getcId();

        if (!TextUtils.isEmpty(circularDataItems.getcTitle())) {
            title = circularDataItems.getcTitle();
            titleEd.setText(title);
        }

        if (!TextUtils.isEmpty(circularDataItems.getcDesc())) {
            description = circularDataItems.getcDesc();
            descEd.setText(description);
        }

        if (circularDataItems.getCircularImgPath() != null) {
            circularImgPath = circularDataItems.getCircularImgPath();
            Glide.with(EditCircularActivity.this).load(circularImgPath).into(circularIv);
            imgRb.setChecked(true);
        }

        if (circularDataItems.getPdfPath() != null) {
            pdfPath = circularDataItems.getPdfPath();
            pdfIv.setImageResource(R.drawable.pdf_icon);
            pdfRb.setChecked(true);
        }
    }

    private StorageReference storageRef;

    private void uploadCircularImage() {
        if (!isAttachmentEdited) {
            if (!TextUtils.isEmpty(circularImgPath)) {
                onImageUploaded(circularImgPath);
            } else if (!TextUtils.isEmpty(pdfPath)) {
                onImageUploaded(pdfPath);
            } else {
                new SuperCompatActivity().showSimpleAlert("Upload an image or pdf to continue", "ok", new SimpleAlert() {
                    @Override
                    public void onBtnClicked(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
            }
        } else {
            showProgress(getString(R.string.uploading_data));

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

            //todo delete the old file and add new file in storage

            uploadTask = storageRef.putFile(toUploadUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    showInfoAlert(getString(R.string.upload_failed));
                    AppHelper.print("Task unsuccessful!");
                    throw task.getException();
                }
                cancelProgress();

                return storageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    uploadedUri = task.getResult();
                    AppHelper.print("Upload " + uploadedUri);
                    cancelProgress();

                    if (uploadedUri != null) {
                        String photoStringLink = uploadedUri.toString();
                        AppHelper.print("Uploaded Uri " + photoStringLink);

                        onImageUploaded(uploadedUri.toString());
                    } else {
                        cancelProgress();
                        AppHelper.print("Image uploaded but uri null");
                    }
                } else {
                    cancelProgress();
                    showInfoAlert(getString(R.string.upload_failed));
                }
            });
        }
    }

    private void onImageUploaded(String circularImgUri) {
        showProgress(getString(R.string.updating_circular));
        CircularDataItems circularDataItems = getCircularItems(circularImgUri);

        new DbHelper().updateCircular(circularDataItems, new DbHelper.UpdateCallback() {
            @Override
            public void onSuccess() {
                cancelProgress();
                showAlertDialog(true);
            }

            @Override
            public void onFailed() {
                cancelProgress();
                showAlertDialog(false);
            }
        });
    }

    private void showAlertDialog(boolean isSuccess) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(isSuccess ? "Circular updated successfully!" : "Some error occurred, try again later!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        alertDialog.setCancelable(false);
        if(!alertDialog.isShowing() && !isFinishing()) {
            alertDialog.show();
        }
    }

    private CircularDataItems getCircularItems(String circularImgUri) {

        String pDate = AppHelper.getTodaysDate();
        String pTime = AppHelper.getCurrentTime();

        CircularDataItems circularDataItems = new CircularDataItems();

        circularDataItems.setcId(cId);
        circularDataItems.setcTitle(title);
        circularDataItems.setcDesc(description);
        if (isAttachmentEdited) {
            if (toUpload == 0) {
                circularDataItems.setCircularImgPath(circularImgUri.toString());
            } else if (toUpload == 1) {
                circularDataItems.setPdfPath(circularImgUri.toString());
            }
        } else {
            if (!TextUtils.isEmpty(circularImgPath)) {
                circularDataItems.setCircularImgPath(circularImgPath);
            }
            if (!TextUtils.isEmpty(pdfPath)) {
                circularDataItems.setPdfPath(pdfPath);
            }
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
