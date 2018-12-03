package com.inspiregeniussquad.handstogether.appViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.inspiregeniussquad.handstogether.R;

public class NewsFeedItemsLayout extends RelativeLayout {

    public NewsFeedItemsLayout(Context context) {
        super(context);
        initViews(context);
    }

    public NewsFeedItemsLayout(Context context, AttributeSet attr) {
        super(context, attr);
        initViews(context);
    }

    public NewsFeedItemsLayout(Context context, AttributeSet attributeSet, int theme) {
        super(context, attributeSet, theme);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.news_feed_items_new, this, true);
        setClipToPadding(false);
    }


}
