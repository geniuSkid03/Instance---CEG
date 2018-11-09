// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MobileNumberActivity_ViewBinding implements Unbinder {
  private MobileNumberActivity target;

  private View view2131296338;

  @UiThread
  public MobileNumberActivity_ViewBinding(MobileNumberActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MobileNumberActivity_ViewBinding(final MobileNumberActivity target, View source) {
    this.target = target;

    View view;
    target.inputMobile = Utils.findRequiredViewAsType(source, R.id.mobile, "field 'inputMobile'", EditText.class);
    view = Utils.findRequiredView(source, R.id.continue_btn, "field 'continueBtn' and method 'onClick'");
    target.continueBtn = Utils.castView(view, R.id.continue_btn, "field 'continueBtn'", AppCompatButton.class);
    view2131296338 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MobileNumberActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.inputMobile = null;
    target.continueBtn = null;

    view2131296338.setOnClickListener(null);
    view2131296338 = null;
  }
}
