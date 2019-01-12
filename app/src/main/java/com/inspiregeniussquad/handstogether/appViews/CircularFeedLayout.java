package com.inspiregeniussquad.handstogether.appViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.inspiregeniussquad.handstogether.R;

public class CircularFeedLayout extends RelativeLayout {

    public CircularFeedLayout(Context context) {
        super(context);
        setUpView(context);
    }

    public CircularFeedLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setUpView(context);
    }

    public CircularFeedLayout(Context context, AttributeSet attributeSet, int resId) {
        super(context, attributeSet, resId);
        setUpView(context);
    }

    private void setUpView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.circular_item_view_new, this, true);
        setClipToPadding(false);
    }
}
