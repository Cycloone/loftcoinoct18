package com.loftschool.loftcoinoct18.screens.main.rate;

public interface RatePresenter {

    void attachView(RateView view);

    void detachView();

    void getRate();

    void onRefresh();
}
