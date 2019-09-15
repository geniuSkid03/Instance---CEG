// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AdminActivity_ViewBinding implements Unbinder {
  private AdminActivity target;

  private View view7f090047;

  private View view7f090044;

  private View view7f090114;

  private View view7f09004c;

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
    view7f090047 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.add_circular, "field 'circularCv' and method 'onClicked'");
    target.circularCv = Utils.castView(view, R.id.add_circular, "field 'circularCv'", CardView.class);
    view7f090044 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.manage_teams, "field 'manageTeamsCv' and method 'onClicked'");
    target.manageTeamsCv = Utils.castView(view, R.id.manage_teams, "field 'manageTeamsCv'", CardView.class);
    view7f090114 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.admin_manage_card, "field 'manageAdminsCv' and method 'onClicked'");
    target.manageAdminsCv = Utils.castView(view, R.id.admin_manage_card, "field 'manageAdminsCv'", CardView.class);
    view7f09004c = view;
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

    view7f090047.setOnClickListener(null);
    view7f090047 = null;
    view7f090044.setOnClickListener(null);
    view7f090044 = null;
    view7f090114.setOnClickListener(null);
    view7f090114 = null;
    view7f09004c.setOnClickListener(null);
    view7f09004c = null;
  }
}
