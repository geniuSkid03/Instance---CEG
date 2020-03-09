// Generated code from Butter Knife. Do not modify!
package com.instance.ceg.appActivities;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.instance.ceg.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TeamsActivity_ViewBinding implements Unbinder {
  private TeamsActivity target;

  @UiThread
  public TeamsActivity_ViewBinding(TeamsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TeamsActivity_ViewBinding(TeamsActivity target, View source) {
    this.target = target;

    target.teamsRv = Utils.findRequiredViewAsType(source, R.id.teams_list_rv, "field 'teamsRv'", RecyclerView.class);
    target.noClubsLayout = Utils.findRequiredViewAsType(source, R.id.no_clubs_view, "field 'noClubsLayout'", LinearLayout.class);
    target.loadingView = Utils.findRequiredViewAsType(source, R.id.team_loading_view, "field 'loadingView'", ShimmerFrameLayout.class);
    target.clubNameTv = Utils.findRequiredViewAsType(source, R.id.club_name_text, "field 'clubNameTv'", TextView.class);
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
    target.clubNameTv = null;
  }
}
