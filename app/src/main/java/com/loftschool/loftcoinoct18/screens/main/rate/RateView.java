package com.loftschool.loftcoinoct18.screens.main.rate;

import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;

import java.util.List;

public interface RateView {

    void setCoins(List<CoinEntity> coins);

    void setRefreshing(Boolean refreshing);

    void showCurrencyDialog();
}
