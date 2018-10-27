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

public class ManageTeamsActivity_ViewBinding implements Unbinder {
  private ManageTeamsActivity target;

  @UiThread
  public ManageTeamsActivity_ViewBinding(ManageTeamsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ManageTeamsActivity_ViewBinding(ManageTeamsActivity target, View source) {
    this.target = target;

    target.tabLayout = Utils.findRequiredViewAsType(source, R.id.manage_teams_tab, "field 'tabLayout'", TabLayout.class);
    target.viewPager = Utils.findRequiredViewAsType(source, R.id.manage_teams_viewpager, "field 'viewPager'", ViewPager.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ManageTeamsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tabLayout = null;
    target.viewPager = null;
    target.toolbar = null;
  }
}
