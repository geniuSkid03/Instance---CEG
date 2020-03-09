package com.instance.ceg.appAdapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instance.ceg.R;
import com.instance.ceg.appData.Team;

import java.util.ArrayList;

public class TeamsItemAdapter extends RecyclerView.Adapter<TeamsItemAdapter.TeamsItems> {

    private Context context;
    private ArrayList<Team> teamsItemsArrayList;
    private TeamClickListener teamClickListener;

    public TeamsItemAdapter(Context context, ArrayList<Team> teamsItemsArrayList, TeamClickListener teamClickListener) {
        this.context = context;
        this.teamsItemsArrayList = teamsItemsArrayList;
        this.teamClickListener = teamClickListener;
    }

    @NonNull
    @Override
    public TeamsItems onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TeamsItems(LayoutInflater.from(context).inflate(R.layout.teams_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsItems teamsItems, int i) {
        teamsItems.bindViews(teamsItemsArrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return teamsItemsArrayList.size();
    }

    class TeamsItems extends RecyclerView.ViewHolder {

        CardView teamItemView;
        ImageView logoIv;
        TextView nameTv, mottoTv;

        TeamsItems(View view) {
            super(view);

            init(view);
        }

        void init(View view) {
            logoIv = view.findViewById(R.id.team_logo);
            nameTv = view.findViewById(R.id.team_name);
            mottoTv = view.findViewById(R.id.team_motto);
            teamItemView = view.findViewById(R.id.team_item_view);
        }

        void bindViews(final Team team) {
            Glide.with(context).load(team.gettLogo()).into(logoIv);

            nameTv.setText(team.gettName());
            mottoTv.setText(team.gettMotto());

            teamItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    teamClickListener.onClicked(team);
                }
            });
        }
    }

    public interface TeamClickListener {
        void onClicked(Team team);
    }
}
