package com.vv.syncpoc.network;

import androidx.lifecycle.MutableLiveData;

import com.vv.syncpoc.model.WeatherMapResponseModel;
import com.vv.syncpoc.utils.ApiConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataRepository {

    Retrofit retrofit;

    private static MutableLiveData<WeatherMapResponseModel> data = new MutableLiveData<>();

    public DataRepository() {
//        retrofit = new Retrofit.Builder()
//                .baseUrl(ApiConstants.WEATHER_MAP_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
    }

    public MutableLiveData<WeatherMapResponseModel> getList(double latitude, double longitude) {

//        RetrofitAPIInterface retrofitAPIInterface = retrofit.create(RetrofitAPIInterface.class);
//
//        Call<WeatherMapResponseModel> call = retrofitAPIInterface.retrieveList(latitude, longitude, ApiConstants.API_KEY, ApiConstants.UNIT);
//        call.enqueue(new Callback<WeatherMapResponseModel>() {
//
//            @Override
//            public void onResponse(Call<WeatherMapResponseModel> call, Response<WeatherMapResponseModel> response) {
//                data.setValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<WeatherMapResponseModel> call, Throwable t) {
////                data.setValue(response.body());
//            }
//        });


        return data;
    }
}
