// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appViews.NoSwipeViewPager;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.navMenuRv = Utils.findRequiredViewAsType(source, R.id.nav_menu_recycler, "field 'navMenuRv'", RecyclerView.class);
    target.drawer = Utils.findRequiredViewAsType(source, R.id.drawer_layout, "field 'drawer'", DrawerLayout.class);
    target.navigationView = Utils.findRequiredViewAsType(source, R.id.nav_view, "field 'navigationView'", LinearLayout.class);
    target.contentLayout = Utils.findRequiredViewAsType(source, R.id.content_layout, "field 'contentLayout'", LinearLayout.class);
    target.noSwipeViewPager = Utils.findRequiredViewAsType(source, R.id.view_pager, "field 'noSwipeViewPager'", NoSwipeViewPager.class);
    target.imageView = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'imageView'", CircularImageView.class);
    target.nameTv = Utils.findRequiredViewAsType(source, R.id.user_name, "field 'nameTv'", TextView.class);
    target.editIv = Utils.findRequiredViewAsType(source, R.id.edit, "field 'editIv'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.navMenuRv = null;
    target.drawer = null;
    target.navigationView = null;
    target.contentLayout = null;
    target.noSwipeViewPager = null;
    target.imageView = null;
    target.nameTv = null;
    target.editIv = null;
  }
}
