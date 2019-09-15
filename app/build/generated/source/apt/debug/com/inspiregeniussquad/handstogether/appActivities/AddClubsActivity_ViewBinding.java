// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddClubsActivity_ViewBinding implements Unbinder {
  private AddClubsActivity target;

  @UiThread
  public AddClubsActivity_ViewBinding(AddClubsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AddClubsActivity_ViewBinding(AddClubsActivity target, View source) {
    this.target = target;

    target.addClubsCv = Utils.findRequiredViewAsType(source, R.id.llMainContents, "field 'addClubsCv'", CardView.class);
    target.clubNameEd = Utils.findRequiredViewAsType(source, R.id.club_name, "field 'clubNameEd'", EditText.class);
    target.clubIv = Utils.findRequiredViewAsType(source, R.id.club_image, "field 'clubIv'", ImageView.class);
    target.clubImgChooserIv = Utils.findRequiredViewAsType(source, R.id.club_image_choose, "field 'clubImgChooserIv'", ImageView.class);
    target.saveFab = Utils.findRequiredViewAsType(source, R.id.save_club, "field 'saveFab'", FloatingActionButton.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AddClubsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.addClubsCv = null;
    target.clubNameEd = null;
    target.clubIv = null;
    target.clubImgChooserIv = null;
    target.saveFab = null;
    target.toolbar = null;
  }
}
