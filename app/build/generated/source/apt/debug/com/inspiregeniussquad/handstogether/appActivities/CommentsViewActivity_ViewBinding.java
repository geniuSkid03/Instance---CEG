// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appViews.CircularImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CommentsViewActivity_ViewBinding implements Unbinder {
  private CommentsViewActivity target;

  @UiThread
  public CommentsViewActivity_ViewBinding(CommentsViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CommentsViewActivity_ViewBinding(CommentsViewActivity target, View source) {
    this.target = target;

    target.commentsRv = Utils.findRequiredViewAsType(source, R.id.comments_recycler, "field 'commentsRv'", RecyclerView.class);
    target.noCommentsTv = Utils.findRequiredViewAsType(source, R.id.no_comments_tv, "field 'noCommentsTv'", TextView.class);
    target.userProfileIv = Utils.findRequiredViewAsType(source, R.id.profile_img, "field 'userProfileIv'", CircularImageView.class);
    target.postFab = Utils.findRequiredViewAsType(source, R.id.post_comment, "field 'postFab'", FloatingActionButton.class);
    target.commentEd = Utils.findRequiredViewAsType(source, R.id.comment_view, "field 'commentEd'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CommentsViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.commentsRv = null;
    target.noCommentsTv = null;
    target.userProfileIv = null;
    target.postFab = null;
    target.commentEd = null;
  }
}
