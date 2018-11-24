// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
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

  private View view2131296361;

  private View view2131296419;

  private View view2131296377;

  private View view2131296492;

  private View view2131296417;

  @UiThread
  public SignupActivity_ViewBinding(SignupActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignupActivity_ViewBinding(final SignupActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.email, "field 'emailEd' and method 'onclicked'");
    target.emailEd = Utils.castView(view, R.id.email, "field 'emailEd'", TextInputEditText.class);
    view2131296361 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    target.nameEd = Utils.findRequiredViewAsType(source, R.id.name, "field 'nameEd'", TextInputEditText.class);
    target.radioGrp = Utils.findRequiredViewAsType(source, R.id.radio_grp, "field 'radioGrp'", RadioGroup.class);
    view = Utils.findRequiredView(source, R.id.male_rb, "field 'maleRdBtn' and method 'onclicked'");
    target.maleRdBtn = Utils.castView(view, R.id.male_rb, "field 'maleRdBtn'", RadioButton.class);
    view2131296419 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.female_rb, "field 'femaleRdBtn' and method 'onclicked'");
    target.femaleRdBtn = Utils.castView(view, R.id.female_rb, "field 'femaleRdBtn'", RadioButton.class);
    view2131296377 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.register_me, "field 'registerMeBtn' and method 'onclicked'");
    target.registerMeBtn = Utils.castView(view, R.id.register_me, "field 'registerMeBtn'", AppCompatButton.class);
    view2131296492 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.login_me, "field 'loginMeBtn' and method 'onclicked'");
    target.loginMeBtn = Utils.castView(view, R.id.login_me, "field 'loginMeBtn'", TextView.class);
    view2131296417 = view;
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

    view2131296361.setOnClickListener(null);
    view2131296361 = null;
    view2131296419.setOnClickListener(null);
    view2131296419 = null;
    view2131296377.setOnClickListener(null);
    view2131296377 = null;
    view2131296492.setOnClickListener(null);
    view2131296492 = null;
    view2131296417.setOnClickListener(null);
    view2131296417 = null;
  }
}
