package com.loftschool.loftcoinoct18.data.db.model;

import android.arch.persistence.room.Embedded;

public class WalletModel {

    @Embedded()
    public Wallet wallet;

    @Embedded()
    public CoinEntity coin;
}