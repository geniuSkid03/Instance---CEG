package com.instance.ceg.appAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instance.ceg.R;
import com.instance.ceg.appData.CircularDataItems;
import com.instance.ceg.appUtils.AppHelper;
import com.instance.ceg.appViews.CircularFeedLayout;

import java.util.ArrayList;

public class CircularFeedAdapter extends RecyclerView.Adapter<CircularFeedAdapter.CircularFeedView> {

    private ArrayList<CircularDataItems> circularDataItemsArrayList;
    private Context context;
    private CircularCallBack circularCallBack;

    public CircularFeedAdapter(Context context, ArrayList<CircularDataItems> circularDataItemsArrayList,
                               CircularCallBack circularCallBack) {
        this.context = context;
        this.circularDataItemsArrayList = circularDataItemsArrayList;
        this.circularCallBack = circularCallBack;
    }

    @NonNull
    @Override
    public CircularFeedView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CircularFeedView(new CircularFeedLayout(context));
    }

    @Override
    public void onBindViewHolder(@NonNull CircularFeedView holder, int position) {
        holder.setCicularFeedView(position);
    }

    @Override
    public int getItemCount() {
        return circularDataItemsArrayList.size();
    }

    class CircularFeedView extends RecyclerView.ViewHolder {

        private View itemView;
        private int position;

        private TextView titleTv, descTv, dateTimeTv, readMoreTv;
        private ImageView circularImgIv;

        CircularFeedView(View view) {
            super(view);
            itemView = view;

            titleTv = itemView.findViewById(R.id.name);
            dateTimeTv = itemView.findViewById(R.id.read_more);
            readMoreTv = itemView.findViewById(R.id.read_more_tv);
            descTv = itemView.findViewById(R.id.desc);
            circularImgIv = itemView.findViewById(R.id.event_poster);
        }

        void setCicularFeedView(int position) {
            setView(circularDataItemsArrayList.get(position));
            this.position = position;
        }

        private void setView(CircularDataItems circularDataItems) {
            titleTv.setText(circularDataItems.getcTitle());
            dateTimeTv.setText(String.format("%s  %s", circularDataItems.getpDate(), circularDataItems.getpTime()));
            descTv.setText(circularDataItems.getcDesc());

            if (circularDataItems.getCircularImgPath() != null) {
                Glide.with(context).load(circularDataItems.getCircularImgPath()).into(circularImgIv);
            } else {
//                if(circularDataItems.getPdfPath() != null) {
//                    getPdfPathzBitmap bitmap = AppHelper.getPreviewImageFromPdf(context, Uri.parse(circularDataItems.getPdfPath()));
//                    Glide.with(context).load(bitmap != null ? bitmap : R.drawable.pdf_icon).into(circularImgIv);
//                } else {
//                    Glide.with(context).load(R.drawable.pdf_icon).into(circularImgIv);
//                }
                Glide.with(context).load(R.drawable.pdf_icon).into(circularImgIv);
            }

            circularImgIv.setOnClickListener(v -> circularCallBack.onItemClicked(position, circularImgIv));
            readMoreTv.setOnClickListener(view -> circularCallBack.onReadMoreClick(circularDataItems.getcDesc()));
        }
    }

    public interface CircularCallBack {
        void onItemClicked(int position, ImageView imageView);
        void onReadMoreClick(String description);
    }
}
