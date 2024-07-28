package fpoly.nhanhhph47395.weather.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.SettingAdapter;
import fpoly.nhanhhph47395.weather.databinding.FragmentSettingBinding;
import fpoly.nhanhhph47395.weather.models.settingModels.SettingModel;
import fpoly.nhanhhph47395.weather.screens.DefaultLocationActivity;
import fpoly.nhanhhph47395.weather.screens.LanguageActivity;
import fpoly.nhanhhph47395.weather.screens.unitSetting.UnitSettingActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment implements SettingAdapter.OnClickListener {
    private FragmentSettingBinding binding;
    private SettingAdapter adapter;
    private List<SettingModel> list;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        setupView();

        return binding.getRoot();
    }

    private void setupView() {
        list = new ArrayList<>();
        list.add(new SettingModel("Đơn vị", R.drawable.ic_arrow, true));
        list.add(new SettingModel("Chế độ tối", R.drawable.ic_arrow, false));
        list.add(new SettingModel("Ngôn ngữ", R.drawable.ic_arrow, true));
        list.add(new SettingModel("Quản lý thông báo", R.drawable.ic_arrow, true));
        list.add(new SettingModel("Truy cập vị trí", R.drawable.ic_arrow, true));
        list.add(new SettingModel("Vị trí mặc định", R.drawable.ic_arrow, true));
        list.add(new SettingModel("Chính sách quyền riêng tư", R.drawable.ic_arrow, true));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rcSetting.setLayoutManager(linearLayoutManager);
        adapter = new SettingAdapter(getContext(), list, this);
        binding.rcSetting.setAdapter(adapter);
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
                goToActivity(LanguageActivity.class);
                break;
            case 4://
            case 5://Vị trí mặc định
                goToActivity(DefaultLocationActivity.class);
                break;
            case 6:
        }
    }

    private void goToActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
    }
}