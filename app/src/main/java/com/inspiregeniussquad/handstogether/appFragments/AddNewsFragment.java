package com.inspiregeniussquad.handstogether.appFragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appData.Team;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.inspiregeniussquad.handstogether.appUtils.TeamDataHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

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

    private TeamDataHelper teamDataHelper;

    private static final int CHOOSE_FILE = 101;

    private Calendar dateSelected = Calendar.getInstance();
    private Calendar timeSelected = Calendar.getInstance();

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        teamDataHelper = new TeamDataHelper(getActivity());
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
        newsVisibilityGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.visible_news:
                        isNewsVisible = true;
                        break;
                    case R.id.invisible_news:
                        isNewsVisible = false;
                        break;
                }
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

        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hrs, int mins) {
                eventTimeTv.setText(String.format(Locale.getDefault(), "%d:%d", hrs, mins));
                AppHelper.print("Time: " + eventTimeTv.getText().toString().trim());
            }
        }, hour, minute, true);
        eventTimeTv.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute));
        AppHelper.print("Time: " + eventTimeTv.getText().toString().trim());
    }

    private void doFunctionsForClick(View view) {
        switch (view.getId()) {
            case R.id.publish_news:
                if (isAllDetailsGiven()) {
                    publishNews();
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), eventPoster);
                    if (bitmap != null) {
                        Picasso.get().load(eventPoster).into(posterIv);
                    } else {
                        AppHelper.showToast(getActivity(), "Image bitmap null");
                    }
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
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
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
            }
        });
    }

    private void onImageUploaded(Uri uploadedPosterUri) {
        showProgress(getString(R.string.updating_news));

        NewsFeedItems newsFeedItems = getNewsFeedItems(uploadedPosterUri);

        newsDbReference.child(newsDbReference.push().getKey()).setValue(newsFeedItems, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                cancelProgress();

                if (databaseError == null) {
                    showSimpleAlert(getString(R.string.news_updated_success), getString(R.string.ok), new SimpleAlert() {
                        @Override
                        public void onBtnClicked(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            showUpdatedNewsFeed();
                        }
                    });
                } else {
                    AppHelper.print("Database error: " + databaseError.getMessage());

                    showSimpleAlert(getString(R.string.db_error), getString(R.string.ok), new SimpleAlert() {
                        @Override
                        public void onBtnClicked(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            showUpdatedNewsFeed();
                        }
                    });
                }
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
        Calendar calendar = Calendar.getInstance();
        int postedHr = calendar.get(Calendar.HOUR_OF_DAY);
        int postedMin = calendar.get(Calendar.MINUTE);
        String postedTime = postedHr + ":" + postedMin;

        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(date, year, month, 0, 0);
        String postedDate = AppHelper.getFormattedDate(calendar);

        AppHelper.print("Date posted: "+postedDate);
        AppHelper.print("Time posted: "+postedTime);

        NewsFeedItems newsFeedItems = new NewsFeedItems();

        newsFeedItems.settName(teamDataHelper.getTeamName(teamId));
        newsFeedItems.seteName(eventName);
        newsFeedItems.seteDesc(eventDesc);
        newsFeedItems.seteDate(eventDate);
        newsFeedItems.seteTime(eventTime);
        newsFeedItems.setpTime(postedTime);
        newsFeedItems.setpDate(postedDate);
        newsFeedItems.seteVenue(venueName);
        newsFeedItems.setVidUrl(videoUrl);
        newsFeedItems.setPstrUrl(uploadedPosterUri.toString());
        newsFeedItems.setLikes(0);
        newsFeedItems.setComments("");
        newsFeedItems.setPostedBy(dataStorage.getString(Keys.MOBILE));

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

            team.setTeamName((String) map.get("teamName"));
            team.setTeamLogoUri((String) map.get("teamLogoUri"));

            AppHelper.print("Team data: " + team.getTeamName() + "\t" + team.getTeamLogoUri());

            teamArrayList.add(team);
        }

        if (teamArrayList.size() != 0) {
            loadWithSpinner(teamArrayList);
        } else {
            cancelProgress();
            AppHelper.print("Team size 0");
        }
    }

    private void loadWithSpinner(ArrayList<Team> teamArrayList) {
        String teamNameArray[] = new String[teamArrayList.size()];

        for (int i = 0; i < teamArrayList.size(); i++) {
            teamNameArray[i] = teamArrayList.get(i).getTeamName();
        }

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, teamNameArray);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        teamNameSpinner.setAdapter(spinnerArrayAdapter);

        teamNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                teamId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                showToast(getString(R.string.choose_team));
            }
        });

        cancelProgress();
    }

}
