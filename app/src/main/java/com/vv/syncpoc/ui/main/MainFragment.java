package com.vv.syncpoc.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.vv.syncpoc.R;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
//        mViewModel.getListObservable().observe(this, data -> {
//            if (data != null && data.getLatitude() != 0.0) {
//                Toast.makeText(getActivity(), String.valueOf(data.getLatitude()), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
