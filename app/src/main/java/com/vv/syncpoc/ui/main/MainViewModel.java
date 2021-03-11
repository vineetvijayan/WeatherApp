package com.vv.syncpoc.ui.main;

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
import com.vv.syncpoc.network.DataRepository;
import com.vv.syncpoc.network.WeatherResponseWorker;
import com.vv.syncpoc.room.DatabaseClient;
import com.vv.syncpoc.room.WeatherResponseRoomModel;

import java.util.concurrent.TimeUnit;

public class MainViewModel extends AndroidViewModel {
    public MutableLiveData<WeatherMapResponseModel> weatherMapResponseModelMutableLiveData;
    private WorkManager workManager;
    public MutableLiveData<WeatherResponseRoomModel> weatherResponseRoomModelMutableLiveData = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchList(double latitude, double longitude) {
        weatherMapResponseModelMutableLiveData = new DataRepository().getList(latitude, longitude);
    }

    public LiveData<WeatherMapResponseModel> getListObservable() {
        return weatherMapResponseModelMutableLiveData;
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

//        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicWorkRequest.getId())
//                .observe(this, Observer { workInfo ->
//            // Check if the current work's state is "successfully finished"
//            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
//                displayImage(workInfo.outputData.getString(KEY_IMAGE_URI))
//            }
//        });
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