package com.instance.ceg.appStorage.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.instance.ceg.appStorage.TeamData;

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
