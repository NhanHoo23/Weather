package fpoly.nhanhhph47395.weather.screens;

import android.os.Bundle;
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
import fpoly.nhanhhph47395.weather.databinding.ActivityDefaultLocationBinding;
import fpoly.nhanhhph47395.weather.databinding.ActivityLanguageBinding;
import fpoly.nhanhhph47395.weather.models.settingModels.DefaultLocationModel;
import fpoly.nhanhhph47395.weather.models.settingModels.LanguageModel;
import fpoly.nhanhhph47395.weather.models.settingModels.SettingModel;
import fpoly.nhanhhph47395.weather.utils.AppManager;

public class DefaultLocationActivity extends AppCompatActivity implements SettingAdapter.OnClickListener {
    ActivityDefaultLocationBinding binding;
    private List<SettingModel> list;
    private SettingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDefaultLocationBinding.inflate(getLayoutInflater());
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
        getSupportActionBar().setTitle("Ngôn ngữ");

        list = new ArrayList<>();
        List<String> locationList = AppManager.shared(this).loadLocationList();
        for (int i = 0; i < locationList.size(); i++) {
            String location = locationList.get(i);
            if (i == 0) {
                list.add(new DefaultLocationModel("Vị trí của tôi", R.drawable.ic_check));
            } else {
                list.add(new DefaultLocationModel(location, R.drawable.ic_check));
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcSetting.setLayoutManager(linearLayoutManager);
        adapter = new SettingAdapter(this, list, this);
        binding.rcSetting.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        AppManager.shared(this).setSelectedDefaultLocationIndex(position);
        adapter.notifyDataSetChanged();
    }
}