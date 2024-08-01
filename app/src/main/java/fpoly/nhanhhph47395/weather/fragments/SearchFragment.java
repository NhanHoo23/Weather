package fpoly.nhanhhph47395.weather.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.LocationAdapter;
import fpoly.nhanhhph47395.weather.adapter.SearchTextAdapter;
import fpoly.nhanhhph47395.weather.databinding.FragmentSearchBinding;
import fpoly.nhanhhph47395.weather.models.Location;
import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import fpoly.nhanhhph47395.weather.subviews.NoResultsView;
import fpoly.nhanhhph47395.weather.utils.AppManager;
import fpoly.nhanhhph47395.weather.utils.WeatherManager;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment implements SearchTextAdapter.SearchTextAdapterClickListener, LocationForecastFragment.LocationForecastFragmentListener {
    private FragmentSearchBinding binding;
    private LocationAdapter locationAdapter;
    private SearchTextAdapter searchTextAdapter;
    private List<WeatherResponse> locationList;
    private List<Location> searchList;

    private LocationAdapter.OnLocationSelectedListener listener;
    private Handler handler;
    private Runnable searchRunnable;

    private boolean isSelect = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LocationAdapter.OnLocationSelectedListener) {
            listener = (LocationAdapter.OnLocationSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnLocationSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public SearchFragment() {}

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupView();
        setupTextWatcher();

        return view;
    }

    private void setupView() {
        binding.rcLocation.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        locationAdapter = new LocationAdapter(getContext(), listener, this);
        binding.rcLocation.setAdapter(locationAdapter);
        locationList = WeatherManager.shared().getLocationList();
        locationAdapter.updateData(locationList);

        binding.rcSearch.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        searchTextAdapter = new SearchTextAdapter(getContext(), this);
        binding.rcSearch.setAdapter(searchTextAdapter);

        binding.tvSelect.setOnClickListener(v -> {
            isSelect = !isSelect;
            binding.tvSelect.setText(isSelect ? "Xong" : "Chọn");
            locationAdapter.toggleDeleteButton();
        });
    }

    @Override
    public void onItemClick(int position) {
        LocationForecastFragment bottomSheet = LocationForecastFragment.newInstance(searchList.get(position), this);
        bottomSheet.show(((FragmentActivity) getContext()).getSupportFragmentManager(), bottomSheet.getTag());
    }

    @Override
    public void updateUI() {
        binding.searchView.hide();
        locationAdapter.updateData(WeatherManager.shared().getLocationList());
    }

    private void setupTextWatcher() {
        handler = new Handler();
        binding.searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable); // Xóa Runnable cũ nếu có
                }

                if (s.length() == 0) {
                    searchTextAdapter.clearData();
                    binding.noResultsView.setVisibility(View.GONE);
                } else {
                    searchRunnable = () -> performSearch(s.toString());
                    handler.postDelayed(searchRunnable, 300);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch(String query) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.noResultsView.setVisibility(View.GONE);

        WeatherManager.shared().getSearchResults(query, new WeatherManager.SearchCallback() {
            @Override
            public void onSuccess(List<Location> locations) {
                binding.progressBar.setVisibility(View.GONE);
                searchList = locations;
                if (searchList.isEmpty()) {
                    binding.noResultsView.showNoResults();
                } else {
                    binding.noResultsView.setVisibility(View.GONE);
                }
                searchTextAdapter.updateData(searchList);
            }

            @Override
            public void onFailure(Throwable throwable) {
                binding.progressBar.setVisibility(View.GONE);
                binding.noResultsView.showNetworkErrorWithoutButton();
                Log.d("huhuhu", "error: " + throwable.getLocalizedMessage());
            }
        });
    }

    public void deleteItem(int position) {
        isSelect = false;
        binding.tvSelect.setText("Chọn");
        List<String> listLocationString = AppManager.shared(getContext()).loadLocationList();
        listLocationString.remove(position);
        AppManager.shared(getContext()).saveLocationList(listLocationString);
        AppManager.shared(getContext()).setSelectedDefaultLocationIndex(0);
        WeatherManager.shared().locationList.remove(position);
        locationAdapter.toggleDeleteButton();
    }

}