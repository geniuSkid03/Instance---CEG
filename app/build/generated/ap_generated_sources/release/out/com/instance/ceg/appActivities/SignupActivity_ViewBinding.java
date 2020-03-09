// Generated code from Butter Knife. Do not modify!
package com.instance.ceg.appActivities;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SignupActivity_ViewBinding implements Unbinder {
  private SignupActivity target;

  private View view7f090113;

  private View view7f0900d3;

  private View view7f09019d;

  private View view7f090111;

  private View view7f0900d0;

  private View view7f0900e2;

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
    view7f090113 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.female_rb, "field 'femaleRdBtn' and method 'onclicked'");
    target.femaleRdBtn = Utils.castView(view, R.id.female_rb, "field 'femaleRdBtn'", RadioButton.class);
    view7f0900d3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.register_me, "field 'registerMeBtn' and method 'onclicked'");
    target.registerMeBtn = Utils.castView(view, R.id.register_me, "field 'registerMeBtn'", AppCompatButton.class);
    view7f09019d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.login_me, "field 'loginMeBtn' and method 'onclicked'");
    target.loginMeBtn = Utils.castView(view, R.id.login_me, "field 'loginMeBtn'", TextView.class);
    view7f090111 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.facebook_login, "field 'fbLoginIv' and method 'onclicked'");
    target.fbLoginIv = Utils.castView(view, R.id.facebook_login, "field 'fbLoginIv'", ImageView.class);
    view7f0900d0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.google_login, "field 'googleLoginIv' and method 'onclicked'");
    target.googleLoginIv = Utils.castView(view, R.id.google_login, "field 'googleLoginIv'", ImageView.class);
    view7f0900e2 = view;
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

    view7f090113.setOnClickListener(null);
    view7f090113 = null;
    view7f0900d3.setOnClickListener(null);
    view7f0900d3 = null;
    view7f09019d.setOnClickListener(null);
    view7f09019d = null;
    view7f090111.setOnClickListener(null);
    view7f090111 = null;
    view7f0900d0.setOnClickListener(null);
    view7f0900d0 = null;
    view7f0900e2.setOnClickListener(null);
    view7f0900e2 = null;
  }
}
