// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appViews.NoSwipeViewPager;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeActivity_ViewBinding implements Unbinder {
  private HomeActivity target;

  @UiThread
  public HomeActivity_ViewBinding(HomeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HomeActivity_ViewBinding(HomeActivity target, View source) {
    this.target = target;

    target.expandableListView = Utils.findRequiredViewAsType(source, R.id.expandable_list, "field 'expandableListView'", ExpandableListView.class);
    target.drawer = Utils.findRequiredViewAsType(source, R.id.drawer_layout, "field 'drawer'", DrawerLayout.class);
    target.navigationView = Utils.findRequiredViewAsType(source, R.id.nav_view, "field 'navigationView'", NavigationView.class);
    target.contentLayout = Utils.findRequiredViewAsType(source, R.id.content_layout, "field 'contentLayout'", LinearLayout.class);
    target.noSwipeViewPager = Utils.findRequiredViewAsType(source, R.id.view_pager, "field 'noSwipeViewPager'", NoSwipeViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.expandableListView = null;
    target.drawer = null;
    target.navigationView = null;
    target.contentLayout = null;
    target.noSwipeViewPager = null;
  }
}
