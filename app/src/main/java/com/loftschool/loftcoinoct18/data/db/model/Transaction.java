package com.loftschool.loftcoinoct18.data.db.model;


import io.realm.RealmObject;

public class Transaction extends RealmObject {

    public Transaction() {
    }

    public String walletId;

    public double amount;

    public long date;

    public CoinEntity coin;


    public Transaction(String walletId, double amount, long date, CoinEntity coin) {
        this.walletId = walletId;
        this.amount = amount;
        this.date = date;
        this.coin = coin;
    }
}
