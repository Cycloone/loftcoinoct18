package com.loftschool.loftcoinoct18.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.Transaction;
import com.loftschool.loftcoinoct18.data.db.model.Wallet;

import java.util.List;

public abstract class WalletsViewModel extends AndroidViewModel {
    public WalletsViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void getWallets();

    public abstract void onNewWalletClick();

    public abstract void onCurrencySelected(CoinEntity coin);

    public abstract void onWalletChanged(int position);

    public abstract LiveData<List<Wallet>> wallets();

    public abstract LiveData<Object> selectCurrency();

    public abstract LiveData<Boolean> walletsVisible();

    public abstract LiveData<Boolean> newWalletVisible();

    public abstract LiveData<List<Transaction>> transactions();

}
