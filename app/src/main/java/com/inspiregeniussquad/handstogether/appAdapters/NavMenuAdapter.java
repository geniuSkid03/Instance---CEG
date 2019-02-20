package com.inspiregeniussquad.handstogether.appAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.NavMenu;

import java.util.ArrayList;

public class NavMenuAdapter extends RecyclerView.Adapter<NavMenuAdapter.NavigationViewHolder> {

    private Context context;
    private ArrayList<NavMenu> navigationItemArrayList;
    private LayoutInflater layoutInflater;
    private NavOnItemClickListener navOnItemClickListener;
    private int selectedPosition = -1;


    public NavMenuAdapter(Context context, ArrayList<NavMenu> navigationItemArrayList,
                          NavOnItemClickListener navOnItemClickListener) {
        this.context = context;
        this.navigationItemArrayList = navigationItemArrayList;
        this.navOnItemClickListener = navOnItemClickListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NavigationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.list_group_header, viewGroup, false);
        return new NavigationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationViewHolder navigationViewHolder, @SuppressLint("RecyclerView") final int i) {
        navigationViewHolder.menuTitleTv.setText(navigationItemArrayList.get(i).getMenuTitle());

        if (selectedPosition == i) {
            navigationViewHolder.menuIconIv.setImageResource(navigationItemArrayList.get(i).getSelectedIcon());
            navigationViewHolder.menuIconIv.setColorFilter(ContextCompat.getColor(context, R.color.white));
            navigationViewHolder.menuTitleTv.setTextColor(ContextCompat.getColor(context, R.color.white));
            navigationViewHolder.selectedView.setBackgroundColor(ContextCompat.getColor(context, R.color.secondaryDarkColor));
        } else {
            navigationViewHolder.menuIconIv.setImageResource(navigationItemArrayList.get(i).getUnSelectedIcon());
            navigationViewHolder.menuIconIv.setColorFilter(ContextCompat.getColor(context, R.color.app_grey));
            navigationViewHolder.menuTitleTv.setTextColor(ContextCompat.getColor(context, R.color.app_grey));
            navigationViewHolder.selectedView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        navigationViewHolder.navMenuLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navOnItemClickListener.onItemClicked(i);
            }
        });
    }

    public void setSelectedPosition(int position) {
        notifyItemChanged(position);
        selectedPosition = position;
        notifyItemChanged(selectedPosition);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return navigationItemArrayList.size();
    }

    public interface NavOnItemClickListener {
        void onItemClicked(int position);
    }

    class NavigationViewHolder extends RecyclerView.ViewHolder {

        private TextView menuTitleTv;
        private ImageView menuIconIv;
        private LinearLayout selectedView;
        private LinearLayout navMenuLyt;

        NavigationViewHolder(View itemView) {
            super(itemView);
            setUpView(itemView);
        }

        private void setUpView(View view) {
            menuTitleTv = view.findViewById(R.id.list_header_tag);
            menuIconIv = view.findViewById(R.id.list_header_icon);
            selectedView = view.findViewById(R.id.selected_header_view);
            navMenuLyt = view.findViewById(R.id.header_root);
        }
    }
}
