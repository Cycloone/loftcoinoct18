package com.loftschool.loftcoinoct18.screens.launch;

import android.app.Activity;
import android.os.Bundle;

import com.loftschool.loftcoinoct18.App;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;
import com.loftschool.loftcoinoct18.screens.start.StartActivity;
import com.loftschool.loftcoinoct18.screens.welcome.WelcomeActivity;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Prefs prefs = ((App)getApplication()).getPrefs();

        if (prefs.isFirstLaunch()) {
            WelcomeActivity.start(this);
        } else {
            StartActivity.start(this);
        }

    }
}
