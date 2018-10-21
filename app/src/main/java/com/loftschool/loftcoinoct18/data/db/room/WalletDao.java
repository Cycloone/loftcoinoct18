package com.loftschool.loftcoinoct18.data.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.loftschool.loftcoinoct18.data.db.model.Wallet;
import com.loftschool.loftcoinoct18.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveWallet(Wallet wallet);

    @Query("SELECT w.*, c.* FROM Wallet w, Coin c WHERE w.currencyId = c.id")
    Flowable<List<WalletModel>> getWallets();
}
