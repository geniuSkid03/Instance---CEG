// Generated code from Butter Knife. Do not modify!
package com.instance.ceg.appActivities;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
import com.instance.ceg.R;
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
