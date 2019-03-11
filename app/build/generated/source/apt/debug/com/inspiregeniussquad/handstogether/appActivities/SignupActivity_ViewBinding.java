// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SignupActivity_ViewBinding implements Unbinder {
  private SignupActivity target;

  private View view2131296494;

  private View view2131296435;

  private View view2131296601;

  private View view2131296492;

  private View view2131296433;

  private View view2131296446;

  @UiThread
  public SignupActivity_ViewBinding(SignupActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignupActivity_ViewBinding(final SignupActivity target, View source) {
    this.target = target;

    View view;
    target.emailEd = Utils.findRequiredViewAsType(source, R.id.email, "field 'emailEd'", EditText.class);
    target.nameEd = Utils.findRequiredViewAsType(source, R.id.name, "field 'nameEd'", EditText.class);
    target.radioGrp = Utils.findRequiredViewAsType(source, R.id.radio_grp, "field 'radioGrp'", RadioGroup.class);
    view = Utils.findRequiredView(source, R.id.male_rb, "field 'maleRdBtn' and method 'onclicked'");
    target.maleRdBtn = Utils.castView(view, R.id.male_rb, "field 'maleRdBtn'", RadioButton.class);
    view2131296494 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.female_rb, "field 'femaleRdBtn' and method 'onclicked'");
    target.femaleRdBtn = Utils.castView(view, R.id.female_rb, "field 'femaleRdBtn'", RadioButton.class);
    view2131296435 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.register_me, "field 'registerMeBtn' and method 'onclicked'");
    target.registerMeBtn = Utils.castView(view, R.id.register_me, "field 'registerMeBtn'", AppCompatButton.class);
    view2131296601 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.login_me, "field 'loginMeBtn' and method 'onclicked'");
    target.loginMeBtn = Utils.castView(view, R.id.login_me, "field 'loginMeBtn'", TextView.class);
    view2131296492 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.facebook_login, "field 'fbLoginIv' and method 'onclicked'");
    target.fbLoginIv = Utils.castView(view, R.id.facebook_login, "field 'fbLoginIv'", ImageView.class);
    view2131296433 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.google_login, "field 'googleLoginIv' and method 'onclicked'");
    target.googleLoginIv = Utils.castView(view, R.id.google_login, "field 'googleLoginIv'", ImageView.class);
    view2131296446 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SignupActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.emailEd = null;
    target.nameEd = null;
    target.radioGrp = null;
    target.maleRdBtn = null;
    target.femaleRdBtn = null;
    target.registerMeBtn = null;
    target.loginMeBtn = null;
    target.fbLoginIv = null;
    target.googleLoginIv = null;

    view2131296494.setOnClickListener(null);
    view2131296494 = null;
    view2131296435.setOnClickListener(null);
    view2131296435 = null;
    view2131296601.setOnClickListener(null);
    view2131296601 = null;
    view2131296492.setOnClickListener(null);
    view2131296492 = null;
    view2131296433.setOnClickListener(null);
    view2131296433 = null;
    view2131296446.setOnClickListener(null);
    view2131296446 = null;
  }
}
