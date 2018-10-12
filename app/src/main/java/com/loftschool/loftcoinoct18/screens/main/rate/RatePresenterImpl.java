package com.loftschool.loftcoinoct18.screens.main.rate;

import android.support.annotation.Nullable;

import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;

public class RatePresenterImpl implements RatePresenter {

    public static final String TAG = "RatePresenterImpl";

    private Api api;
    private Prefs prefs;

    @Nullable
    private RateView view;

    public RatePresenterImpl(Api api, Prefs prefs) {
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

    }

    @Override
    public void onRefresh() {
        loadRate();

    }

    private void loadRate() {
    }
}
