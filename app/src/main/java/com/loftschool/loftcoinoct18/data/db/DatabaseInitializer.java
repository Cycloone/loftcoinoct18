package com.loftschool.loftcoinoct18.data.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.loftschool.loftcoinoct18.data.db.room.AppDatabase;
import com.loftschool.loftcoinoct18.data.db.room.DatabaseRoomImpl;

public class DatabaseInitializer {

    public Database init(Context context){
        AppDatabase appDatabase = Room
                .databaseBuilder(context, AppDatabase.class, "LoftCoin.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        return new DatabaseRoomImpl(appDatabase);
    }
}
