package com.inspiregeniussquad.handstogether.appAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;

public class PermissionsAdapter  extends BaseAdapter {

    private int[] icon;
    private String[] title, hint;
    private Context context;

    private LayoutInflater layoutInflater;


    public PermissionsAdapter(Context context, int[] icon, String[] title, String[] hint) {
        this.context = context;
        this.icon = icon;
        this.title = title;
        this.hint = hint;

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return icon.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.permissions_layout, null);

        ((ImageView) convertView.findViewById(R.id.permissions_icon)).setImageResource(icon[position]);
        ((TextView) convertView.findViewById(R.id.permissions_title)).setText(title[position]);
        ((TextView) convertView.findViewById(R.id.permissions_hint)).setText(hint[position]);

        return convertView;
    }
}