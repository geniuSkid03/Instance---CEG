// Generated code from Butter Knife. Do not modify!
package com.instance.ceg.appActivities;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.instance.ceg.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SplashActivity_ViewBinding implements Unbinder {
  private SplashActivity target;

  @UiThread
  public SplashActivity_ViewBinding(SplashActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SplashActivity_ViewBinding(SplashActivity target, View source) {
    this.target = target;

    target.splashIv = Utils.findRequiredViewAsType(source, R.id.logo, "field 'splashIv'", ImageView.class);
    target.appMottoTv = Utils.findRequiredViewAsType(source, R.id.app_motto, "field 'appMottoTv'", TextView.class);
    target.appNameTv = Utils.findRequiredViewAsType(source, R.id.app_name, "field 'appNameTv'", TextView.class);
    target.rootView = Utils.findRequiredViewAsType(source, R.id.root_view, "field 'rootView'", LinearLayout.class);
    target.splashFooterLayout = Utils.findRequiredViewAsType(source, R.id.splash_footer, "field 'splashFooterLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SplashActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.splashIv = null;
    target.appMottoTv = null;
    target.appNameTv = null;
    target.rootView = null;
    target.splashFooterLayout = null;
  }
}
