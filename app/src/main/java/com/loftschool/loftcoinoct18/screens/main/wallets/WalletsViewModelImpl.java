package com.loftschool.loftcoinoct18.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.loftschool.loftcoinoct18.App;
import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.db.model.Transaction;
import com.loftschool.loftcoinoct18.data.db.model.TransactionModel;
import com.loftschool.loftcoinoct18.data.db.model.Wallet;
import com.loftschool.loftcoinoct18.data.db.model.WalletModel;
import com.loftschool.loftcoinoct18.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletsViewModelImpl extends WalletsViewModel {

    private SingleLiveEvent<Object> selectCurrency = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> walletsVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> newWalletVisible = new MutableLiveData<>();
    private MutableLiveData<List<WalletModel>> walletsItems = new MutableLiveData<>();
    private MutableLiveData<List<TransactionModel>> transactionsItems = new MutableLiveData<>();

    private Database database;

    private CompositeDisposable disposables = new CompositeDisposable();

    public WalletsViewModelImpl(@NonNull Application application) {
        super(application);

        database = ((App) application).getDatabase();
    }

    @Override
    public void getWallets() {
        getWalletsInner();

    }

    private void getWalletsInner() {
        Disposable disposable = database.getWallets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wallets -> {
                    if (wallets.isEmpty()) {
                        walletsVisible.setValue(false);
                        newWalletVisible.setValue(true);
                    } else {
                        walletsVisible.setValue(true);
                        newWalletVisible.setValue(false);

                        if (walletsItems.getValue() == null || walletsItems.getValue().isEmpty()) {
                            WalletModel model = wallets.get(0);
                            String walletId = model.wallet.walletId;
                            getTransaction(walletId);
                        }

                        walletsItems.setValue(wallets);
                    }

                });
        disposables.add(disposable);
    }

    private void getTransaction(String walletId) {
        Disposable disposable = database.getTransactions(walletId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactions -> transactionsItems.setValue(transactions)
                );
        disposables.add(disposable);
    }


    @Override
    public void onNewWalletClick() {
        selectCurrency.postValue(new Object());
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        Wallet wallet = randomWallet(coin);
        List<Transaction> transactions = randomTransactions(wallet);

        Disposable disposable = Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                database.saveWallet(wallet);
                database.saveTransaction(transactions);
                return new Object();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();

        disposables.add(disposable);
    }

    private List<Transaction> randomTransactions(Wallet wallet) {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            transactions.add(randomTransaction(wallet));
        }
        return transactions;
    }

    private Transaction randomTransaction(Wallet wallet) {
        Random random = new Random();

        long startDate = 148322880000L;
        long nowDate = System.currentTimeMillis();
        long date = startDate + (long) (random.nextDouble() * (nowDate - startDate));

        double amount = 2 * random.nextDouble();
        boolean amountSign = random.nextBoolean();

        return new Transaction(wallet.walletId, wallet.currencyId, amountSign ? amount : -amount, date);
    }

    private Wallet randomWallet(CoinEntity coin) {
        Random random = new Random();
        return new Wallet(UUID.randomUUID().toString(), coin.id, 10 * random.nextDouble());
    }

    @Override
    public LiveData<Object> selectCurrency() {
        return selectCurrency;
    }

    @Override
    public LiveData<Boolean> walletsVisible() {
        return walletsVisible;
    }

    @Override
    public LiveData<Boolean> newWalletVisible() {
        return newWalletVisible;
    }

    @Override
    public LiveData<List<WalletModel>> wallets() {
        return walletsItems;
    }

    @Override
    public LiveData<List<TransactionModel>> transactions(){
        return transactionsItems;
    }
}
