package fpoly.nhanhhph47395.weather.screens;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.LocationAdapter;
import fpoly.nhanhhph47395.weather.databinding.ActivityMainBinding;
import fpoly.nhanhhph47395.weather.fragments.HomeFragment;
import fpoly.nhanhhph47395.weather.fragments.NoLocationFragment;
import fpoly.nhanhhph47395.weather.fragments.SearchFragment;
import fpoly.nhanhhph47395.weather.fragments.SettingFragment;
import fpoly.nhanhhph47395.weather.utils.AppManager;

public class MainActivity extends AppCompatActivity implements LocationAdapter.OnLocationSelectedListener {
    ActivityMainBinding binding;
    private int flag = R.id.nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (AppManager.shared(this).isLocationEnabled()) {
            replaceFragment(new HomeFragment());
        } else {
            replaceFragment(new NoLocationFragment());
        }

        binding.nvView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_home && flag != R.id.nav_home) {
                if (AppManager.shared(this).isLocationEnabled()) {
                    replaceFragment(new HomeFragment());
                } else {
                    replaceFragment(new NoLocationFragment());
                }
                flag = R.id.nav_home;
            } else if (menuItem.getItemId() == R.id.nav_search && flag != R.id.nav_search) {
                replaceFragment(new SearchFragment());
                flag = R.id.nav_search;
            } else if (menuItem.getItemId() == R.id.nav_setting && flag != R.id.nav_setting) {
                replaceFragment(new SettingFragment());
                flag = R.id.nav_setting;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        transaction.commit();
    }

    //Listener của LocationRecycleView bên SearchFragment
    @Override
    public void onLocationSelected(int position) {
        Fragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("locationIndex", position);
        homeFragment.setArguments(args);

        binding.nvView.setSelectedItemId(R.id.nav_home);
        replaceFragment(homeFragment);
    }
}