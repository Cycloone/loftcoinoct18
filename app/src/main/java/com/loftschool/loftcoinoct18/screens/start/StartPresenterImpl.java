package com.loftschool.loftcoinoct18.screens.start;

import android.support.annotation.Nullable;

import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.api.model.Coin;
import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartPresenterImpl implements StartPresenter {
    public static final String TAG = "StartPresenterImpl";
    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;
    private CompositeDisposable disposables = new CompositeDisposable();

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
        disposables.dispose();
        this.view = null;
    }

    @Override
    public void loadRate() {
        Disposable disposable = api.ticker("array", prefs.getFiatCurrency().name())
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> {
                    List<Coin> coins = rateResponse.data;
                    List<CoinEntity> coinEntities = mapper.mapCoins(coins);
                    database.saveCoins(coinEntities);
                    return coinEntities;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        coinEntities -> {
                            if (view != null) {
                                view.navigateToMainScreen();
                            }
                        },
                        throwable -> {

                        }
                );


        disposables.add(disposable);

    }
}
