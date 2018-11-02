// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OtpVerificationActivity_ViewBinding implements Unbinder {
  private OtpVerificationActivity target;

  private View view2131296562;

  private View view2131296476;

  private View view2131296315;

  @UiThread
  public OtpVerificationActivity_ViewBinding(OtpVerificationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public OtpVerificationActivity_ViewBinding(final OtpVerificationActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.verify_btn, "field 'verifyBtn' and method 'oNCLick'");
    target.verifyBtn = Utils.castView(view, R.id.verify_btn, "field 'verifyBtn'", AppCompatButton.class);
    view2131296562 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.oNCLick(p0);
      }
    });
    target.otpEd = Utils.findRequiredViewAsType(source, R.id.otp, "field 'otpEd'", TextInputEditText.class);
    target.otpTimerTv = Utils.findRequiredViewAsType(source, R.id.otp_timer, "field 'otpTimerTv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.rsend_otp_btn, "field 'resendOtpBtn' and method 'oNCLick'");
    target.resendOtpBtn = Utils.castView(view, R.id.rsend_otp_btn, "field 'resendOtpBtn'", AppCompatButton.class);
    view2131296476 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.oNCLick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.change_num, "field 'changeNumberTv' and method 'oNCLick'");
    target.changeNumberTv = Utils.castView(view, R.id.change_num, "field 'changeNumberTv'", TextView.class);
    view2131296315 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.oNCLick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    OtpVerificationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.verifyBtn = null;
    target.otpEd = null;
    target.otpTimerTv = null;
    target.resendOtpBtn = null;
    target.changeNumberTv = null;

    view2131296562.setOnClickListener(null);
    view2131296562 = null;
    view2131296476.setOnClickListener(null);
    view2131296476 = null;
    view2131296315.setOnClickListener(null);
    view2131296315 = null;
  }
}
