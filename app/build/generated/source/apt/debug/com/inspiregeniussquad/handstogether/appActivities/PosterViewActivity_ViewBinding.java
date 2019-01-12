// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import com.wang.avi.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PosterViewActivity_ViewBinding implements Unbinder {
  private PosterViewActivity target;

  private View view2131296344;

  private View view2131296589;

  @UiThread
  public PosterViewActivity_ViewBinding(PosterViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PosterViewActivity_ViewBinding(final PosterViewActivity target, View source) {
    this.target = target;

    View view;
    target.posterIv = Utils.findRequiredViewAsType(source, R.id.poster_image, "field 'posterIv'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.close_preview, "field 'closePreviewIv' and method 'onClicked'");
    target.closePreviewIv = Utils.castView(view, R.id.close_preview, "field 'closePreviewIv'", ImageView.class);
    view2131296344 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.save_preview, "field 'savePreviewIv' and method 'onClicked'");
    target.savePreviewIv = Utils.castView(view, R.id.save_preview, "field 'savePreviewIv'", ImageView.class);
    view2131296589 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
    target.loadingIv = Utils.findRequiredViewAsType(source, R.id.image_placeholder, "field 'loadingIv'", AVLoadingIndicatorView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PosterViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.posterIv = null;
    target.closePreviewIv = null;
    target.savePreviewIv = null;
    target.loadingIv = null;

    view2131296344.setOnClickListener(null);
    view2131296344 = null;
    view2131296589.setOnClickListener(null);
    view2131296589 = null;
  }
}
