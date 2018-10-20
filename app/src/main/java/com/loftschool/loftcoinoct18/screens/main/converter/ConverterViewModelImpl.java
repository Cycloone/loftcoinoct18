package com.loftschool.loftcoinoct18.screens.main.converter;

import android.os.Bundle;

import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.utils.CurrencyFormatter;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class ConverterViewModelImpl implements ConverterViewModel {

    private static final String KEY_SOURCE_CURRENCY = "source_currency";
    private static final String KEY_DESTINATION_CURRENCY = "destination_currency";

    private BehaviorSubject<String> sourceCurrency = BehaviorSubject.create();
    private BehaviorSubject<String> destinationCurrency = BehaviorSubject.create();
    private BehaviorSubject<String> destinationAmount = BehaviorSubject.create();

    private PublishSubject<Object> selectSourceCurrency = PublishSubject.create();
    private PublishSubject<Object> selectDestinationCurrency = PublishSubject.create();

    private Database database;
    private CompositeDisposable disposables = new CompositeDisposable();

    private CoinEntity sourceCoin;
    private CoinEntity destinationCoin;

    private String sourceCurrencySymbol = "BTC";
    private String destinationCurrencySymbol = "ETH";

    private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

    private String sourceAmountValue = "";

    public ConverterViewModelImpl(Bundle savedInstanceState, Database database) {
        this.database = database;
        if (savedInstanceState != null) {
            sourceCurrencySymbol = savedInstanceState.getString(KEY_SOURCE_CURRENCY);
            destinationCurrencySymbol = savedInstanceState.getString(KEY_DESTINATION_CURRENCY);
        }

        loadCoins();
    }

    private void loadCoins() {
        Disposable disposable1 = Observable.fromCallable((Callable<CoinEntity>) () -> database.getCoin(sourceCurrencySymbol))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinEntity -> {
                    onSourceCurrencySelected(coinEntity);
                });

        Disposable disposable2 = Observable.fromCallable((Callable<CoinEntity>) () -> database.getCoin(destinationCurrencySymbol))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinEntity -> {
                    onDestinationCurrencySelected(coinEntity);
                });


        disposables.add(disposable1);
        disposables.add(disposable2);
    }

    @Override
    public Observable<String> sourceCurrency() {
        return sourceCurrency;
    }

    @Override
    public Observable<String> destinationCurrency() {
        return destinationCurrency;
    }

    @Override
    public Observable<String> destinationAmount() {
        return destinationAmount;
    }

    @Override
    public Observable<Object> selectSourceCurrency() {
        return selectSourceCurrency;
    }

    @Override
    public Observable<Object> selectDestinationCurrency() {
        return selectDestinationCurrency;
    }

    @Override
    public void onSourceAmountChange(String amount) {
        sourceAmountValue = amount;
        convert();
    }

    private void convert() {
        if (sourceAmountValue.isEmpty()) {
            destinationAmount.onNext("");
            return;
        }

        if (sourceCoin == null || destinationCoin == null) {
            return;
        }

        double sourceValue = Double.parseDouble(sourceAmountValue);
        double destinationValue = sourceValue * sourceCoin.usd.price / destinationCoin.usd.price;
        String destinationAmountValue = String.valueOf(currencyFormatter.formatForConverter(destinationValue));

        destinationAmount.onNext(destinationAmountValue);
    }

    @Override
    public void onSourceCurrencyClick() {
        selectSourceCurrency.onNext(new Object());
    }

    @Override
    public void onDestinationCurrencyClick() {
        selectDestinationCurrency.onNext(new Object());
    }

    @Override
    public void onSourceCurrencySelected(CoinEntity coin) {
        sourceCoin = coin;
        sourceCurrencySymbol = coin.symbol;
        sourceCurrency.onNext(coin.symbol);
        convert();
    }

    @Override
    public void onDestinationCurrencySelected(CoinEntity coin) {
        destinationCoin = coin;
        destinationCurrencySymbol = coin.symbol;
        destinationCurrency.onNext(coin.symbol);
        convert();
    }

    @Override
    public void saveState(Bundle outState) {
        outState.putString(KEY_SOURCE_CURRENCY, sourceCurrencySymbol);
        outState.putString(KEY_DESTINATION_CURRENCY, destinationCurrencySymbol);
    }
}
