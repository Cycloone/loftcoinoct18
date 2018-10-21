package com.loftschool.loftcoinoct18.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

public class WalletsViewModelImpl extends WalletsViewModel {

    private MutableLiveData<Object> selectCurrency = new MutableLiveData<Object>();

    public WalletsViewModelImpl(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onNewWalletClick() {
        selectCurrency.postValue(new Object());
    }

    @Override
    public LiveData<Object> selectCurrency() {
        return selectCurrency;
    }
}
