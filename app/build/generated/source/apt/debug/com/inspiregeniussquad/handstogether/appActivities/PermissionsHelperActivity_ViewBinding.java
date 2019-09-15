// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.view.View;
import android.widget.ListView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PermissionsHelperActivity_ViewBinding implements Unbinder {
  private PermissionsHelperActivity target;

  private View view7f09009f;

  @UiThread
  public PermissionsHelperActivity_ViewBinding(PermissionsHelperActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PermissionsHelperActivity_ViewBinding(final PermissionsHelperActivity target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.continue_btn, "field 'continueBtn' and method 'onClicked'");
    target.continueBtn = Utils.castView(view, R.id.continue_btn, "field 'continueBtn'", AppCompatButton.class);
    view7f09009f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
    target.permissionLv = Utils.findRequiredViewAsType(source, R.id.permissions_list, "field 'permissionLv'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PermissionsHelperActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.continueBtn = null;
    target.permissionLv = null;

    view7f09009f.setOnClickListener(null);
    view7f09009f = null;
  }
}
