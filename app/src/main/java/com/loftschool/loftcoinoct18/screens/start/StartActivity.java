package com.loftschool.loftcoinoct18.screens.start;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.loftschool.loftcoinoct18.App;
import com.loftschool.loftcoinoct18.R;
import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;
import com.loftschool.loftcoinoct18.screens.main.MainActivity;
import com.loftschool.loftcoinoct18.screens.welcome.WelcomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StartActivity extends AppCompatActivity implements StartView {
    private StartPresenter presenter;

    @BindView(R.id.start_top_corner)
    ImageView start_top_corner;

    @BindView(R.id.start_page_centr)
    ImageView start_page_centr;

    @BindView(R.id.title_start_page)
    TextView title_start_page;

    @BindView(R.id.start_bottom_corner)
    ImageView start_bottom_corner;

    Unbinder unbinder;

    public static void start(Context context) {
        Intent starter = new Intent(context, StartActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        unbinder = ButterKnife.bind(this);

        Api api = ((App) getApplication()).getApi();
        Prefs prefs = ((App) getApplication()).getPrefs();

        presenter = new StartPresenterImpl(api, prefs);
        presenter.attachView(this);

        presenter.loadRate();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startAnimations();
    }

    private void startAnimations() {
        ObjectAnimator innerAnimator = ObjectAnimator.ofFloat(start_top_corner, "rotation",0,360);
        innerAnimator.setDuration(30000);
        innerAnimator.setRepeatMode(ValueAnimator.RESTART);
        innerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        innerAnimator.setInterpolator(new LinearInterpolator());

        ObjectAnimator outerAnimator = ObjectAnimator.ofFloat(start_bottom_corner, "rotation",0,-360);
        outerAnimator.setDuration(60000);
        outerAnimator.setRepeatMode(ValueAnimator.RESTART);
        outerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        outerAnimator.setInterpolator(new LinearInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.play(innerAnimator).with(outerAnimator);
        set.start();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void navigateToMainScreen() {
        MainActivity.start(this);

    }
}
