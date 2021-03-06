package com.instance.ceg.appFragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.instance.ceg.R;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.NewsFeedItems;
import com.instance.ceg.appData.Team;
import com.instance.ceg.appUtils.AppHelper;
import com.instance.ceg.appUtils.TeamDataHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddNewsFragment extends SuperFragment {

    private Spinner teamNameSpinner;
    private EditText nameEd, descEd, venueEd, videoUrlEd;
    private TextView eventDateTv, eventTimeTv;
    private RadioGroup newsVisibilityGrp;
    private RadioButton showNewsRb, hideNewsRb;
    private ImageButton posterImgBtn;
    private ImageView posterIv;
    private AppCompatButton publishNewsBtn;

    private String teamName, eventDesc, eventName, venueName, videoUrl, eventDate, eventTime;
    private Uri eventPoster, uploadedPosterUri;
    private boolean isNewsVisible, isFirstCompleted = false;
    private int teamId = -1;
    private String tName = "";
    private String tLogo="";

    private TeamDataHelper teamDataHelper;

    private static final int CHOOSE_FILE = 101;

    private Calendar dateSelected = Calendar.getInstance();
    private Calendar timeSelected = Calendar.getInstance();

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private String nfId="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        teamDataHelper = new TeamDataHelper(getActivity());
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.add_news_fragment, container, false));
    }

    private View initView(View view) {

        teamNameSpinner = view.findViewById(R.id.team_spinner);
        nameEd = view.findViewById(R.id.event_name);
        descEd = view.findViewById(R.id.event_desc);
        eventDateTv = view.findViewById(R.id.event_date);
        eventTimeTv = view.findViewById(R.id.event_time);
        venueEd = view.findViewById(R.id.event_venue);
        posterIv = view.findViewById(R.id.event_poster);
        videoUrlEd = view.findViewById(R.id.event_video_url);
        newsVisibilityGrp = view.findViewById(R.id.news_rgrb);
        showNewsRb = view.findViewById(R.id.visible_news);
        hideNewsRb = view.findViewById(R.id.invisible_news);
        posterImgBtn = view.findViewById(R.id.add_event_poster);

        publishNewsBtn = view.findViewById(R.id.publish_news);

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

        showNewsRb.setChecked(true);
        isNewsVisible = true;
        newsVisibilityGrp.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.visible_news:
                    isNewsVisible = true;
                    break;
                case R.id.invisible_news:
                    isNewsVisible = false;
                    break;
            }
        });

        loadTeamInfoInSpinner();

        loadDatePicker();

        loadTimePicker();

        return view;
    }

    private void loadDatePicker() {
        Calendar calendar = dateSelected;

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateSelected.set(year, month, day, 0, 0);
                eventDateTv.setText(AppHelper.getFormattedDate(dateSelected));
                AppHelper.print("Date: " + eventDateTv.getText().toString());

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        eventDateTv.setText(AppHelper.getFormattedDate(dateSelected));

        AppHelper.print("Date: " + eventDateTv.getText().toString());
    }

    private void loadTimePicker() {
        int hour = timeSelected.get(Calendar.HOUR_OF_DAY);
        int minute = timeSelected.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(getActivity(), (timePicker, hrs, mins) -> {
            eventTimeTv.setText(String.format(Locale.getDefault(), "%d:%d", hrs, mins));
            AppHelper.print("Time: " + eventTimeTv.getText().toString().trim());
        }, hour, minute, true);
        eventTimeTv.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute));
        AppHelper.print("Time: " + eventTimeTv.getText().toString().trim());
    }

    private void doFunctionsForClick(View view) {
        switch (view.getId()) {
            case R.id.publish_news:
                if (isAllDetailsGiven()) publishNews();
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

        if (eventPoster == null) {
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
                eventPoster = data.getData();

                try {
                    if(eventPoster != null) {
                        Glide.with(Objects.requireNonNull(getContext())).load(eventPoster).into(posterIv);
                    } else {
                        AppHelper.showToast(getActivity(), "Image null");
                    }
//
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), eventPoster);
//                    if (bitmap != null) {
//                        Picasso.get().load(eventPoster).into(posterIv);
//                    } else {
//                        AppHelper.showToast(getActivity(), "Image bitmap null");
//                    }
                } catch (Exception e) {
                    AppHelper.showToast(getActivity(), "Exception in parsing image");
                }

            }
        }
    }

    private void publishNews() {
        String posterName = eventName + "_poster";

        showProgress(getString(R.string.uploading_data));

        final StorageReference storageRef = storageReference.child("Posters/" + posterName);

        uploadTask = storageRef.putFile(eventPoster);

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
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                uploadedPosterUri = task.getResult();
                AppHelper.print("Upload " + uploadedPosterUri);
                cancelProgress();

                if (uploadedPosterUri != null) {
                    String photoStringLink = uploadedPosterUri.toString();
                    AppHelper.print("Uploaded logo Uri " + photoStringLink);

                    onImageUploaded(uploadedPosterUri);
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

    private void onImageUploaded(Uri uploadedPosterUri) {
        showProgress(getString(R.string.updating_news));

        NewsFeedItems newsFeedItems = getNewsFeedItems(uploadedPosterUri);

        String id = newsDbReference.push().getKey();

        newsDbReference.child(newsFeedItems.getNfId()).setValue(newsFeedItems, (databaseError, databaseReference) -> {
            cancelProgress();

            if (databaseError == null) {
                showSimpleAlert(getString(R.string.news_updated_success), getString(R.string.ok), (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                    showUpdatedNewsFeed();
                });
            } else {
                AppHelper.print("Database error: " + databaseError.getMessage());

                showSimpleAlert(getString(R.string.db_error), getString(R.string.ok), (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                    showUpdatedNewsFeed();
                });
            }
        });
    }

    private void showUpdatedNewsFeed() {
        clearViewData();
        //todo set and show updated news feed fragment
    }

    private void clearViewData() {
        teamNameSpinner.setSelection(0);
        nameEd.setText("");
        descEd.setText("");
        venueEd.setText("");
        videoUrlEd.setText("");
        posterIv.setImageResource(0);
    }

    private NewsFeedItems getNewsFeedItems(Uri uploadedPosterUri) {

        String postedTime = AppHelper.getCurrentTime();
        String postedDate = AppHelper.getTodaysDate();

        AppHelper.print("Date posted: "+postedDate);
        AppHelper.print("Time posted: "+postedTime);

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
        newsFeedItems.setPstrUrl(uploadedPosterUri.toString());
        newsFeedItems.setLikesCount(0);
        newsFeedItems.setCommentCount(0);
        newsFeedItems.setNfId(eventName+"_"+postedDate);
        newsFeedItems.setPostedBy(dataStorage.getString(Keys.MOBILE));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int millis = calendar.get(Calendar.MILLISECOND);
        AppHelper.print("News Millis: "+millis);
        newsFeedItems.setTimeStamp(millis);

        return newsFeedItems;
    }

    private void loadTeamInfoInSpinner() {
        showProgress(getString(R.string.loading_teams));

        teamDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    retriveTeamsData(dataSnapshot);
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

    private void retriveTeamsData(DataSnapshot dataSnapshot) {
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
            loadWithSpinner(teamArrayList);
        } else {
            cancelProgress();
            AppHelper.print("Team size 0");
        }
    }

    private void loadWithSpinner(final ArrayList<Team> teamArrayList) {
        final String teamNameArray[] = new String[teamArrayList.size()];

        for (int i = 0; i < teamArrayList.size(); i++) {
            teamNameArray[i] = teamArrayList.get(i).gettName();
        }

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, teamNameArray);

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
    }

}
