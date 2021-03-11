package com.vv.syncpoc.room;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

public class DatabaseClient extends Application {

    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private WeatherDatabase weatherDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        weatherDatabase = Room.databaseBuilder(mCtx, WeatherDatabase.class, "weatherDB").allowMainThreadQueries().build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public WeatherDatabase getAppDatabase() {
        return weatherDatabase;
    }
}
