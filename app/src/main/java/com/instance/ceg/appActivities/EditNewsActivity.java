package com.instance.ceg.appActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.instance.ceg.R;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.NewsFeedItems;
import com.instance.ceg.appData.Team;
import com.instance.ceg.appHelpers.DbHelper;
import com.instance.ceg.appUtils.AppHelper;
import com.instance.ceg.appUtils.TeamDataHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

public class EditNewsActivity extends SuperCompatActivity {

    private Spinner teamNameSpinner;
    private EditText nameEd, descEd, venueEd, videoUrlEd;
    private TextView eventDateTv, eventTimeTv;
    private RadioGroup newsVisibilityGrp;
    private RadioButton showNewsRb, hideNewsRb;
    private ImageButton posterImgBtn;
    private ImageView posterIv;
    private AppCompatButton publishNewsBtn;

    private String nfId;
    private String teamName, eventDesc, eventName, venueName, videoUrl, eventDate, eventTime;
    private Uri eventPoster, uploadedPosterUri;
    private String eventPosterImgUrl;
    private boolean isNewsVisible, isFirstCompleted = false;
    private int teamId = -1;
    private String tName = "";
    private String tLogo = "";

    private TeamDataHelper teamDataHelper;

    private static final int CHOOSE_FILE = 101;

    private Calendar dateSelected = Calendar.getInstance();
    private Calendar timeSelected = Calendar.getInstance();

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private Toolbar toolbar;

