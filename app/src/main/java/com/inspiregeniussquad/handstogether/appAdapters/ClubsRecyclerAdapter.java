package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Clubs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ClubsRecyclerAdapter extends RecyclerView.Adapter<ClubsRecyclerAdapter.ClubsItemViewHolder> {

    private Context context;
    private ArrayList<Clubs> clubsArrayList;

    private ImageLoader imageLoader;


    private ClubClickListener clubClickListener;

    public ClubsRecyclerAdapter(Context context, ArrayList<Clubs> clubsArrayList, ClubClickListener clubClickListener) {
        this.context = context;
        this.clubsArrayList = clubsArrayList;
        this.clubClickListener = clubClickListener;

        imageLoader = ImageLoader.getInstance();
    }

    @NonNull
    @Override
    public ClubsItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.club_item_view, viewGroup, false);
        return new ClubsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubsItemViewHolder clubsItemViewHolder, int i) {
        clubsItemViewHolder.loadViews(clubsArrayList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return clubsArrayList.size();
    }

    class ClubsItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView clubIconIv;
        private TextView clubsNameTv;
        private CardView clubRootLayout;

        ClubsItemViewHolder(View view) {
            super(view);

            setUpView(view);
        }

        private void setUpView(View view) {
            clubIconIv = view.findViewById(R.id.club_logo);
            clubsNameTv = view.findViewById(R.id.club_name_);
            clubRootLayout = view.findViewById(R.id.club_view_root);
        }

        void loadViews(Clubs clubs, final int position) {

            clubsNameTv.setText(clubs.getClubsName());

            clubRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clubClickListener.onClicked(position);
                }
            });

            File posterImage = DiskCacheUtils.findInCache(clubs.getClubsImgUrl(), imageLoader.getDiskCache());
            if (posterImage != null && posterImage.exists()) {
                Picasso.get().load(posterImage).fit().into(clubIconIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        clubIconIv.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        clubIconIv.setVisibility(View.GONE);
                    }
                });
            } else {
                imageLoader.loadImage(clubs.getClubsImgUrl(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        clubIconIv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        clubIconIv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        clubIconIv.setVisibility(View.VISIBLE);
                        Picasso.get().load(imageUri).fit().into(clubIconIv);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
        }
    }

    public interface ClubClickListener {
        void onClicked(int position);
    }

}
