package com.inspiregeniussquad.handstogether.appStorage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.inspiregeniussquad.handstogether.appStorage.daos.TeamDao;


@Database(entities = {TeamData.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDbs extends RoomDatabase {

    private static AppDbs appDbs;

    public abstract TeamDao teamDao();

    public static AppDbs getTeamDao(Context context) {
        if (appDbs == null) {
            appDbs = Room.databaseBuilder(context.getApplicationContext(),
                    AppDbs.class,
                    "instance-db")
                    .allowMainThreadQueries()
                    .build();
        }
        return appDbs;
    }
}
