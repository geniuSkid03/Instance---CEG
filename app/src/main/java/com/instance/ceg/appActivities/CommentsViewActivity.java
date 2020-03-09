package com.instance.ceg.appActivities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.instance.ceg.R;
import com.instance.ceg.appAdapters.CommentsAdapter;
import com.instance.ceg.appData.Comments;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.NewsFeedItems;
import com.instance.ceg.appUtils.AppHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CommentsViewActivity extends SuperCompatActivity {

    @BindView(R.id.comments_recycler)
    RecyclerView commentsRv;

    @BindView(R.id.no_comments_tv)
    TextView noCommentsTv;

    @BindView(R.id.post_comment)
    FloatingActionButton postFab;

    @BindView(R.id.comment_view)
    EditText commentEd;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    @BindView(R.id.event_poster)
    ImageView posterIv;

    @BindView(R.id.title)
    TextView eventTitleTv;

    @BindView(R.id.desc)
    TextView eventDescTv;

    @BindView(R.id.posting_View)
    ProgressBar postingView;

    private NewsFeedItems newsFeedItems;
    private ArrayList<Comments> commentsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_view);

        setUpTOolbar();

        if (getIntent() != null && getIntent().getExtras() != null) {
            newsFeedItems = gson.fromJson(getIntent().getStringExtra(Keys.NEWS_ITEM), NewsFeedItems.class);
            updateUi(newsFeedItems);
        }

        commentsArrayList = new ArrayList<>();

        postFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndAddComment();
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

    private void checkAndAddComment() {
        String comment = commentEd.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            showToast(this, getString(R.string.enter_your_comment));
            return;
        }

        showAsInserting();

        insertComment(comment);
    }

    private void insertComment(String comment) {
        commentsArrayList.add(getCommentValues(comment));

        commentsDbReference.child(newsFeedItems.getNfId())
                .setValue(commentsArrayList, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            showAsInserted();
                        } else {
                            showToast(CommentsViewActivity.this, getString(R.string.some_error_occurred));
                            postFab.show();
                            postingView.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void showAsInserting() {
        commentEd.setText("");
        postFab.hide();
        postingView.setVisibility(View.VISIBLE);
    }

    private void showAsInserted() {
        postingView.setVisibility(View.GONE);
        postFab.show();

        loadComments();
    }

    private Comments getCommentValues(String comment) {
        String userName = dataStorage.getString(Keys.USER_NAME);
        String userImage = dataStorage.getString(Keys.USER_GENDER);
        return new Comments(userName, AppHelper.getTodaysDate(), AppHelper.getCurrentTime(), comment, userImage);
    }

    private void setUpTOolbar() {
//        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void updateUi(NewsFeedItems newsFeedItems) {
        if (newsFeedItems != null) {
            Glide.with(this).load(newsFeedItems.getPstrUrl()).into(posterIv);
            eventTitleTv.setText(newsFeedItems.geteName());
            eventDescTv.setText(newsFeedItems.geteDesc());
        }
    }

    private void loadComments() {
        //todo load comments for this post id
        if (newsFeedItems != null) {
            commentsDbReference.child(newsFeedItems.getNfId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        AppHelper.print("Comment datasnapshot exists");
                        retrieveComments(dataSnapshot);
                    } else {
                        AppHelper.print("Comment datasnapshot null");
                        updateComments();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    AppHelper.print("Error in loading comment");
                    updateComments();
                }
            });
        }
    }

    private void retrieveComments(DataSnapshot dataSnapshot) {
        commentsArrayList.clear();

        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
            commentsArrayList.add(dataSnapshot1.getValue(Comments.class));
        }

        commentsRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        CommentsAdapter commentsAdapter = new CommentsAdapter(this, commentsArrayList);
        commentsRv.setAdapter(commentsAdapter);

        updateComments();
    }

    private void updateComments() {
        commentsRv.setVisibility(commentsArrayList.size() > 0 ? View.VISIBLE : View.GONE);
        noCommentsTv.setVisibility(commentsArrayList.size() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadComments();
    }
}
