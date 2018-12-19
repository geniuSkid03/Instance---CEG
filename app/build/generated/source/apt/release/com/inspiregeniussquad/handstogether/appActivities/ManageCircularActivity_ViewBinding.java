// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ManageCircularActivity_ViewBinding implements Unbinder {
  private ManageCircularActivity target;

  @UiThread
  public ManageCircularActivity_ViewBinding(ManageCircularActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ManageCircularActivity_ViewBinding(ManageCircularActivity target, View source) {
    this.target = target;

    target.manageCircularTab = Utils.findRequiredViewAsType(source, R.id.manage_circular_tab, "field 'manageCircularTab'", TabLayout.class);
    target.manageCircularViewPager = Utils.findRequiredViewAsType(source, R.id.manage_circular_viewpager, "field 'manageCircularViewPager'", ViewPager.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ManageCircularActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.manageCircularTab = null;
    target.manageCircularViewPager = null;
    target.toolbar = null;
  }
}
