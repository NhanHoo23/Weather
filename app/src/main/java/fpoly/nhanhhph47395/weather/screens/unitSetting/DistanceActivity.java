package fpoly.nhanhhph47395.weather.screens.unitSetting;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.SettingAdapter;
import fpoly.nhanhhph47395.weather.databinding.ActivityDistanceBinding;
import fpoly.nhanhhph47395.weather.models.settingModels.DistanceSettingModel;
import fpoly.nhanhhph47395.weather.models.settingModels.SettingModel;
import fpoly.nhanhhph47395.weather.utils.AppManager;

public class DistanceActivity extends AppCompatActivity implements SettingAdapter.OnClickListener {
    ActivityDistanceBinding binding;
    private List<SettingModel> list;
    private SettingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDistanceBinding.inflate(getLayoutInflater());
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
        getSupportActionBar().setTitle("Khoảng cách");

        list = new ArrayList<>();
        list.add(new DistanceSettingModel("Kilomet (km)", R.drawable.ic_check));
        list.add(new DistanceSettingModel("Dặm (mi)", R.drawable.ic_check));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcSetting.setLayoutManager(linearLayoutManager);
        adapter = new SettingAdapter(this, list, this);
        binding.rcSetting.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        AppManager.shared(this).setSelectedDistanceIndex(position);
        adapter.notifyDataSetChanged();
    }
}