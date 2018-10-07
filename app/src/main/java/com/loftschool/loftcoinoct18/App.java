package com.loftschool.loftcoinoct18;

import android.app.Application;

import com.loftschool.loftcoinoct18.data.prefs.Prefs;
import com.loftschool.loftcoinoct18.data.prefs.PrefsImpl;

public class App extends Application {
    private Prefs prefs;

    public Prefs getPrefs() {
        return prefs;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = new PrefsImpl(this);
    }


}
