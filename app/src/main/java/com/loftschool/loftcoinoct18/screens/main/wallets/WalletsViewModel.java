package com.loftschool.loftcoinoct18.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;

public abstract class WalletsViewModel extends AndroidViewModel {
    public WalletsViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void onNewWalletClick();

    public abstract void onCurrencySelected(CoinEntity coin);

    public abstract LiveData<Object> selectCurrency();

}
