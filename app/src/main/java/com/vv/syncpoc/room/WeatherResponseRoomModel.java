package com.vv.syncpoc.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather_details")
public class WeatherResponseRoomModel {

    @PrimaryKey
    @NonNull
    public String id;
    @ColumnInfo(name = "temp")
    private String temp;
    @ColumnInfo(name = "max_temp")
    private String tempMax;
    @ColumnInfo(name = "min_temp")
    private String tempMin;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }
}
