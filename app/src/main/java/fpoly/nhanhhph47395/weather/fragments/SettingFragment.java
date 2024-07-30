package fpoly.nhanhhph47395.weather.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.SettingAdapter;
import fpoly.nhanhhph47395.weather.databinding.FragmentSettingBinding;
import fpoly.nhanhhph47395.weather.models.settingModels.SettingModel;
import fpoly.nhanhhph47395.weather.screens.DefaultLocationActivity;
import fpoly.nhanhhph47395.weather.screens.LanguageActivity;
import fpoly.nhanhhph47395.weather.screens.unitSetting.UnitSettingActivity;
import fpoly.nhanhhph47395.weather.utils.AppManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment implements SettingAdapter.OnClickListener, SettingAdapter.OnSwitchListener {
    private FragmentSettingBinding binding;
    private SettingAdapter adapter;
    private List<SettingModel> list;
    private BroadcastReceiver languageChangeReceiver;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        languageChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateLanguage();
            }
        };

        IntentFilter filter = new IntentFilter("LANGUAGE_CHANGE");
        requireActivity().registerReceiver(languageChangeReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        setupView();

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(languageChangeReceiver);
    }

    private void setupView() {
        boolean isDarkMode = AppManager.shared(getContext()).getDarkModeStatus();
        list = new ArrayList<>();
        list.add(new SettingModel(getString(R.string.unitSetting),  isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.darkModeSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, false));
        list.add(new SettingModel(getString(R.string.languageSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.notiSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.locationSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.defaultLocationSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.polycySetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rcSetting.setLayoutManager(linearLayoutManager);
        adapter = new SettingAdapter(getContext(), list, this, this);
        binding.rcSetting.setAdapter(adapter);
    }

    private void goToActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Log.d("hihihi", "onItemClick: " + position);
        switch (position) {
            case 0: //Đơn vị
                goToActivity(UnitSettingActivity.class);
                break;
            case 2://Chế độ tối

            case 3:
                Intent intent = new Intent(getActivity(), LanguageActivity.class);
                startActivityForResult(intent, 1);
                break;
            case 4://
            case 5://Vị trí mặc định
                goToActivity(DefaultLocationActivity.class);
                break;
            case 6:
        }
    }

    private void updateLanguage() {
        binding.tvTitle.setText(getString(R.string.setting));
        boolean isDarkMode = AppManager.shared(getContext()).getDarkModeStatus();
        list.clear();
        list.add(new SettingModel(getString(R.string.unitSetting),  isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.darkModeSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, false));
        list.add(new SettingModel(getString(R.string.languageSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.notiSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.locationSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.defaultLocationSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.polycySetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rcSetting.setLayoutManager(linearLayoutManager);
        adapter = new SettingAdapter(getContext(), list, this, this);
        binding.rcSetting.setAdapter(adapter);
    }

    @Override
    public void onClick(boolean isDarkMode) {
        Toast.makeText(getContext(), "isDarkMode: " + isDarkMode, Toast.LENGTH_SHORT).show();
        AppManager.shared(getContext()).setDarkModeStatus(isDarkMode);
        AppManager.applyTheme(isDarkMode);
    }
}