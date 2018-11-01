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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loftschool.loftcoinoct18.App;
import com.loftschool.loftcoinoct18.R;
import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoinoct18.data.model.Fiat;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;
import com.loftschool.loftcoinoct18.job.JobHelper;
import com.loftschool.loftcoinoct18.job.JobHelperImpl;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RateFragment extends Fragment implements RateView, Toolbar.OnMenuItemClickListener, CurrencyDialog.CurrencyDialogListener, RateAdapter.Listener {

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
    @BindView(R.id.progress)
    ViewGroup progress;

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
        Database mainDatabase = ((App) getActivity().getApplication()).getDatabase();
        Database workerDatabase = ((App) getActivity().getApplication()).getDatabase();
        CoinEntityMapper mapper = new CoinEntityMapper();
        JobHelper jobHelper = new JobHelperImpl(getContext());

        presenter = new RatePresenterImpl(api, prefs, mainDatabase, workerDatabase, mapper, jobHelper);

        adapter = new RateAdapter(prefs);
        adapter.setHasStableIds(true);
        adapter.setListener(this);
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

        rateToolbar.inflateMenu(R.menu.menu_rate);
        rateToolbar.setOnMenuItemClickListener(this);

        presenter.attachView(this);
        presenter.getRate();

        rateRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });

        if (savedInstanceState != null) {
            layoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
        }

        Fragment fragment = Objects.requireNonNull(getFragmentManager()).findFragmentByTag(CurrencyDialog.TAG);

        if (fragment != null) {
            ((CurrencyDialog) fragment).setListener(this);
        }

        presenter.attachView(this);
        presenter.getRate();

        rateRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        rateRecycler.setHasFixedSize(true);
        rateRecycler.setAdapter(adapter);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LAYOUT_MANAGER_STATE, Objects.requireNonNull(rateRecycler.getLayoutManager()).onSaveInstanceState());
        super.onSaveInstanceState(outState);
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

        if (layoutManagerState != null) {
            Objects.requireNonNull(rateRecycler.getLayoutManager()).onRestoreInstanceState(layoutManagerState);
            layoutManagerState = null;
        }

    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        rateRefresh.setRefreshing(refreshing);

    }

    @Override
    public void showCurrencyDialog() {
        CurrencyDialog dialog = new CurrencyDialog();
        dialog.setListener(this);
        dialog.show(Objects.requireNonNull(getFragmentManager()), CurrencyDialog.TAG);

    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_currency:
                presenter.onMenuItemCurrencyClick();
        }
        return false;
    }

    @Override
    public void onCurrencySelected(Fiat currency) {
        presenter.onFiatCurrencySelected(currency);
    }

    @Override
    public void onRateLongClick(String symbol) {
        presenter.onRateLongClick(symbol);
    }
}
