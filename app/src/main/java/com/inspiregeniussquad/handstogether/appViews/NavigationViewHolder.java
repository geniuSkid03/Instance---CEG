package com.inspiregeniussquad.handstogether.appViews;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.inspiregeniussquad.handstogether.appAdapters.NavMenuAdapter;
import com.inspiregeniussquad.handstogether.appData.NavigationItem;

public class NavigationViewHolder extends RecyclerView.ViewHolder {


    protected NavigationItem navigationItem;
    protected View itemView;
    protected int position;
    protected NavMenuAdapter.NavOnItemClickListener navOnItemClickListener;

    public NavigationViewHolder(View itemView, NavMenuAdapter.NavOnItemClickListener navOnItemClickListener) {
        super(itemView);
        this.itemView = itemView;
        this.navOnItemClickListener = navOnItemClickListener;
    }

    public void setNavigationItemData(int position, NavigationItem navigationItem){
        this.position = position;
        this.navigationItem = navigationItem;
    }

    public void setUpView(){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navOnItemClickListener.onItemClicked(position);
            }
        });
    }
}

