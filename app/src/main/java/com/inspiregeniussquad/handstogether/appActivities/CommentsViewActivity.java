package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appViews.CircularImageView;

import butterknife.BindView;

public class CommentsViewActivity extends SuperCompatActivity {

    @BindView(R.id.comments_recycler)
    RecyclerView commentsRv;

    @BindView(R.id.no_comments_tv)
    TextView noCommentsTv;

    @BindView(R.id.profile_img)
    CircularImageView userProfileIv;

    @BindView(R.id.post_comment)
    FloatingActionButton postFab;

    @BindView(R.id.comment_view)
    EditText commentEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_view);
    }
}
