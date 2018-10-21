package com.loftschool.loftcoinoct18.data.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.loftschool.loftcoinoct18.data.db.model.Wallet;

@Dao
public interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveWallet(Wallet wallet);
}
