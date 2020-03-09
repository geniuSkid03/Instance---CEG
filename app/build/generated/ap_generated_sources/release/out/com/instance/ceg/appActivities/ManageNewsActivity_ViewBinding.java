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

public class ManageNewsActivity_ViewBinding implements Unbinder {
  private ManageNewsActivity target;

  @UiThread
  public ManageNewsActivity_ViewBinding(ManageNewsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ManageNewsActivity_ViewBinding(ManageNewsActivity target, View source) {
    this.target = target;

    target.manageNewsTab = Utils.findRequiredViewAsType(source, R.id.manage_news_tab, "field 'manageNewsTab'", TabLayout.class);
    target.manageNewsViewPager = Utils.findRequiredViewAsType(source, R.id.manage_news_viewpager, "field 'manageNewsViewPager'", ViewPager.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ManageNewsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.manageNewsTab = null;
    target.manageNewsViewPager = null;
    target.toolbar = null;
  }
}
