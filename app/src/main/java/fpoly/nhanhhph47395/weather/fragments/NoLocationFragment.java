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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.databinding.FragmentHomeBinding;
import fpoly.nhanhhph47395.weather.databinding.FragmentNoLocationBinding;
import fpoly.nhanhhph47395.weather.screens.MainActivity;
import fpoly.nhanhhph47395.weather.screens.SplashActivity;
import fpoly.nhanhhph47395.weather.utils.AppManager;
import fpoly.nhanhhph47395.weather.utils.WeatherManager;

public class NoLocationFragment extends Fragment {
    private FragmentNoLocationBinding binding;
    private FusedLocationProviderClient fusedLocationClient;

    public NoLocationFragment() {}

    public static NoLocationFragment newInstance() {
        NoLocationFragment fragment = new NoLocationFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoLocationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        boolean isDarkMode = AppManager.shared(getContext()).getDarkModeStatus();
        binding.imgMap.setImageResource(isDarkMode ? R.drawable.img_map_dark : R.drawable.img_map_light);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        binding.btnCurrentLocation.setOnClickListener(v -> {
            requestLocationPermission();
        });

        binding.btnSearchLocation.setOnClickListener(v -> {
            ((MainActivity) getActivity()).switchToSearchFragment();
            replaceFragment(new SearchFragment());
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
                getLastKnownLocation()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.e("Initialization Error", "Failed to get last known location", task.getException());
                            }

                            WeatherManager.shared().locationList.clear();
                            WeatherManager.shared().fetchAndStoreWeatherData(getContext())
                                    .thenAccept(aVoid -> {
                                        AppManager.shared(getContext()).setLocationEnabled(true);
                                        replaceFragment(new HomeFragment());
                                    })
                                    .exceptionally(throwable -> {
                                        Log.e("Initialization Error", "Failed to fetch weather data", throwable);
                                        return null;
                                    });
                        });
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

    private Task<Void> getLastKnownLocation() {
        final TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            taskCompletionSource.setException(new SecurityException("Location permissions not granted"));
            return taskCompletionSource.getTask();
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            List<String> locationList = AppManager.shared(getContext()).loadLocationList();
                            String latAndLong = location.getLatitude()+","+location.getLongitude();

                            if (AppManager.shared(getContext()).isFirstLogin()) {
                                if (locationList.isEmpty()) {
                                    locationList.add(latAndLong);
                                }
                            } else {
                                if (locationList.isEmpty()) {
                                    locationList.add(latAndLong);
                                } else {
                                    if (AppManager.shared(getContext()).hasLatAndLong()) {
                                        locationList.set(0, latAndLong);
                                    } else {
                                        locationList.add(0, latAndLong);
                                    }
                                }
                            }
                            AppManager.shared(getContext()).saveLocationList(locationList);
                            AppManager.shared(getContext()).setHasLatAndLong(true);

                            taskCompletionSource.setResult(null);
                        } else {
                            taskCompletionSource.setException(new Exception("Location is null"));
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }
}