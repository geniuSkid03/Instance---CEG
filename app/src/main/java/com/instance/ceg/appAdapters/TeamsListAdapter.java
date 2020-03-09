package com.instance.ceg.appAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instance.ceg.R;
import com.instance.ceg.appData.Team;
import com.instance.ceg.appViews.CircularImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TeamsListAdapter extends RecyclerView.Adapter<TeamsListAdapter.TeamsList> {

    private ArrayList<Team> teamArrayList;
    private Context context;
    private TeamClickListener teamClickListener;

//    private ImageLoader imageLoader;


    public TeamsListAdapter(Context context, ArrayList<Team> teamArrayList, TeamClickListener teamClickListener){
        this.context = context;
        this.teamArrayList = teamArrayList;
        this.teamClickListener =  teamClickListener;

//        imageLoader = ImageLoader.getInstance();
    }


    @NonNull
    @Override
    public TeamsList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teams_list_item, parent, false);
        return new TeamsList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsList holder, int position) {
        holder.setTeams(teamArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return teamArrayList.size();
    }

    class TeamsList extends RecyclerView.ViewHolder {

        private CardView teamCv;
        private CircularImageView teamLogoIv, logoPlaceHolderIv;
        private TextView teamNameTv, teamMottoTv;

        TeamsList(View itemView) {
            super(itemView);

            teamCv = itemView.findViewById(R.id.team_item_view);
            teamLogoIv = itemView.findViewById(R.id.team_logo);
//            logoPlaceHolderIv = itemView.findViewById(R.id.team_logo_placeholder);
            teamNameTv = itemView.findViewById(R.id.team_name);
            teamMottoTv = itemView.findViewById(R.id.team_motto);

        }

        void setTeams(final Team teams) {

            Glide.with(context).load(teams.gettLogo()).into(teamLogoIv);
//
//            File logoImage = DiskCacheUtils.findInCache(teams.getTeamLogoUri(), imageLoader.getDiskCache());
//            if(logoImage != null && logoImage.exists()) {
//                Picasso.get().load(logoImage).fit().into(teamLogoIv, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        logoPlaceHolderIv.setVisibility(View.GONE);
//                        teamLogoIv.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                });
//            } else {
//                imageLoader.loadImage(teams.getTeamLogoUri(), new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        logoPlaceHolderIv.setVisibility(View.GONE);
//                        teamLogoIv.setVisibility(View.VISIBLE);
//                        Picasso.get().load(imageUri).fit().into(teamLogoIv);
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String imageUri, View view) {
//
//                    }
//                });
//            }

            teamNameTv.setText(teams.gettName());
            teamMottoTv.setText(teams.gettMotto());

            Glide.with(context).load(teams.gettLogo()).into(teamLogoIv);

            teamCv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    teamClickListener.onTeamClicked(teams);
                }
            });
        }

    }

    public interface TeamClickListener {
        void onTeamClicked(Team team);
    }
}
