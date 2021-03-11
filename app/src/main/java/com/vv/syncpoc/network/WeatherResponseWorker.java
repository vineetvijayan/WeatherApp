package com.vv.syncpoc.network;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.vv.syncpoc.model.WeatherMapResponseModel;
import com.vv.syncpoc.room.DatabaseClient;
import com.vv.syncpoc.room.WeatherResponseRoomModel;
import com.vv.syncpoc.utils.ApiConstants;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherResponseWorker extends Worker {

    RetrofitAPIInterface retrofitAPIInterface;
    private Context context;

    public WeatherResponseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.WEATHER_MAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitAPIInterface = retrofit.create(RetrofitAPIInterface.class);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {

            String lat = PreferenceManager.getDefaultSharedPreferences(context).getString("latitude", "0");
            String lon = PreferenceManager.getDefaultSharedPreferences(context).getString("longitude", "0");


            Response<WeatherMapResponseModel> response = retrofitAPIInterface.retrieveList(lat, lon, ApiConstants.API_KEY, ApiConstants.UNIT).execute();
            WeatherMapResponseModel weatherMapResponseModel = response.body();

            if(!response.isSuccessful()) {
                return ListenableWorker.Result.failure();
            }

            // save to room db
            WeatherResponseRoomModel model = new WeatherResponseRoomModel();
            model.setId(ApiConstants.API_KEY);
            model.setTemp(weatherMapResponseModel.getMain().getTemp().toString());
            model.setTempMax(weatherMapResponseModel.getMain().getTempMax().toString());
            model.setTempMin(weatherMapResponseModel.getMain().getTempMin().toString());

            Log.d("Worker Response", "Temp - "+ model.getTemp());


            if (DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .weatherDao().getAll() != null && DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .weatherDao().getAll().size() > 0) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .weatherDao()
                        .updateWeatherResponse(model);
            } else {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .weatherDao()
                        .insert(model);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Worker.Result.success();
    }
}
