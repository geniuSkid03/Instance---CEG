/*
package com.instance.ceg.appViews;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.instance.ceg.R;
import com.instance.ceg.appAdapters.NavMenuAdapter;

class NavigationHeadViewHolder extends NavigationViewHolder {

    public NavigationHeadViewHolder(View itemView, NavMenuAdapter.NavOnItemClickListener navOnItemClickListener) {
        super(itemView, navOnItemClickListener);
    }

    @Override
    public void setUpView() {
        super.setUpView();

        if(itemView != null){
            itemView.setBackgroundColor(itemView.isSelected() ? ContextCompat.getColor(itemView.getContext(), R.color.app_grey) :
                    ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
        }

        ImageView profileIv = null;
        if((profileIv = itemView.findViewById(R.id.edit)) != null) {
            profileIv.setImageResource();
        }

        TextView nameTv = null;
        if((nameTv = itemView.findViewById(R.id.nameTextv)) != null){
            nameTv.setText(((NavigationOne) navigationItem).getName());
        }

        ImageView editIv = null;
        if((editIv = itemView.findViewById(R.id.edit)) != null) {
            editIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public interface EditClickListener {
        void onClicked();
    }
}*/
