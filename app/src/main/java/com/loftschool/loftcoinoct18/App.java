package com.loftschool.loftcoinoct18;

import android.app.Application;

import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.api.ApiInitializer;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;
import com.loftschool.loftcoinoct18.data.prefs.PrefsImpl;

public class App extends Application {
    private Api api;
    private Prefs prefs;

    public Prefs getPrefs() {
        return prefs;
    }

    public Api getApi() {
        return api;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = new PrefsImpl(this);
        api = new ApiInitializer().init();
    }


}
