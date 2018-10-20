package com.loftschool.loftcoinoct18.screens.currencies;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;

public interface CurrenciesAdapterListener {
    void onCurrencyClick(CoinEntity coin);
}
