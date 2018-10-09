package com.loftschool.loftcoinoct18.data.prefs;

import com.loftschool.loftcoinoct18.data.model.Fiat;

public interface Prefs {
    //Если запуск первый - показываем Welcome-screen
    boolean isFirstLaunch();
    void setFirstLaunch(Boolean firstLaunch);

    Fiat getFiatCurrency();

    void setFiatCurrency(Fiat currency);
}
