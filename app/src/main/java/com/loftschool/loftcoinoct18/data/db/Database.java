package com.loftschool.loftcoinoct18.data.db;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.Wallet;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins(List<CoinEntity> coins);

    void saveWallet(Wallet wallet);

    Flowable<List<CoinEntity>> getCoins();

    CoinEntity getCoin(String symbol);

}
