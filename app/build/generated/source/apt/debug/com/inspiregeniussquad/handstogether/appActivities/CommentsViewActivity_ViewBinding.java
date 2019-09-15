// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inspiregeniussquad.handstogether.R;
import com.wang.avi.AVLoadingIndicatorView;
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
    target.postFab = Utils.findRequiredViewAsType(source, R.id.post_comment, "field 'postFab'", FloatingActionButton.class);
    target.commentEd = Utils.findRequiredViewAsType(source, R.id.comment_view, "field 'commentEd'", EditText.class);
    target.posterIv = Utils.findRequiredViewAsType(source, R.id.event_poster, "field 'posterIv'", ImageView.class);
    target.imgLoadingView = Utils.findRequiredViewAsType(source, R.id.img_loading_view, "field 'imgLoadingView'", AVLoadingIndicatorView.class);
    target.eventTitleTv = Utils.findRequiredViewAsType(source, R.id.title, "field 'eventTitleTv'", TextView.class);
    target.eventDescTv = Utils.findRequiredViewAsType(source, R.id.desc, "field 'eventDescTv'", TextView.class);
    target.postingView = Utils.findRequiredViewAsType(source, R.id.posting_View, "field 'postingView'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CommentsViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.commentsRv = null;
    target.noCommentsTv = null;
    target.postFab = null;
    target.commentEd = null;
    target.posterIv = null;
    target.imgLoadingView = null;
    target.eventTitleTv = null;
    target.eventDescTv = null;
    target.postingView = null;
  }
}
