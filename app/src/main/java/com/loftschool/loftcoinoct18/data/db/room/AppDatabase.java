package com.loftschool.loftcoinoct18.data.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;

@Database(entities = {CoinEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CoinDao coinDao();
}
