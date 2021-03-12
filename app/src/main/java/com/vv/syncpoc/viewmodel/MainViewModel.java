package com.vv.syncpoc.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.vv.syncpoc.model.WeatherMapResponseModel;
import com.vv.syncpoc.network.WeatherResponseWorker;
import com.vv.syncpoc.room.DatabaseClient;
import com.vv.syncpoc.room.WeatherResponseRoomModel;

import java.util.concurrent.TimeUnit;

public class MainViewModel extends AndroidViewModel {
    private WorkManager workManager;
    public MutableLiveData<WeatherResponseRoomModel> weatherResponseRoomModelMutableLiveData = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<WeatherResponseRoomModel> getWeatherDataFromDBObservable() {
        return weatherResponseRoomModelMutableLiveData;
    }

    public void callWeatherApi(LifecycleOwner lifecycleOwner) {

        workManager = WorkManager.getInstance(getApplication());

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();


        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(WeatherResponseWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .addTag("weather_periodic_request")
                        .build();

        workManager.enqueue(periodicWorkRequest);

        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.getId()).observe(lifecycleOwner, workInfo -> {
            if (workInfo.getState().equals(WorkInfo.State.ENQUEUED)) {
                Log.d("Work status", " " + workInfo.getState().name());
                // trigger db call to update UI
                getDataFromDB();
            }
        });
    }

    public void getDataFromDB() {
        WeatherResponseRoomModel model;
        if (DatabaseClient.getInstance(getApplication()).getAppDatabase()
                .weatherDao()
                .getAll() != null && DatabaseClient.getInstance(getApplication()).getAppDatabase()
                .weatherDao()
                .getAll().size() > 0) {
            model = DatabaseClient.getInstance(getApplication()).getAppDatabase()
                    .weatherDao()
                    .getAll().get(0);
            weatherResponseRoomModelMutableLiveData.setValue(model);

        }
    }
}