// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewsItemViewActivity_ViewBinding implements Unbinder {
  private NewsItemViewActivity target;

  @UiThread
  public NewsItemViewActivity_ViewBinding(NewsItemViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NewsItemViewActivity_ViewBinding(NewsItemViewActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar_layout, "field 'appBarLayout'", AppBarLayout.class);
    target.collapsingToolbarLayout = Utils.findRequiredViewAsType(source, R.id.collapsing_toolbar, "field 'collapsingToolbarLayout'", CollapsingToolbarLayout.class);
    target.posterIv = Utils.findRequiredViewAsType(source, R.id.event_poster, "field 'posterIv'", ImageView.class);
    target.nestedScrollView = Utils.findRequiredViewAsType(source, R.id.nested_scrollview, "field 'nestedScrollView'", NestedScrollView.class);
    target.infoDescTv = Utils.findRequiredViewAsType(source, R.id.desc_view, "field 'infoDescTv'", TextView.class);
    target.titleTv = Utils.findRequiredViewAsType(source, R.id.title, "field 'titleTv'", TextView.class);
    target.dateTv = Utils.findRequiredViewAsType(source, R.id.date, "field 'dateTv'", TextView.class);
    target.timeTv = Utils.findRequiredViewAsType(source, R.id.time, "field 'timeTv'", TextView.class);
    target.teamLogoIv = Utils.findRequiredViewAsType(source, R.id.team_logo, "field 'teamLogoIv'", CircularImageView.class);
    target.teamLogo2Iv = Utils.findRequiredViewAsType(source, R.id.team_logo2, "field 'teamLogo2Iv'", CircularImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NewsItemViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.appBarLayout = null;
    target.collapsingToolbarLayout = null;
    target.posterIv = null;
    target.nestedScrollView = null;
    target.infoDescTv = null;
    target.titleTv = null;
    target.dateTv = null;
    target.timeTv = null;
    target.teamLogoIv = null;
    target.teamLogo2Iv = null;
  }
}