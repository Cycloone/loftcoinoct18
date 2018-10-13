package com.loftschool.loftcoinoct18.data.db;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;

import java.util.List;

public interface Database {

    void saveCoins(List<CoinEntity> coins);

    List<CoinEntity> getCoins();

}
