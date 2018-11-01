package com.loftschool.loftcoinoct18.job;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.loftschool.loftcoinoct18.App;
import com.loftschool.loftcoinoct18.R;
import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoinoct18.data.db.model.QuoteEntity;
import com.loftschool.loftcoinoct18.data.model.Fiat;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;
import com.loftschool.loftcoinoct18.screens.main.MainActivity;
import com.loftschool.loftcoinoct18.utils.CurrencyFormatter;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SyncRateJobService extends JobService {

    public static final String EXTRA_SYMBOL = "symbol";

    public static final String TAG = "SyncRateJobService";

    public static final String NOTIFICATION_CHANNEL_RATE_CHANGED = "RATE_CHANGED";

    public static final int NOTIFICATION_ID_RATE_CHANGED = 10;

    private Api api;
    private Database database;
    private Prefs prefs;
    private CurrencyFormatter formatter;
    private CoinEntityMapper mapper;
    private String symbol = "BTC";

    private Disposable disposable;


    @Override
    public void onCreate() {
        super.onCreate();

        api = ((App) getApplication()).getApi();
        database = ((App) getApplication()).getDatabase();
        prefs = ((App) getApplication()).getPrefs();

        mapper = new CoinEntityMapper();
        formatter = new CurrencyFormatter();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        doJob(params);
        return true;
    }

    private void doJob(JobParameters params) {
        symbol = params.getExtras().getString(EXTRA_SYMBOL, "BTC");

        disposable = api.ticker("array", prefs.getFiatCurrency().name())
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> mapper.mapCoins(rateResponse.data))
                .subscribe(
                        coinEntities -> {
                            handleCoins(coinEntities);
                            jobFinished(params, false);
                        },
                        error -> {
                            handleError(error);
                            jobFinished(params, false);
                        }
                );
    }


    private void handleCoins(List<CoinEntity> newCoins) {
        database.openRealm();
        Fiat fiat = prefs.getFiatCurrency();

        CoinEntity oldCoin = database.getCoin(symbol);
        CoinEntity newCoin = findCoin(newCoins, symbol);

        if (oldCoin != null && newCoin != null) {
            QuoteEntity oldQuote = oldCoin.getQuote(fiat);
            QuoteEntity newQuote = newCoin.getQuote(fiat);

            if (newQuote.price != oldQuote.price) {
                Log.i(TAG, "Price is changed: ");

                double priceDiff = newQuote.price - oldQuote.price;

                String priceDiffString;
                String price = formatter.format(Math.abs(priceDiff), false);

                if (priceDiff > 0) {
                    priceDiffString = "+ " + price + " " + fiat.symbol;
                } else {
                    priceDiffString = "- " + price + " " + fiat.symbol;
                }

                showRateChangedNotification(newCoin, priceDiffString);
            } else {
                Log.i(TAG, "Price not changed: ");
            }
        }

        database.saveCoins(newCoins);
        database.closeRealm();
    }

    private void handleError(Throwable error) {
        Log.e(TAG, "Failure to sync", error);
    }

    private void showRateChangedNotification(CoinEntity newCoin, String priceDiff) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_RATE_CHANGED);

        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(newCoin.name);
        builder.setContentText(getString(R.string.notification_rate, priceDiff));
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_RATE_CHANGED,
                    getString(R.string.notification_channel_rate),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(newCoin.symbol, NOTIFICATION_ID_RATE_CHANGED, notification);

    }

    private CoinEntity findCoin(List<CoinEntity> newCoins, String symbol) {
        for (CoinEntity coin : newCoins) {
            if (coin.symbol.equals(symbol)) {
                return coin;
            }
        }
        return null;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        return false;
    }
}
