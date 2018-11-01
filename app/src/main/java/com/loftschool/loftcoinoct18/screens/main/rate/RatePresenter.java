package com.loftschool.loftcoinoct18.screens.main.rate;

import com.loftschool.loftcoinoct18.data.model.Fiat;

public interface RatePresenter {

    void attachView(RateView view);

    void detachView();

    void getRate();

    void onRefresh();

    void onMenuItemCurrencyClick();

    void onFiatCurrencySelected(Fiat currency);

    void onRateLongClick(String symbol);
}
