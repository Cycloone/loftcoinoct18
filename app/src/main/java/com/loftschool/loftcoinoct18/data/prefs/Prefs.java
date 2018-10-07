package com.loftschool.loftcoinoct18.data.prefs;

public interface Prefs {
    //Если запуск первый - показываем Welcome-screen
    boolean isFirstLaunch();
    void setFirstLaunch(Boolean firstLaunch);
}
