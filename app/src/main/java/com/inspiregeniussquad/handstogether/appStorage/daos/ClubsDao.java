package com.inspiregeniussquad.handstogether.appStorage.daos;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.inspiregeniussquad.handstogether.appData.Clubs;
import com.inspiregeniussquad.handstogether.appStorage.TeamData;

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
