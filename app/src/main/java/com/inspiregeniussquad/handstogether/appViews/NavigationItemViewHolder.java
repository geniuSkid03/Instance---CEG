package com.inspiregeniussquad.handstogether.appViews;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.NavMenuAdapter;

class NavigationItemViewHolder extends NavigationViewHolder {

    public NavigationItemViewHolder(View itemView, NavMenuAdapter.NavOnItemClickListener navOnItemClickListener) {
        super(itemView, navOnItemClickListener);
    }

    @Override
    public void setUpView() {
        super.setUpView();


        View selectedImage = null;
        if ((selectedImage = itemView.findViewById(R.id.selected_image)) != null) {
            selectedImage.setVisibility(((NavigationTwo) navigationItem).isSelected() ? View.VISIBLE : View.INVISIBLE);
        }

        ImageView image = null;
        if ((image = itemView.findViewById(R.id.image)) != null) {
            image.setImageResource(((NavigationTwo) navigationItem).getImgResId());
        }

        TextView nameTv = null;
        if ((nameTv = itemView.findViewById(R.id.text)) != null) {
            nameTv.setText(((NavigationTwo) navigationItem).getName());
        }

    }
}

