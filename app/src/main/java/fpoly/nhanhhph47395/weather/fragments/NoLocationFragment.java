package fpoly.nhanhhph47395.weather.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.screens.SplashActivity;
import fpoly.nhanhhph47395.weather.utils.AppManager;

public class NoLocationFragment extends Fragment {
    private ExtendedFloatingActionButton btnCurrentLocation, btnSearchLocation;
    private FusedLocationProviderClient fusedLocationClient;

    public NoLocationFragment() {
        // Required empty public constructor
    }

    public static NoLocationFragment newInstance() {
        NoLocationFragment fragment = new NoLocationFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_location, container, false);

        btnCurrentLocation = view.findViewById(R.id.btnCurrentLocation);
        btnSearchLocation = view.findViewById(R.id.btnSearchLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        btnCurrentLocation.setOnClickListener(v -> {
            requestLocationPermission();
        });

        return view;
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppManager.shared(getContext()).REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AppManager.shared(getContext()).REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AppManager.shared(getContext()).setLocationEnabled(true);
                getLastKnownLocation();
                replaceFragment(new HomeFragment());
            } else {
                AppManager.shared(getContext()).setLocationEnabled(false);
            }
        }
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            AppManager.shared(getContext()).setDefaultLongitude((float) location.getLongitude());
                            AppManager.shared(getContext()).setDefaultLatitude((float) location.getLatitude());
                            Log.d("Long", "onSuccess: " + location.getLongitude());
                            Log.d("Lat", "onSuccess: " + location.getLatitude());
                        }
                    }
                });
    }
}