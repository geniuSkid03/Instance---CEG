// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.inspiregeniussquad.handstogether.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TeamInfoActivity_ViewBinding implements Unbinder {
  private TeamInfoActivity target;

  @UiThread
  public TeamInfoActivity_ViewBinding(TeamInfoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TeamInfoActivity_ViewBinding(TeamInfoActivity target, View source) {
    this.target = target;

    target.teamNestedScrollView = Utils.findRequiredViewAsType(source, R.id.team_scroll_view, "field 'teamNestedScrollView'", NestedScrollView.class);
    target.teamContentView = Utils.findRequiredViewAsType(source, R.id.team_content_view, "field 'teamContentView'", RelativeLayout.class);
    target.teamLoadingView = Utils.findRequiredViewAsType(source, R.id.team_loading_view, "field 'teamLoadingView'", ShimmerFrameLayout.class);
    target.teamNameTv = Utils.findRequiredViewAsType(source, R.id.team_name, "field 'teamNameTv'", TextView.class);
    target.teamMottoTv = Utils.findRequiredViewAsType(source, R.id.team_motto, "field 'teamMottoTv'", TextView.class);
    target.teamLogoIv = Utils.findRequiredViewAsType(source, R.id.team_log_iv, "field 'teamLogoIv'", CircularImageView.class);
    target.dashboardCv = Utils.findRequiredViewAsType(source, R.id.dashboard, "field 'dashboardCv'", CardView.class);
    target.teamFoundedTv = Utils.findRequiredViewAsType(source, R.id.team_founded, "field 'teamFoundedTv'", TextView.class);
    target.teamMembersCountTv = Utils.findRequiredViewAsType(source, R.id.team_members_count, "field 'teamMembersCountTv'", TextView.class);
    target.teamPostsTv = Utils.findRequiredViewAsType(source, R.id.team_posts_count, "field 'teamPostsTv'", TextView.class);
    target.descCv = Utils.findRequiredViewAsType(source, R.id.desc, "field 'descCv'", CardView.class);
    target.teamDescTv = Utils.findRequiredViewAsType(source, R.id.team_desc, "field 'teamDescTv'", TextView.class);
    target.membersCv = Utils.findRequiredViewAsType(source, R.id.members_card, "field 'membersCv'", CardView.class);
    target.teamMembersRv = Utils.findRequiredViewAsType(source, R.id.team_members_rv, "field 'teamMembersRv'", RecyclerView.class);
    target.teamPostsCv = Utils.findRequiredViewAsType(source, R.id.team_posts, "field 'teamPostsCv'", CardView.class);
    target.teamPostsRv = Utils.findRequiredViewAsType(source, R.id.team_posts_rv, "field 'teamPostsRv'", RecyclerView.class);
    target.teamMemberLoadingView = Utils.findRequiredViewAsType(source, R.id.team_member_loading_view, "field 'teamMemberLoadingView'", ShimmerFrameLayout.class);
    target.noTeamMemberAvailLayout = Utils.findRequiredViewAsType(source, R.id.no_team_mem_view, "field 'noTeamMemberAvailLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TeamInfoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.teamNestedScrollView = null;
    target.teamContentView = null;
    target.teamLoadingView = null;
    target.teamNameTv = null;
    target.teamMottoTv = null;
    target.teamLogoIv = null;
    target.dashboardCv = null;
    target.teamFoundedTv = null;
    target.teamMembersCountTv = null;
    target.teamPostsTv = null;
    target.descCv = null;
    target.teamDescTv = null;
    target.membersCv = null;
    target.teamMembersRv = null;
    target.teamPostsCv = null;
    target.teamPostsRv = null;
    target.teamMemberLoadingView = null;
    target.noTeamMemberAvailLayout = null;
  }
}
