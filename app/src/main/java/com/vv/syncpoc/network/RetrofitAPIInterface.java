package com.vv.syncpoc.network;

import com.vv.syncpoc.model.WeatherMapResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPIInterface {

    @GET("weather")
    Call<WeatherMapResponseModel> retrieveList(@Query("lat") String lat,
                                               @Query("lon") String lon, @Query("appid") String apiKey, @Query("units") String unit );
}
