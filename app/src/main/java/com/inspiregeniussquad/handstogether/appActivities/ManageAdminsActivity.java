package com.inspiregeniussquad.handstogether.appActivities;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.AdminsAdapter;
import com.inspiregeniussquad.handstogether.appData.Admin;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appDialogs.AddAdminDialog;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

public class ManageAdminsActivity extends SuperCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.admins_list)
    ListView adminsLv;

    @BindView(R.id.no_admins)
    TextView noAdminsTv;

    @BindView(R.id.add_admin_fab)
    FloatingActionButton addAdminFab;

    private AddAdminDialog addAdminDialog;
    private AdminsAdapter adminsAdapter;
    private ArrayList<Admin> adminArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_admins);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        adminArrayList = new ArrayList<>();
        adminsAdapter = new AdminsAdapter(this, adminArrayList, new AdminsAdapter.OnOptionsSelected() {
            @Override
            public void onClicked(int position, View view) {
                showOptions(position, view);
            }
        });
        adminsLv.setAdapter(adminsAdapter);

        addAdminDialog = new AddAdminDialog(this, new AddAdminDialog.AdminListener() {
            @Override
            public void onOkClicked(Admin admin) {
                if (admin != null) {
                    insertAdminToDb(admin);
                } else {
                    showToast(ManageAdminsActivity.this, getString(R.string.admin_cannot_be_added));
                }
            }
        });

        addAdminFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addAdminDialog != null) {
                    if (!addAdminDialog.isShowing() && !isFinishing()) {
                        addAdminDialog.clearValues();
                        addAdminDialog.prepareUi(dataStorage.getString(Keys.ADMIN_VALUE));
                        addAdminDialog.show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showOptions(final int position, View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                   /* case R.id.edit:
                        proceedToEdit(adminArrayList.get(position));
                        break;*/
                    case R.id.remove:
                        askAndRemove(position);
                        break;
                }
                return true;
            }
        });
        menu.inflate(R.menu.admin_options_menu);
        menu.show();
    }

    private void askAndRemove(final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Are you sure you want to remove?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                proceedToRemove(adminArrayList.get(position));
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if(!alertDialog.isShowing() && !isFinishing()) alertDialog.show();
    }

    private void proceedToRemove(Admin admin) {
        showProgress(getString(R.string.processing));

        String adminMobile = admin.getMobile();
        Query deleteQuery = adminDbReference.orderByChild("mobile").equalTo(adminMobile);
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot deleteSnapshot : dataSnapshot.getChildren()) {
                    deleteSnapshot.getRef().removeValue();
                }
                showToast(ManageAdminsActivity.this, getString(R.string.admin_removed));
                refreshList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cancelProgress();
                AppHelper.print("Db error while trying to delete: " + databaseError.getMessage());
                showToast(ManageAdminsActivity.this, getString(R.string.some_error_occurred));
            }
        });
    }

    /*private void proceedToEdit(Admin admin) {

    }*/

    private void insertAdminToDb(Admin admin) {
        showProgress(getString(R.string.adding_admin));

        adminDbReference.child(admin.getMobile()).setValue(admin,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        cancelProgress();

                        if (databaseError == null) {
                            showSimpleAlert(getString(R.string.admin_added_successfully), getString(R.string.ok), new SimpleAlert() {
                                @Override
                                public void onBtnClicked(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                    refreshList();
                                }
                            });
                        } else {
                            AppHelper.print("Database error: " + databaseError.getMessage());

                            showSimpleAlert(getString(R.string.db_error), getString(R.string.ok), new SimpleAlert() {
                                @Override
                                public void onBtnClicked(DialogInterface dialogInterface, int which) {
                                    refreshList();
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                    }
                });
    }

    private void refreshList() {
        showProgress(getString(R.string.loading));

        adminDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("Admins exists and trying to retrive!");
                    retriveDataFromDb(dataSnapshot);
                } else {
                    AppHelper.print("No admins found!");
                    cancelProgress();
                    updateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cancelProgress();
                updateUi();
                AppHelper.print("Db Error while loading news feed: " + databaseError);
                showToast(ManageAdminsActivity.this, getString(R.string.db_error));
            }
        });
    }

    private void retriveDataFromDb(DataSnapshot dataSnapshot) {
        cancelProgress();

        Map<String, Admin> newsFeedItemsMap = (Map<String, Admin>) dataSnapshot.getValue();

        adminArrayList.clear();

        for (Map.Entry<String, Admin> teamEntry : newsFeedItemsMap.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            String position = (String) map.get("position");

            Admin admin = null;

            assert position != null;
            switch (position) {
                case "0":
                    admin = new Admin((String) map.get("name"), (String) map.get("mobile"), Keys.GENIUS_ADMIN);
                    break;
                case "1":
                    admin = new Admin((String) map.get("name"), (String) map.get("mobile"), Keys.SUPER_ADMIN);
                    break;
                case "2":
                    admin = new Admin((String) map.get("name"), (String) map.get("mobile"), Keys.NEWS_ADMIN);
                    break;
                case "3":
                    admin = new Admin((String) map.get("name"), (String) map.get("mobile"), Keys.NEWS_MANAGER);
                    break;
                case "4":
                    admin = new Admin((String) map.get("name"), (String) map.get("mobile"), Keys.CIRCULAR_ADMIN);
                    break;
                case "5":
                    admin = new Admin((String) map.get("name"), (String) map.get("mobile"), Keys.CIRCULAR_MANAGER);
                    break;
                case "6":
                    admin = new Admin((String) map.get("name"), (String) map.get("mobile"), Keys.EDITOR);
                    break;
            }

            adminArrayList.add(admin);
        }

        if(adminArrayList.size() == 0) {
            updateUi();
            return;
        }

        adminsAdapter.notifyDataSetChanged();
        adminsLv.setAdapter(adminsAdapter);

        updateUi();
    }

    private void updateUi() {
        adminsLv.setVisibility(adminArrayList.size() == 0 ? View.GONE : View.VISIBLE);
        noAdminsTv.setVisibility(adminArrayList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
