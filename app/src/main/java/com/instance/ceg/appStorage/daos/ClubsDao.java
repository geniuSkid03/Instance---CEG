package com.instance.ceg.appStorage.daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.instance.ceg.appData.Clubs;
import com.instance.ceg.appStorage.TeamData;

import java.util.ArrayList;

public interface ClubsDao {

    @Insert
    void insert(Clubs clubs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<Clubs> clubsArrayList);

    @Query("SELECT * from ClubsData WHERE clubsId = :clubsId")
    Clubs getTeamInfo(String clubsId);

    @Delete
    void deleteTeam(Clubs clubs);

    @Update
    void updateTeam(Clubs clubs);

    @Query("DELETE from ClubsData")
    void deleteAllTeams();

    @Query("Select * FROM ClubsData")
    TeamData[] loadAll();
}
