package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.TeamMembers;

import java.util.ArrayList;

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

        TeamMem(View itemView) {
            super(itemView);

            memberIv = itemView.findViewById(R.id.member_iv);
            memberName = itemView.findViewById(R.id.member_name);
            memberPosition = itemView.findViewById(R.id.member_pos);
        }

        void setTeamMembers(TeamMembers teamMembers) {
            memberPosition.setText(teamMembers.getTeamMemberPosition());
            memberName.setText(teamMembers.getTeamMemberName());
            memberIv.setText(teamMembers.getTeamMemberName().substring(0, 1));
        }
    }
}
