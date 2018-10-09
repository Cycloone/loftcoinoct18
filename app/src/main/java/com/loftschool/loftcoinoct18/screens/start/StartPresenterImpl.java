package com.loftschool.loftcoinoct18.screens.start;

import android.support.annotation.Nullable;
import android.util.Log;

import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.api.model.RateResponse;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartPresenterImpl implements StartPresenter {
    public static final String TAG = "StartPresenterImpl";
    private Api api;
    private Prefs prefs;

    public StartPresenterImpl(Api api, Prefs prefs) {
        this.api = api;
        this.prefs = prefs;
    }

    @Nullable
    private StartView view;

    @Override
    public void attachView(StartView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadRate() {
        api.ticker("array", "USD").enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                if(view != null){
                    view.navigateToMainScreen();
                }

            }

            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {
                Log.e(TAG, "Load rate err", t);
            }
        });
    }
}
