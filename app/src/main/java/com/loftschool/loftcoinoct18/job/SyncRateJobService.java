package com.loftschool.loftcoinoct18.job;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.loftschool.loftcoinoct18.App;
import com.loftschool.loftcoinoct18.data.api.Api;
import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoinoct18.data.db.model.QuoteEntity;
import com.loftschool.loftcoinoct18.data.model.Fiat;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;
import com.loftschool.loftcoinoct18.utils.CurrencyFormatter;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SyncRateJobService extends JobService {

    public static final String EXTRA_SYMBOL = "symbol";

    public static final String TAG = "SyncRateJobService";

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

    private void handleError(Throwable error) {

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
                showRateChangedNotification(newCoin, newQuote.price - oldQuote.price, fiat);
            } else {
                Log.i(TAG, "Price not changed: ");
            }
        }

        database.saveCoins(newCoins);
    }

    private void showRateChangedNotification(CoinEntity newCoin, double v, Fiat fiat) {

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
