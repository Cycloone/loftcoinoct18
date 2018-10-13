package com.loftschool.loftcoinoct18.data.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;

import java.util.List;

@Dao
public interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCoins(List<CoinEntity> coins);

    @Query("SELECT * FROM Coin")
    List<CoinEntity> getCoins();
}
