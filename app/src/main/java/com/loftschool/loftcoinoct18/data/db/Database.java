package com.loftschool.loftcoinoct18.data.db;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins(List<CoinEntity> coins);

    Flowable<List<CoinEntity>> getCoins();

    CoinEntity getCoin(String symbol);

}
