// Generated code from Butter Knife. Do not modify!
package com.instance.ceg.appActivities;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.instance.ceg.R;
import com.instance.ceg.appViews.ZoomImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PosterViewActivity_ViewBinding implements Unbinder {
  private PosterViewActivity target;

  private View view7f0901ae;

  @UiThread
  public PosterViewActivity_ViewBinding(PosterViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PosterViewActivity_ViewBinding(final PosterViewActivity target, View source) {
    this.target = target;

    View view;
    target.posterIv = Utils.findRequiredViewAsType(source, R.id.poster_image, "field 'posterIv'", ZoomImageView.class);
    view = Utils.findRequiredView(source, R.id.save_fab, "field 'savePreviewIv' and method 'onClicked'");
    target.savePreviewIv = Utils.castView(view, R.id.save_fab, "field 'savePreviewIv'", FloatingActionButton.class);
    view7f0901ae = view;
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
    target.savePreviewIv = null;

    view7f0901ae.setOnClickListener(null);
    view7f0901ae = null;
  }
}
