package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Admin;

import java.util.ArrayList;

public class AdminsAdapter extends BaseAdapter {

    private ArrayList<Admin> adminArrayList;
    private Context context;

    private OnOptionsSelected onOptionsSelected;

    public AdminsAdapter(Context context, ArrayList<Admin> adminArrayList, OnOptionsSelected onOptionsSelected) {
        this.context = context;
        this.adminArrayList = adminArrayList;
        this.onOptionsSelected = onOptionsSelected;
    }

    @Override
    public int getCount() {
        return adminArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.admin_item_view, null);
        }

        ((TextView) view.findViewById(R.id.name)).setText(adminArrayList.get(i).getName());
        ((TextView) view.findViewById(R.id.mobile)).setText(adminArrayList.get(i).getMobile());
        ((TextView) view.findViewById(R.id.designation)).setText(adminArrayList.get(i).getPosition());

        ((ImageView) view.findViewById(R.id.more_options)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsSelected.onClicked(i, view);
            }
        });

        return view;
    }

    public interface OnOptionsSelected {
        void onClicked(int position, View view);
    }
}
