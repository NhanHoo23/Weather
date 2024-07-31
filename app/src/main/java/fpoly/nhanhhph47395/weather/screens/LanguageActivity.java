package fpoly.nhanhhph47395.weather.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.SettingAdapter;
import fpoly.nhanhhph47395.weather.databinding.ActivityLanguageBinding;
import fpoly.nhanhhph47395.weather.databinding.ActivityTempBinding;
import fpoly.nhanhhph47395.weather.models.settingModels.LanguageModel;
import fpoly.nhanhhph47395.weather.models.settingModels.SettingModel;
import fpoly.nhanhhph47395.weather.models.settingModels.TempSettingModel;
import fpoly.nhanhhph47395.weather.utils.AppManager;
import fpoly.nhanhhph47395.weather.utils.WeatherManager;

public class LanguageActivity extends AppCompatActivity implements SettingAdapter.OnClickListener {
    ActivityLanguageBinding binding;
    private List<SettingModel> list;
    private SettingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.languageSetting));

        boolean isDarkMode = AppManager.shared(this).getDarkModeStatus();
        list = new ArrayList<>();
        list.add(new LanguageModel(getString(R.string.languageViSetting), isDarkMode ? R.drawable.ic_check_dark : R.drawable.ic_check));
        list.add(new LanguageModel(getString(R.string.languageEnSetting), isDarkMode ? R.drawable.ic_check_dark : R.drawable.ic_check));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcSetting.setLayoutManager(linearLayoutManager);
        adapter = new SettingAdapter(this, list, this);
        binding.rcSetting.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        AppManager.shared(this).setSelectedLanguageIndex(position);
        adapter.notifyDataSetChanged();

        AppManager.setLocale(this, position == 0 ? "vi":"en");
        Intent intent = new Intent("LANGUAGE_CHANGE");
        sendBroadcast(intent);

        WeatherManager.shared().locationList.clear();
        WeatherManager.shared().fetchAndStoreWeatherData(this)
                .thenAccept(aVoid -> {
                    recreate();
                })
                .exceptionally(throwable -> {
                    Log.e("Initialization Error", "Failed to fetch weather data", throwable);
                    return null;
                });
    }
}