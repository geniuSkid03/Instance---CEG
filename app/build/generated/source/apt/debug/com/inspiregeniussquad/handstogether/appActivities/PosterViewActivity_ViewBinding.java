// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appViews.ZoomImageView;
import com.wang.avi.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PosterViewActivity_ViewBinding implements Unbinder {
  private PosterViewActivity target;

  private View view2131296616;

  @UiThread
  public PosterViewActivity_ViewBinding(PosterViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PosterViewActivity_ViewBinding(final PosterViewActivity target, View source) {
    this.target = target;

    View view;
    target.posterIv = Utils.findRequiredViewAsType(source, R.id.poster_image, "field 'posterIv'", ZoomImageView.class);
    target.loadingIv = Utils.findRequiredViewAsType(source, R.id.image_placeholder, "field 'loadingIv'", AVLoadingIndicatorView.class);
    view = Utils.findRequiredView(source, R.id.save_fab, "field 'savePreviewIv' and method 'onClicked'");
    target.savePreviewIv = Utils.castView(view, R.id.save_fab, "field 'savePreviewIv'", FloatingActionButton.class);
    view2131296616 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    PosterViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.posterIv = null;
    target.loadingIv = null;
    target.savePreviewIv = null;

    view2131296616.setOnClickListener(null);
    view2131296616 = null;
  }
}
