package com.inspiregeniussquad.handstogether.appFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.DataStorage;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appUtils.PermissionHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SuperFragment extends Fragment {

    protected Gson gson = new Gson();
    private Unbinder unbinder;

    protected DataStorage dataStorage;
    protected PermissionHelper permissionHelper;

    protected FirebaseStorage firebaseStorage;
    protected StorageReference storageReference;

    protected StorageReference teamLogoStorageReference;
    protected UploadTask uploadTask;
    protected DatabaseReference teamDbReference, newsDbReference, circularDbReference;

    protected ProgressDialog progressDialog;
    protected AlertDialog simpleAlertDialog, infoAlert;


    //here fragment is attached with activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //used for initializing fragments
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //instances and references for fire base database
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        teamLogoStorageReference = firebaseStorage.getReference();

        //node and child references
        teamDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_TEAM);
        newsDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_NEWSFEED);
        circularDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_CIRCULAR);

        dataStorage = new DataStorage(getActivity());
        permissionHelper = new PermissionHelper(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        simpleAlertDialog = new AlertDialog.Builder(getActivity()).create();
        infoAlert = new AlertDialog.Builder(getActivity()).create();

    }

    //view creation and views are inflated here
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(view);
        return view;
    }

    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    //view creation completed for fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //this was invoked after onCreate()
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //this makes the fragment visible
    @Override
    public void onStart() {
        super.onStart();
    }

    //this makes the fragment interactive with user
    @Override
    public void onResume() {
        super.onResume();
    }

    //the fragment is now no longer interactive
    @Override
    public void onPause() {
        super.onPause();
    }

    //the fragment is no longer visible to user
    @Override
    public void onStop() {
        super.onStop();
    }

    //fragment was removed and view cleared
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //removing binding on view destroyed
        unbinder.unbind();
    }

    //fragment resources were cleared
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //it was called when the fragment was no longer attached to the activity
    @Override
    public void onDetach() {
        super.onDetach();
    }

    //for starting activity with single intent values
    protected void goTo(Context from, Class to, boolean close, String key, String data) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null) {
                startActivity(new Intent(from, to).putExtra(key, data));
            }
            if (close) {
                getActivity().finish();
            }
        }

    }

    //for starting activity with two intent values
    protected void goTo(Context from, Class to, boolean close, String key, String data, String key1, String data1) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null && key1 != null && data1 != null) {
                startActivity(new Intent(from, to).putExtra(key, data).putExtra(key1, data1));
            }
            if (close) {
                getActivity().finish();
            }
        }

    }

    //for starting activity without intent values
    protected void goTo(Context from, Class to, boolean close) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            startActivity(new Intent(from, to));
            if (close) {
                getActivity().finish();
            }
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showProgress(String msg) {
        if (msg != null) {
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    protected void cancelProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void showInfoAlert(String msg) {
        if (infoAlert != null) {
            if (msg != null) {
                infoAlert.setMessage(msg);
                infoAlert.setButton(android.app.AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                infoAlert.setCancelable(false);
                if (!getActivity().isFinishing()) {
                    infoAlert.show();
                }
            }
        }
    }

    protected void showSimpleAlert(String msg, String btnText, final SimpleAlert simpleAlert) {
        if(simpleAlertDialog != null) {
            simpleAlertDialog.setMessage(msg);
            simpleAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, btnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    simpleAlert.onBtnClicked(dialog, which);
                }
            });
            simpleAlertDialog.show();
        }
    }

    interface SimpleAlert {
        void onBtnClicked(DialogInterface dialogInterface, int which);
    }
}
