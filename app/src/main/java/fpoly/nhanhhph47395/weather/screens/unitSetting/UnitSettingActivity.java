package fpoly.nhanhhph47395.weather.screens.unitSetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.SettingAdapter;
import fpoly.nhanhhph47395.weather.databinding.ActivityUnitSettingBinding;
import fpoly.nhanhhph47395.weather.models.settingModels.SettingModel;
import fpoly.nhanhhph47395.weather.utils.AppManager;

public class UnitSettingActivity extends AppCompatActivity implements SettingAdapter.OnClickListener {
    ActivityUnitSettingBinding binding;
    private List<SettingModel> list;
    private SettingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUnitSettingBinding.inflate(getLayoutInflater());
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
        getSupportActionBar().setTitle(getString(R.string.unitSetting));

        boolean isDarkMode = AppManager.shared(this).getDarkModeStatus();
        list = new ArrayList<>();
        list.add(new SettingModel(getString(R.string.tempSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.windSpeedSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.distanceSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));
        list.add(new SettingModel(getString(R.string.precipSetting), isDarkMode ? R.drawable.ic_arrow_dark : R.drawable.ic_arrow, true));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcSetting.setLayoutManager(linearLayoutManager);
        adapter = new SettingAdapter(this, list, this);
        binding.rcSetting.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                goToActivity(TempActivity.class);
                break;
            case 1:
                goToActivity(WindSpeedActivity.class);
                break;
            case 2:
                goToActivity(DistanceActivity.class);
                break;
            case 3:
                goToActivity(PrecipitationActivity.class);
                break;
            default:
                break;
        }
    }

    private void goToActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(UnitSettingActivity.this, activityClass);
        startActivity(intent);
    }
}