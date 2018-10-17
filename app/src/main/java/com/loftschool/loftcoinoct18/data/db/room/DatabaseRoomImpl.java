package com.loftschool.loftcoinoct18.data.db.room;

import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;

import java.util.List;

import io.reactivex.Flowable;

public class DatabaseRoomImpl implements Database {

    private AppDatabase database;

    public DatabaseRoomImpl(AppDatabase database) {
        this.database = database;
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        database.coinDao().saveCoins(coins);
    }

    @Override
    public Flowable<List<CoinEntity>> getCoins() {
        return database.coinDao().getCoins();
    }
}
