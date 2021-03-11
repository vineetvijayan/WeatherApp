package com.vv.syncpoc.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather_details")
    List<WeatherResponseRoomModel> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherResponseRoomModel weatherResponseRoomModel);

    @Update
    void updateWeatherResponse(WeatherResponseRoomModel weatherResponseRoomModel);
}
