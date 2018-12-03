package com.inspiregeniussquad.handstogether.appStorage.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.inspiregeniussquad.handstogether.appStorage.TeamData;

import java.util.ArrayList;

@Dao
public interface TeamDao {

    @Insert
    void insert(TeamData teamData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<TeamData> teamDataArrayList);

    @Query("SELECT * from TeamData WHERE teamName = :teamName")
    TeamData getTeamInfo(String teamName);

    @Delete
    void deleteTeam(TeamData teamData);

    @Update
    void updateTeam(TeamData teamData);

    @Query("DELETE from TeamData")
    void deleteAllTeams();

    @Query("Select * FROM TeamData")
    TeamData[] loadAll();
}