    private boolean isImageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);

        initToolbar();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle(getString(R.string.update_news));
        }

        teamNameSpinner = findViewById(R.id.team_spinner);
        nameEd = findViewById(R.id.event_name);
        descEd = findViewById(R.id.event_desc);
        eventDateTv = findViewById(R.id.event_date);
        eventTimeTv = findViewById(R.id.event_time);
        venueEd = findViewById(R.id.event_venue);
        posterIv = findViewById(R.id.event_poster);
        videoUrlEd = findViewById(R.id.event_video_url);
        newsVisibilityGrp = findViewById(R.id.news_rgrb);
        showNewsRb = findViewById(R.id.visible_news);
        hideNewsRb = findViewById(R.id.invisible_news);
        posterImgBtn = findViewById(R.id.add_event_poster);

        publishNewsBtn = findViewById(R.id.publish_news);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFunctionsForClick(view);
            }
        };

        publishNewsBtn.setOnClickListener(clickListener);
        posterImgBtn.setOnClickListener(clickListener);
        eventDateTv.setOnClickListener(clickListener);
        eventTimeTv.setOnClickListener(clickListener);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String newsItemStr = bundle.getString(Keys.NEWS_ITEM);
            if (!TextUtils.isEmpty(newsItemStr)) {
                NewsFeedItems newsFeedItems = new Gson().fromJson(newsItemStr, NewsFeedItems.class);
                if (newsFeedItems != null) {
                    updateUi(newsFeedItems);
                } else {
                    AppHelper.showToast(this, "Cannot edit this news, delete and try creating new one!");
                    finish();
                }
            }
        }
    }

    private void doFunctionsForClick(View view) {
        switch (view.getId()) {
            case R.id.publish_news:
                if (isAllDetailsGiven()) {
                    updateNewsToDb();
                }
                break;
            case R.id.add_event_poster:
                openGallery();
                break;
            case R.id.event_date:
                showDatePickerDialog();
                break;
            case R.id.event_time:
                showTimePickerDialog();
                break;
        }
    }

    private void updateNewsToDb() {
        showProgress("Updating news, please wait...");

        new DbHelper().updateNews(getNewsFeedItems(), new DbHelper.UpdateCallback() {
            @Override
            public void onSuccess() {
                cancelProgress();
                showToast("News Updated successfully!");
            }

            @Override
            public void onFailed() {
                cancelProgress();
                showToast("News update failed, try again later!");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE) {
                eventPoster = data.getData();
                isImageChanged = true;
                try {
                    if (eventPoster != null) {
                        Glide.with(EditNewsActivity.this).load(eventPoster).into(posterIv);
                    } else {
                        AppHelper.showToast(EditNewsActivity.this, "Image null");
                    }
                } catch (Exception e) {
                    AppHelper.showToast(EditNewsActivity.this, "Exception in parsing image");
                }

            }
        }
    }

    private void loadTeamInfoInSpinner(String teamName) {
        showProgress(getString(R.string.loading_teams));

        teamDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    retriveTeamsData(dataSnapshot, teamName);
                } else {
                    cancelProgress();
                    AppHelper.print("Team doesn't exist!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cancelProgress();
                showToast(getString(R.string.db_error));
                AppHelper.print("Db Error: " + databaseError);
            }
        });
    }

    private ArrayList<Team> teamArrayList = new ArrayList<>();

    private void retriveTeamsData(DataSnapshot dataSnapshot, String teamName) {
        Map<String, Team> teamData = (Map<String, Team>) dataSnapshot.getValue();

        for (Map.Entry<String, Team> teamEntry : teamData.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            Team team = new Team();

            team.settName((String) map.get("tName"));
            team.settLogo((String) map.get("tLogo"));
            team.settId((String) map.get("tId"));

            AppHelper.print("Team data: " + team.gettName() + "\t" + team.gettLogo());

            teamArrayList.add(team);
        }

        if (teamArrayList.size() != 0) {
            loadWithSpinner(teamArrayList, teamName);
        } else {
            cancelProgress();
            AppHelper.print("Team size 0");
        }
    }

    private void loadWithSpinner(final ArrayList<Team> teamArrayList, String teamName) {
        final String teamNameArray[] = new String[teamArrayList.size()];

        for (int i = 0; i < teamArrayList.size(); i++) {
            teamNameArray[i] = teamArrayList.get(i).gettName();
        }

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNameArray);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        teamNameSpinner.setAdapter(spinnerArrayAdapter);

        teamNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                teamId = position;
                tName = teamNameArray[position];
                tLogo = teamArrayList.get(position).gettLogo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                showToast(getString(R.string.choose_team));
            }
        });

        cancelProgress();

        if (!TextUtils.isEmpty(teamName) &&
                !teamName.equalsIgnoreCase("")) {
            for (int i = 0; i < teamArrayList.size(); i++) {
                if(teamArrayList.get(i).gettName().equalsIgnoreCase(teamName)) {
                    teamNameSpinner.setSelection(i);
                    return;
                }
            }
        }
    }

    private void publishNews() {
        String posterName = eventName + "_poster";

        showProgress(getString(R.string.uploading_data));

        final StorageReference storageRef = storageReference.child("Posters/" + posterName);

        uploadTask = storageRef.putFile(eventPoster);

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
                uploadedPosterUri = task.getResult();
                AppHelper.print("Upload " + uploadedPosterUri);
                cancelProgress();

                if (uploadedPosterUri != null) {
                    String photoStringLink = uploadedPosterUri.toString();

                    eventPosterImgUrl = photoStringLink;

                    AppHelper.print("Uploaded logo Uri " + photoStringLink);

                    //onImageUploaded(uploadedPosterUri);
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

    /*private void onImageUploaded(Uri uploadedPosterUri) {
        showProgress(getString(R.string.updating_news));

        NewsFeedItems newsFeedItems = getNewsFeedItems(uploadedPosterUri);

        String id = newsDbReference.push().getKey();

        newsDbReference.child(id).setValue(newsFeedItems, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                cancelProgress();

                if (databaseError == null) {
                    showSimpleAlert(getString(R.string.news_updated_success), getString(R.string.ok), new SuperFragment.SimpleAlert() {
                        @Override
                        public void onBtnClicked(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            showUpdatedNewsFeed();
                        }
                    });
                } else {
                    AppHelper.print("Database error: " + databaseError.getMessage());

                    showSimpleAlert(getString(R.string.db_error), getString(R.string.ok), new SuperFragment.SimpleAlert() {
                        @Override
                        public void onBtnClicked(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            showUpdatedNewsFeed();
                        }
                    });
                }
            }
        });
    }*/
    private NewsFeedItems getNewsFeedItems() {

        String postedTime = AppHelper.getCurrentTime();
        String postedDate = AppHelper.getTodaysDate();

        AppHelper.print("Date posted: " + postedDate);
        AppHelper.print("Time posted: " + postedTime);

        eventName = nameEd.getText().toString().trim();
        eventDesc = descEd.getText().toString().trim();
        venueName = venueEd.getText().toString().trim();
        videoUrl = videoUrlEd.getText().toString().trim();
        eventDate = eventDateTv.getText().toString().trim();
        eventTime = eventTimeTv.getText().toString().trim();

        if (!isImageChanged) {
            eventPosterImgUrl = newsFeedItems.getPstrUrl();
        }

        NewsFeedItems newsFeedItems = new NewsFeedItems();

        newsFeedItems.seteName(eventName);
        newsFeedItems.settName(tName);
        newsFeedItems.settLogo(tLogo);
        newsFeedItems.seteDesc(eventDesc);
        newsFeedItems.seteDate(eventDate);
        newsFeedItems.seteTime(eventTime);
        newsFeedItems.setpTime(postedTime);
        newsFeedItems.setpDate(postedDate);
        newsFeedItems.seteVenue(venueName);
        newsFeedItems.setVidUrl(videoUrl);
        newsFeedItems.setPstrUrl(eventPosterImgUrl);
        newsFeedItems.setLikesCount(0);
        newsFeedItems.setCommentCount(0);
        newsFeedItems.setNfId(nfId);
        //newsFeedItems.setComment("");
        newsFeedItems.setPostedBy(dataStorage.getString(Keys.MOBILE));


        return newsFeedItems;
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

    private void showDatePickerDialog() {
        if (datePickerDialog != null && !datePickerDialog.isShowing()) {
            datePickerDialog.show();
        }
    }

    private void showTimePickerDialog() {
        if (timePickerDialog != null && !timePickerDialog.isShowing()) {
            timePickerDialog.show();
        }
    }

    private boolean isAllDetailsGiven() {
        if (teamId == -1) {
            showToast(getString(R.string.choose_team));
            return false;
        }

        eventName = nameEd.getText().toString().trim();
        if (TextUtils.isEmpty(eventName)) {
            showToast(getString(R.string.event_name_empty));
            return false;
        }

        eventDesc = descEd.getText().toString().trim();
        if (TextUtils.isEmpty(eventDesc)) {
            showToast(getString(R.string.event_desc_empty));
            return false;
        }

        eventDate = eventDateTv.getText().toString().trim();
        if (TextUtils.isEmpty(eventDate)) {
            showToast(getString(R.string.event_dare_empty));
            return false;
        }

        eventTime = eventTimeTv.getText().toString().trim();
        if (TextUtils.isEmpty(eventTime)) {
            showToast(getString(R.string.event_time_empty));
            return false;
        }

        venueName = venueEd.getText().toString().trim();
        if (TextUtils.isEmpty(venueName)) {
            showToast(getString(R.string.event_venue_empty));
            return false;
        }

        if (eventPosterImgUrl == null) {
            showToast(getString(R.string.event_poster_empty));
            return false;
        }

        videoUrl = videoUrlEd.getText().toString().trim();

//        if (!showNewsRb.isChecked() && !hideNewsRb.isChecked()) {
//            showToast(getString(R.string.empty_news_visiblity));
//            return false;
//        }

        return true;
    }

    private void showToast(String message) {
        AppHelper.showToast(this, message);
    }

    private NewsFeedItems newsFeedItems;

    private void updateUi(NewsFeedItems newsFeedItems) {
        this.newsFeedItems = newsFeedItems;

        nfId = newsFeedItems.getNfId();

        AppHelper.print("NfId: "+nfId);

        eventName = newsFeedItems.geteName();
        eventDesc = newsFeedItems.geteDesc();
        eventDate = newsFeedItems.geteDate();
        eventTime = newsFeedItems.geteTime();
        venueName = newsFeedItems.geteVenue();
        videoUrl = newsFeedItems.getVidUrl();
        eventPosterImgUrl = newsFeedItems.getPstrUrl();
        teamName = newsFeedItems.gettName();

        if (!TextUtils.isEmpty(newsFeedItems.geteName())) {
            nameEd.setText(newsFeedItems.geteName());
        }
        if (!TextUtils.isEmpty(newsFeedItems.geteDesc())) {
            descEd.setText(newsFeedItems.geteDesc());
            AppHelper.print("Description: "+newsFeedItems.geteDesc());
        }
        if (!TextUtils.isEmpty(newsFeedItems.geteDate())) {
            eventDateTv.setText(newsFeedItems.geteDate());
        }
        if (!TextUtils.isEmpty(newsFeedItems.geteTime())) {
            eventTimeTv.setText(newsFeedItems.geteTime());
        }
        if (!TextUtils.isEmpty(newsFeedItems.geteVenue())) {
            venueEd.setText(newsFeedItems.geteVenue());
        }
        if (!TextUtils.isEmpty(newsFeedItems.getPstrUrl())) {
            Glide.with(this).load(newsFeedItems.getPstrUrl()).into(posterIv);
        }
        if (!TextUtils.isEmpty(newsFeedItems.getVidUrl())) {
            videoUrlEd.setText(newsFeedItems.getVidUrl());
        }

        loadTeamInfoInSpinner(teamName);
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
}
