package com.loftschool.loftcoinoct18.screens.main.wallets;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loftschool.loftcoinoct18.R;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.screens.currencies.CurrenciesBottomSheet;
import com.loftschool.loftcoinoct18.screens.currencies.CurrenciesBottomSheetListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class WalletsFragment extends Fragment implements CurrenciesBottomSheetListener {


    private WalletsViewModel viewModel;

    @BindView(R.id.wallets_toolbar)
    Toolbar walletsToolbar;
    @BindView(R.id.wallets_pager)
    ViewPager walletsPager;
    @BindView(R.id.new_wallet)
    CardView newWallet;
    @BindView(R.id.transactions_recycler)
    RecyclerView transactionsRecycler;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(WalletsViewModelImpl.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallets, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        walletsToolbar.setTitle(R.string.wallets_screen_title);
        walletsToolbar.inflateMenu(R.menu.menu_wallets);

        initInputs();
        initOutputs();
    }

    private void initInputs() {
        viewModel.selectCurrency().observe(this, o -> {
            showCurrenciesBottomSheet();
        });

    }

    private void initOutputs() {
        newWallet.setOnClickListener(v -> viewModel.onNewWalletClick());

        walletsToolbar.getMenu().findItem(R.id.menu_item_add_wallet).setOnMenuItemClickListener(menuItem -> {
            viewModel.onNewWalletClick();
            return true;
        });
    }

    private void showCurrenciesBottomSheet() {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();
        bottomSheet.show(getFragmentManager(), CurrenciesBottomSheet.TAG);
        bottomSheet.setListener(this);
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
