package com.instance.ceg.appStorage;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.instance.ceg.appStorage.daos.TeamDao;


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
