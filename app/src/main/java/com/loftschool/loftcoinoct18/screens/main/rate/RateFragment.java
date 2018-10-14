package com.loftschool.loftcoinoct18.screens.main.rate;


import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loftschool.loftcoinoct18.App;
import com.loftschool.loftcoinoct18.R;
import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.api.model.Coin;
import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RateFragment extends Fragment implements RateView {

    public static final String TAG = "RateFragment";
    public static final String LAYOUT_MANAGER_STATE = "layout_manager_state";

    @BindView(R.id.rate_toolbar)
    Toolbar rateToolbar;
    @BindView(R.id.rate_recycler)
    RecyclerView rateRecycler;
    @BindView(R.id.rate_refresh)
    SwipeRefreshLayout rateRefresh;
    @BindView(R.id.rate_content)
    FrameLayout rateContent;
    Unbinder unbinder;

    private RatePresenter presenter;
    private RateAdapter adapter;
    private Parcelable layoutManagerState;

    public RateFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();

        if (activity == null) {
            return;
        }

        Api api = ((App) getActivity().getApplication()).getApi();
        Prefs prefs = ((App) getActivity().getApplication()).getPrefs();
        Database database = ((App) getActivity().getApplication()).getDatabase();
        CoinEntityMapper mapper = new CoinEntityMapper();

        presenter = new RatePresenterImpl(api, prefs, database, mapper);

        adapter = new RateAdapter(prefs);
        adapter.setHasStableIds(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        rateToolbar.setTitle(R.string.rate_screen_title);

        presenter.attachView(this);
        presenter.getRate();

        rateRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });

        presenter.attachView(this);
        presenter.getRate();

        rateRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        rateRecycler.setHasFixedSize(true);
        rateRecycler.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setCoins(List<CoinEntity> coins) {
        adapter.setCoins(coins);

    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        rateRefresh.setRefreshing(refreshing);

    }

    @Override
    public void showCurrencyDialog() {

    }
}
