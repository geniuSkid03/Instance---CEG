// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
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
