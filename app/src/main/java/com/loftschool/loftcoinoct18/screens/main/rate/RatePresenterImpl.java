package com.loftschool.loftcoinoct18.screens.main.rate;

import android.support.annotation.Nullable;

import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.api.model.RateResponse;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatePresenterImpl implements RatePresenter {

    public static final String TAG = "RatePresenterImpl";

    private Api api;
    private Prefs prefs;

    @Nullable
    private RateView view;

    public RatePresenterImpl(Api api, Prefs prefs) {
        this.api = api;
        this.prefs = prefs;
    }

    @Override
    public void attachView(RateView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void getRate() {
        api.ticker("array", prefs.getFiatCurrency().name()).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                if (view != null && response.body() != null) {
                    view.setCoins(response.body().data);
                    view.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {
                if (view != null) {
                    view.setRefreshing(false);
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        getRate();

    }

}
