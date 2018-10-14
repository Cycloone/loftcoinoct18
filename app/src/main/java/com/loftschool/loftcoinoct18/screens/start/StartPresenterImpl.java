package com.loftschool.loftcoinoct18.screens.start;

import android.support.annotation.Nullable;
import android.util.Log;

import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.api.model.Coin;
import com.loftschool.loftcoinoct18.data.api.model.RateResponse;
import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartPresenterImpl implements StartPresenter {
    public static final String TAG = "StartPresenterImpl";
    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;

    public StartPresenterImpl(Api api, Prefs prefs, Database database, CoinEntityMapper mapper) {
        this.api = api;
        this.prefs = prefs;
        this.database = database;
        this.mapper = mapper;
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
        api.ticker("array",prefs.getFiatCurrency().name()).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                if (response.body() != null){
                    List<Coin> coins = response.body().data;
                    List<CoinEntity> entities = mapper.mapCoins(coins);

                    database.saveCoins(entities);
                }

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
