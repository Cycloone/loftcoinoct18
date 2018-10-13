package com.loftschool.loftcoinoct18.data.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Coin")
public class CoinEntity {

    @PrimaryKey
    public int id;

    public String name;
    public String symbol;
    public String slug;
    public int rank;
    public long updated;
}
