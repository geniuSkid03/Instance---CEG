// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ManageAdminsActivity_ViewBinding implements Unbinder {
  private ManageAdminsActivity target;

  @UiThread
  public ManageAdminsActivity_ViewBinding(ManageAdminsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ManageAdminsActivity_ViewBinding(ManageAdminsActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.adminsLv = Utils.findRequiredViewAsType(source, R.id.admins_list, "field 'adminsLv'", ListView.class);
    target.noAdminsTv = Utils.findRequiredViewAsType(source, R.id.no_admins, "field 'noAdminsTv'", TextView.class);
    target.addAdminFab = Utils.findRequiredViewAsType(source, R.id.add_admin_fab, "field 'addAdminFab'", FloatingActionButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ManageAdminsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.adminsLv = null;
    target.noAdminsTv = null;
    target.addAdminFab = null;
  }
}
