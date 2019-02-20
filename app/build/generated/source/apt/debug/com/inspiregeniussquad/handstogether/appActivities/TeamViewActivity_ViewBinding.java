// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TeamViewActivity_ViewBinding implements Unbinder {
  private TeamsActivity target;

  @UiThread
  public TeamViewActivity_ViewBinding(TeamsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TeamViewActivity_ViewBinding(TeamsActivity target, View source) {
    this.target = target;

    target.teamsRv = Utils.findRequiredViewAsType(source, R.id.teams_list_rv, "field 'teamsRv'", RecyclerView.class);
    target.noClubsLayout = Utils.findRequiredViewAsType(source, R.id.no_clubs_view, "field 'noClubsLayout'", LinearLayout.class);
    target.loadingView = Utils.findRequiredViewAsType(source, R.id.team_loading_view, "field 'loadingView'", ShimmerFrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TeamsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.teamsRv = null;
    target.noClubsLayout = null;
    target.loadingView = null;
  }
}
