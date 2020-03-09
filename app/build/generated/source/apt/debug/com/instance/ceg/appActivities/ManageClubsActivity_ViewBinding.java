// Generated code from Butter Knife. Do not modify!
package com.instance.ceg.appActivities;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.instance.ceg.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ManageClubsActivity_ViewBinding implements Unbinder {
  private ManageClubsActivity target;

  private View view7f090045;

  @UiThread
  public ManageClubsActivity_ViewBinding(ManageClubsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ManageClubsActivity_ViewBinding(final ManageClubsActivity target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.clubsLv = Utils.findRequiredViewAsType(source, R.id.clubs_list, "field 'clubsLv'", ListView.class);
    target.noClubsTv = Utils.findRequiredViewAsType(source, R.id.no_clubs, "field 'noClubsTv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.add_clubs_fab, "field 'addClubFab' and method 'onclicked'");
    target.addClubFab = Utils.castView(view, R.id.add_clubs_fab, "field 'addClubFab'", FloatingActionButton.class);
    view7f090045 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onclicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ManageClubsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.clubsLv = null;
    target.noClubsTv = null;
    target.addClubFab = null;

    view7f090045.setOnClickListener(null);
    view7f090045 = null;
  }
}
