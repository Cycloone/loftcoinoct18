package com.loftschool.loftcoinoct18.data.db;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.Transaction;
import com.loftschool.loftcoinoct18.data.db.model.Wallet;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins(List<CoinEntity> coins);

    void saveWallet(Wallet wallet);

    void saveTransaction(List<Transaction> transactions);

    Flowable<List<CoinEntity>> getCoins();

    Flowable<List<Wallet>> getWallets();

    Flowable<List<Transaction>> getTransactions(String walletId);

    CoinEntity getCoin(String symbol);

    void openRealm();

    void closeRealm();

}
