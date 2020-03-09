package com.instance.ceg.appAdapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instance.ceg.R;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appViews.CircularImageView;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.Comments> {

    private Context context;
    private ArrayList<com.instance.ceg.appData.Comments> commentsArrayList;

    public CommentsAdapter(Context context, ArrayList<com.instance.ceg.appData.Comments> commentsArrayList) {
        this.context = context;
        this.commentsArrayList = commentsArrayList;
    }

    @NonNull
    @Override
    public Comments onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Comments(LayoutInflater.from(context).inflate(R.layout.comments_item_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Comments comments, int i) {
        comments.bind(commentsArrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return commentsArrayList.size();
    }

    class Comments extends RecyclerView.ViewHolder {

        private TextView nameTv, commentTv, postedTimeTv;
        private CircularImageView userIconIv;

        Comments(View view) {
            super(view);

            nameTv = view.findViewById(R.id.user_name);
            postedTimeTv = view.findViewById(R.id.comment_time);
            userIconIv = view.findViewById(R.id.user_image);
            commentTv = view.findViewById(R.id.user_comment);

        }

        void bind(com.instance.ceg.appData.Comments comments) {
            nameTv.setText(comments.getcName());

            userIconIv.setBackground(comments.getcGender().equalsIgnoreCase(Keys.MALE) ?
                    ContextCompat.getDrawable(context, R.drawable.ic_man) :
                    ContextCompat.getDrawable(context, R.drawable.ic_girl));

            commentTv.setText(comments.getCmnt());
            postedTimeTv.setText(comments.getcTime());
        }
    }
}
