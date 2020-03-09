// Generated code from Butter Knife. Do not modify!
package com.instance.ceg.appActivities;

import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.instance.ceg.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WebActivity_ViewBinding implements Unbinder {
  private WebActivity target;

  @UiThread
  public WebActivity_ViewBinding(WebActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WebActivity_ViewBinding(WebActivity target, View source) {
    this.target = target;

    target.webView = Utils.findRequiredViewAsType(source, R.id.webview, "field 'webView'", WebView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.webProgress, "field 'progressBar'", ProgressBar.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WebActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.webView = null;
    target.progressBar = null;
    target.toolbar = null;
  }
}
