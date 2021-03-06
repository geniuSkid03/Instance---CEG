package com.instance.ceg.appAdapters;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instance.ceg.R;
import com.instance.ceg.appData.TeamMembers;

import java.util.ArrayList;
import java.util.Random;

public class TeamMembersAdapter extends RecyclerView.Adapter<TeamMembersAdapter.TeamMem> {

    private Context context;
    private ArrayList<TeamMembers> teamMembersArrayList;


    public TeamMembersAdapter(Context context, ArrayList<TeamMembers> teamMembersArrayList) {
        this.context = context;
        this.teamMembersArrayList = teamMembersArrayList;
    }

    @NonNull
    @Override
    public TeamMem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TeamMem (LayoutInflater.from(context).inflate(R.layout.team_members_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMem teamMem, int i) {
        teamMem.setTeamMembers(teamMembersArrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return teamMembersArrayList.size();
    }


    class TeamMem extends RecyclerView.ViewHolder {

        TextView memberName, memberPosition, memberIv;
        LinearLayout memberBgLayout;

        TeamMem(View itemView) {
            super(itemView);

            memberIv = itemView.findViewById(R.id.member_iv);
            memberName = itemView.findViewById(R.id.member_name);
            memberPosition = itemView.findViewById(R.id.member_pos);
            memberBgLayout = itemView.findViewById(R.id.member_bg_iv);
        }

        void setTeamMembers(TeamMembers teamMembers) {
            memberPosition.setText(teamMembers.getTeamMemberPosition());
            memberName.setText(teamMembers.getTeamMemberName());
            memberIv.setText(teamMembers.getTeamMemberName().substring(0, 1));

            Random random = new Random();
            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            memberIv.setBackgroundColor(color);
            memberBgLayout.setBackgroundColor(color);
        }
    }
}
