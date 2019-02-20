package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Clubs;

import java.util.ArrayList;

public class ClubsAdapter extends BaseAdapter {

    private ArrayList<Clubs> clubsArrayList;
    private Context context;

    private OnOptionsSelected onOptionsSelected;

    public ClubsAdapter(Context context, ArrayList<Clubs> clubsArrayList, OnOptionsSelected onOptionsSelected) {
        this.context = context;
        this.clubsArrayList = clubsArrayList;
        this.onOptionsSelected = onOptionsSelected;
    }

    @Override
    public int getCount() {
        return clubsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.clubs_item_view, null);
        }

        ((TextView) convertView.findViewById(R.id.club_name)).setText(clubsArrayList.get(position).getClubsName());

        Glide.with(context).load(clubsArrayList.get(position).getClubsImgUrl())
                .into(((ImageView) convertView.findViewById(R.id.club_icon)));

        return convertView;
    }

    public interface OnOptionsSelected {
        void onClicked(int position, View view);
    }

}
