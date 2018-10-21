package com.loftschool.loftcoinoct18.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public abstract class WalletsViewModel extends AndroidViewModel {
    public WalletsViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void onNewWalletClick();

    public abstract LiveData<Object> selectCurrency();



}
