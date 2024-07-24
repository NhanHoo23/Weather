package fpoly.nhanhhph47395.weather.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;
import java.util.concurrent.TimeUnit;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.adapter.LocationAdapter;
import fpoly.nhanhhph47395.weather.adapter.SearchTextAdapter;
import fpoly.nhanhhph47395.weather.models.Location;
import fpoly.nhanhhph47395.weather.models.WeatherResponse;
import fpoly.nhanhhph47395.weather.subviews.NoResultsView;
import fpoly.nhanhhph47395.weather.utils.WeatherManager;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment implements SearchTextAdapter.SearchTextAdapterClickListener, LocationForecastFragment.LocationForecastFragmentListener {
    private SearchBar searchBar;
    private SearchView searchView;
    private RecyclerView rcSearch, rcLocation;
    private LocationAdapter locationAdapter;
    private SearchTextAdapter searchTextAdapter;
    private ProgressBar progressBar;
    private NoResultsView noResultsView;
    private List<WeatherResponse> locationList;
    private List<Location> searchList;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        setupView(view);

        setupSearchViewClearListener();

        setupSearchObservable();

        return view;
    }

    private void setupView(View view) {
        searchBar = view.findViewById(R.id.searchBar);
        searchView = view.findViewById(R.id.searchView);
        rcSearch = view.findViewById(R.id.rcSearch);
        rcLocation = view.findViewById(R.id.rcLocation);
        progressBar = view.findViewById(R.id.progressBar);
        noResultsView = view.findViewById(R.id.noResultsView);

        rcLocation.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        locationAdapter = new LocationAdapter(getContext());
        rcLocation.setAdapter(locationAdapter);
        locationList = WeatherManager.shared().getLocationList();
        locationAdapter.updateData(locationList);

        rcSearch.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        searchTextAdapter = new SearchTextAdapter(getContext(), this);
        rcSearch.setAdapter(searchTextAdapter);
    }

    private void setupSearchObservable() {
        Observable<String> searchObservable = createSearchObservable();
        compositeDisposable.add(
                searchObservable
                        .debounce(300, TimeUnit.MILLISECONDS) //chờ người dùng ngừng gõ 0.3s mới gọi lại
                        .distinctUntilChanged() //chỉ phát ra khi chuỗi mới khác chuỗi cũ
                        .filter(query -> !query.isEmpty())
                        .switchMap(query -> Observable.create(emitter -> {
                                    getActivity().runOnUiThread(() -> {
                                        progressBar.setVisibility(View.VISIBLE);
                                        noResultsView.setVisibility(View.GONE);
                                    });
                                    WeatherManager.shared().getSearchResults(query, new WeatherManager.SearchCallback() {

                                        @Override
                                        public void onSuccess(List<Location> locations) {
                                            emitter.onNext(locations);
                                            emitter.onComplete();
                                        }

                                        @Override
                                        public void onFailure(Throwable throwable) {
                                            emitter.onError(throwable);
                                        }
                                    });
                                }
                        ))
                        .subscribeOn(Schedulers.io()) //Thực hiện các thao tác tìm kiếm trên luồng I/O, tránh chặn luồng chính.
                        .observeOn(AndroidSchedulers.mainThread()) //Quan sát và cập nhật kết quả trên luồng chính để cập nhật UI.
                        .subscribe(locations -> {
                            progressBar.setVisibility(View.GONE);
                            searchList = (List<Location>) locations;
                            if (searchList.isEmpty()) {
                                noResultsView.setVisibility(View.VISIBLE);
                            } else {
                                noResultsView.setVisibility(View.GONE);
                            }
                            searchTextAdapter.updateData(searchList);
                        }, throwable -> {
                            progressBar.setVisibility(View.GONE);
                            noResultsView.setVisibility(View.VISIBLE);
                            Log.d("huhuhu", "error: " + throwable.getLocalizedMessage());
                        })
        );
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(), "" + searchList.get(position).name, Toast.LENGTH_SHORT).show();
        LocationForecastFragment bottomSheet = LocationForecastFragment.newInstance(searchList.get(position), this);
        bottomSheet.show(((FragmentActivity) getContext()).getSupportFragmentManager(), bottomSheet.getTag());
    }

    @Override
    public void updateUI() {
        searchView.hide();
        locationAdapter.updateData(WeatherManager.shared().getLocationList());
    }

    private Observable<String> createSearchObservable() {
        return Observable.create(emitter -> {
            searchView.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("onTextChanged", "onTextChanged: " + s.toString());
                    emitter.onNext(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        });
    }

    private void setupSearchViewClearListener() {
        searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    searchTextAdapter.clearData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }


}