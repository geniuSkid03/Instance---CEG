// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AdminActivity_ViewBinding implements Unbinder {
  private AdminActivity target;

  private View view2131296290;

  private View view2131296287;

  private View view2131296487;

  private View view2131296295;

  @UiThread
  public AdminActivity_ViewBinding(AdminActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AdminActivity_ViewBinding(final AdminActivity target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    view = Utils.findRequiredView(source, R.id.add_news, "field 'newsCv' and method 'onClicked'");
    target.newsCv = Utils.castView(view, R.id.add_news, "field 'newsCv'", CardView.class);
    view2131296290 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.add_circular, "field 'circularCv' and method 'onClicked'");
    target.circularCv = Utils.castView(view, R.id.add_circular, "field 'circularCv'", CardView.class);
    view2131296287 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.manage_teams, "field 'manageTeamsCv' and method 'onClicked'");
    target.manageTeamsCv = Utils.castView(view, R.id.manage_teams, "field 'manageTeamsCv'", CardView.class);
    view2131296487 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.admin_manage_card, "field 'manageAdminsCv' and method 'onClicked'");
    target.manageAdminsCv = Utils.castView(view, R.id.admin_manage_card, "field 'manageAdminsCv'", CardView.class);
    view2131296295 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AdminActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.newsCv = null;
    target.circularCv = null;
    target.manageTeamsCv = null;
    target.manageAdminsCv = null;

    view2131296290.setOnClickListener(null);
    view2131296290 = null;
    view2131296287.setOnClickListener(null);
    view2131296287 = null;
    view2131296487.setOnClickListener(null);
    view2131296487 = null;
    view2131296295.setOnClickListener(null);
    view2131296295 = null;
  }
}
