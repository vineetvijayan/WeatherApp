package com.vv.syncpoc.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.vv.syncpoc.R;
import com.vv.syncpoc.databinding.MainActivityBinding;
import com.vv.syncpoc.room.DatabaseClient;
import com.vv.syncpoc.viewmodel.MainViewModel;
import com.vv.syncpoc.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private MainViewModel mViewModel;
    private boolean weatherServiceTriggered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        // build db
        DatabaseClient.getInstance(this);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);

        mViewModel.getDataFromDB();

//        setLocationParams();
        getLocation();
        observeWeatherRoomData();
    }

//    private void setLocationParams() {
//        //Instantiating the Location request and setting the priority and the interval I need to update the location.
//        locationRequest = locationRequest.create();
//        locationRequest.setInterval(100);
//        locationRequest.setFastestInterval(50);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult != null) {
//                    if (locationResult == null) {
//                        return;
//                    }
//                    //Showing the latitude, longitude and accuracy on the home screen.
//                    for (Location location : locationResult.getLocations()) {
//                        Toast.makeText(MainActivity.this, "" + location.getLatitude() + " - " + location.getLongitude(), Toast.LENGTH_SHORT).show();
//                        Log.d("LOCATION", "" + location.getLatitude() + " - " + location.getLongitude());
//                        mViewModel.callWeatherApi(location.getLatitude(), location.getLongitude());
//                    }
//                }
//            }
//        };
//    }

    public void getLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (PermissionUtils.checkPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            fetchLocation();
        } else {
            PermissionUtils.requestPermissions(this, MY_PERMISSIONS_REQUEST_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    Double wLatitude = location.getLatitude();
                    Double wLongitude = location.getLongitude();
                    // save data to pref
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("latitude", String.valueOf(wLatitude)).apply();
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("longitude", String.valueOf(wLongitude)).apply();

                    Toast.makeText(this, "" + wLatitude + " - " + wLongitude, Toast.LENGTH_SHORT).show();

                    callWeatherService();
                }
            });

//            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private void callWeatherService() {
        if (!weatherServiceTriggered) {
            mViewModel.callWeatherApi(this);
            weatherServiceTriggered = true;
        }
    }

    private void observeWeatherRoomData() {
        mViewModel.getWeatherDataFromDBObservable().observe(this, data -> {
            if (data != null) {
                Log.d("Romm fetch Response", data.getTemp());
            } else {

            }
        });
    }
}