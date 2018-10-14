package com.loftschool.loftcoinoct18;

import android.app.Application;

import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.api.ApiInitializer;
import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.DatabaseInitializer;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;
import com.loftschool.loftcoinoct18.data.prefs.PrefsImpl;

public class App extends Application {
    private Api api;
    private Prefs prefs;
    private Database database;

    public Prefs getPrefs() {
        return prefs;
    }

    public Api getApi() {
        return api;
    }

    public Database getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = new PrefsImpl(this);
        api = new ApiInitializer().init();
        database = new DatabaseInitializer().init(this);
    }
}
