package com.loftschool.loftcoinoct18.screens.main.converter;

import android.os.Bundle;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;

import io.reactivex.Observable;

public interface ConverterViewModel {

    Observable<String> sourceCurrency();

    Observable<String> destinationCurrency();

    Observable<String> destinationAmount();

    Observable<Object> selectSourceCurrency();

    Observable<Object> selectDestinationCurrency();

    void onSourceAmountChange(String amount);

    void onSourceCurrencyClick();

    void onDestinationCurrencyClick();

    void onSourceCurrencySelected(CoinEntity coin);

    void onDestinationCurrencySelected(CoinEntity coin);

    void saveState(Bundle outState);

    void onDetach();

}
