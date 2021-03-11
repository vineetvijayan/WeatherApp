package com.vv.syncpoc.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = WeatherResponseRoomModel.class, version = 1, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
}
