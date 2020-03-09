// Generated code from Butter Knife. Do not modify!
package com.instance.ceg.appActivities;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.instance.ceg.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OtpVerificationActivity_ViewBinding implements Unbinder {
  private OtpVerificationActivity target;

  private View view7f09023e;

  private View view7f0901aa;

  private View view7f090070;

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
    view7f09023e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.oNCLick(p0);
      }
    });
    target.otpEd = Utils.findRequiredViewAsType(source, R.id.otp, "field 'otpEd'", EditText.class);
    target.otpTimerTv = Utils.findRequiredViewAsType(source, R.id.otp_timer, "field 'otpTimerTv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.rsend_otp_btn, "field 'resendOtpBtn' and method 'oNCLick'");
    target.resendOtpBtn = Utils.castView(view, R.id.rsend_otp_btn, "field 'resendOtpBtn'", AppCompatButton.class);
    view7f0901aa = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.oNCLick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.change_num, "field 'changeNumberTv' and method 'oNCLick'");
    target.changeNumberTv = Utils.castView(view, R.id.change_num, "field 'changeNumberTv'", TextView.class);
    view7f090070 = view;
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

    view7f09023e.setOnClickListener(null);
    view7f09023e = null;
    view7f0901aa.setOnClickListener(null);
    view7f0901aa = null;
    view7f090070.setOnClickListener(null);
    view7f090070 = null;
  }
}
