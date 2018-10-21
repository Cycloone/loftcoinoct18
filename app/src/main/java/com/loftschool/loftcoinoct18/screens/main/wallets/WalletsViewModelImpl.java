package com.loftschool.loftcoinoct18.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.loftschool.loftcoinoct18.App;
import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.Wallet;
import com.loftschool.loftcoinoct18.utils.SingleLiveEvent;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletsViewModelImpl extends WalletsViewModel {

    private SingleLiveEvent<Object> selectCurrency = new SingleLiveEvent<Object>();

    private Database database;

    private CompositeDisposable disposables = new CompositeDisposable();

    public WalletsViewModelImpl(@NonNull Application application) {
        super(application);

        database = ((App)application).getDatabase();
    }

    @Override
    public void onNewWalletClick() {
        selectCurrency.postValue(new Object());
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        Wallet wallet = randomWallet(coin);

        Disposable disposable = Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                database.saveWallet(wallet);
                return new Object();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();

        disposables.add(disposable);
    }

    private Wallet randomWallet(CoinEntity coin) {
        Random random = new Random();
        return new Wallet(UUID.randomUUID().toString(), coin.id, 10 * random.nextDouble());
    }

    @Override
    public LiveData<Object> selectCurrency() {
        return selectCurrency;
    }
}
