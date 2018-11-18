package com.inspiregeniussquad.handstogether.appViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.inspiregeniussquad.handstogether.R;

public class NewsUpdateLayout extends RelativeLayout {
    public NewsUpdateLayout(Context context) {
        super(context);
        initViews(context);
    }

    public NewsUpdateLayout(Context context, AttributeSet attr) {
        super(context, attr);
        initViews(context);
    }

    public NewsUpdateLayout(Context context, AttributeSet attributeSet, int theme) {
        super(context, attributeSet, theme);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.updated_news_item, this, true);
        setClipToPadding(false);
    }

}
