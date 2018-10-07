package com.loftschool.loftcoinoct18.screens.start;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.loftschool.loftcoinoct18.R;
import com.loftschool.loftcoinoct18.screens.welcome.WelcomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.start_top_corner)
    ImageView start_top_corner;

    @BindView(R.id.start_page_centr)
    ImageView start_page_centr;

    @BindView(R.id.title_start_page)
    TextView title_start_page;

    @BindView(R.id.start_bottom_corner)
    ImageView start_bottom_corner;

    Unbinder unbinder;

    public static void start(Context context){
        Intent starter = new Intent(context, StartActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        unbinder = ButterKnife.bind(this);
    }

}
