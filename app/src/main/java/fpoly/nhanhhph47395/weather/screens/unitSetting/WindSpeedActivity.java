package fpoly.nhanhhph47395.weather.screens.unitSetting;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.SettingAdapter;
import fpoly.nhanhhph47395.weather.databinding.ActivityWindSpeedBinding;
import fpoly.nhanhhph47395.weather.models.settingModels.WindSpeedSettingModel;
import fpoly.nhanhhph47395.weather.models.settingModels.SettingModel;
import fpoly.nhanhhph47395.weather.utils.AppManager;

public class WindSpeedActivity extends AppCompatActivity implements SettingAdapter.OnClickListener {
    ActivityWindSpeedBinding binding;
    private List<SettingModel> list;
    private SettingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWindSpeedBinding.inflate(getLayoutInflater());
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
        getSupportActionBar().setTitle(getString(R.string.windSpeedSetting));

        boolean isDarkMode = AppManager.shared(this).getDarkModeStatus();
        list = new ArrayList<>();
        list.add(new WindSpeedSettingModel(getString(R.string.windSpeedKmSetting), isDarkMode ? R.drawable.ic_check_dark : R.drawable.ic_check));
        list.add(new WindSpeedSettingModel(getString(R.string.windSpeedMphSetting), isDarkMode ? R.drawable.ic_check_dark : R.drawable.ic_check));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcSetting.setLayoutManager(linearLayoutManager);
        adapter = new SettingAdapter(this, list, this);
        binding.rcSetting.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        AppManager.shared(this).setSelectedWindSpeedIndex(position);
        adapter.notifyDataSetChanged();
    }
}