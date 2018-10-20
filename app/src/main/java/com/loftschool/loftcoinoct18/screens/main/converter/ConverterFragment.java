package com.loftschool.loftcoinoct18.screens.main.converter;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.loftschool.loftcoinoct18.App;
import com.loftschool.loftcoinoct18.R;
import com.loftschool.loftcoinoct18.data.db.Database;
import com.loftschool.loftcoinoct18.data.db.model.CoinEntity;
import com.loftschool.loftcoinoct18.data.model.Currency;
import com.loftschool.loftcoinoct18.screens.currencies.CurrenciesBottomSheet;
import com.loftschool.loftcoinoct18.screens.currencies.CurrenciesBottomSheetListener;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class ConverterFragment extends Fragment {

    private ConverterViewModel viewModel;

    private CompositeDisposable disposables = new CompositeDisposable();

    private static final String SOURCE_CURRENCY_BOTTOM_SHEET_TAG = "source_currency_bottom_sheet";
    private static final String DESTINATION_CURRENCY_BOTTOM_SHEET_TAG = "destination_currency_bottom_sheet";

    private Random random = new Random();

    private static int[] colors = {
            0xFFF5FF30,
            0xFFFFFFFF,
            0xFF2ABDF5,
            0xFFFF7416,
            0xFFFF7416,
            0xFF534FFF,
    };

    @BindView(R.id.source_currency)
    ViewGroup sourceCurrency;

    @BindView(R.id.source_amount)
    EditText sourceAmount;

    @BindView(R.id.destination_currency)
    ViewGroup destinationCurrency;

    @BindView(R.id.destination_amount)
    TextView destinationAmount;

    @BindView(R.id.converter_toolbar)
    Toolbar toolbar;

    Unbinder unbinder;

    TextView sourceCurrencySymbolText;
    ImageView sourceCurrencySymbolIcon;
    TextView sourceCurrencySymbolName;

    TextView destinationCurrencySymbolText;
    ImageView destinationCurrencySymbolIcon;
    TextView destinationCurrencySymbolName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.bottom_navigation_item_converter);

        Database database = ((App) getActivity().getApplication()).getDatabase();
        viewModel = new ConverterViewModelImpl(savedInstanceState, database);

        sourceCurrencySymbolText = sourceCurrency.findViewById(R.id.symbol_text);
        sourceCurrencySymbolIcon = sourceCurrency.findViewById(R.id.symbol_icon);
        sourceCurrencySymbolName = sourceCurrency.findViewById(R.id.currency_name);

        destinationCurrencySymbolText = destinationCurrency.findViewById(R.id.symbol_text);
        destinationCurrencySymbolIcon = destinationCurrency.findViewById(R.id.symbol_icon);
        destinationCurrencySymbolName = destinationCurrency.findViewById(R.id.currency_name);

        if (savedInstanceState == null) {
            sourceAmount.setText("1");
        }

        Fragment bottomSheetSource = getFragmentManager().findFragmentByTag(SOURCE_CURRENCY_BOTTOM_SHEET_TAG);
        if (bottomSheetSource != null) {
            ((CurrenciesBottomSheet) bottomSheetSource).setListener(sourceListener);
        }

        Fragment bottomSheetDestination = getFragmentManager().findFragmentByTag(DESTINATION_CURRENCY_BOTTOM_SHEET_TAG);
        if (bottomSheetDestination != null) {
            ((CurrenciesBottomSheet) bottomSheetDestination).setListener(destinationListener);
        }


        initOutputs();
        initInputs();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        viewModel.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    private void initOutputs() {
        Disposable disposable1 = RxTextView.afterTextChangeEvents(sourceAmount).subscribe(textViewAfterTextChangeEvent ->
                viewModel.onSourceAmountChange(textViewAfterTextChangeEvent.editable().toString())
        );

        sourceCurrency.setOnClickListener(v ->
                viewModel.onDestinationCurrencyClick());

        destinationCurrency.setOnClickListener(v ->
                viewModel.onDestinationCurrencyClick());

        disposables.add(disposable1);
    }

    private void initInputs() {
        Disposable disposable1 = viewModel.sourceCurrency().subscribe(currency ->
                bindCurrency(currency, sourceCurrencySymbolIcon, sourceCurrencySymbolText, sourceCurrencySymbolName)
        );


        Disposable disposable2 = viewModel.destinationCurrency().subscribe(currency ->
                bindCurrency(currency, destinationCurrencySymbolIcon, destinationCurrencySymbolText, destinationCurrencySymbolName)
        );

        Disposable disposable3 = viewModel.destinationAmount().subscribe(s ->
                destinationAmount.setText(s));

        Disposable disposable4 = viewModel.selectSourceCurrency().subscribe(o ->
                showCurrenciesBottomSheet(true)
        );

        Disposable disposable5 = viewModel.selectDestinationCurrency().subscribe(o ->
                showCurrenciesBottomSheet(false)
        );

        disposables.add(disposable1);
        disposables.add(disposable2);
        disposables.add(disposable3);
        disposables.add(disposable4);
        disposables.add(disposable5);
    }

    private void showCurrenciesBottomSheet(boolean source) {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();

        if (source) {
            bottomSheet.show(getFragmentManager(), SOURCE_CURRENCY_BOTTOM_SHEET_TAG);
            bottomSheet.setListener(sourceListener);
        } else {
            bottomSheet.show(getFragmentManager(), DESTINATION_CURRENCY_BOTTOM_SHEET_TAG);
            bottomSheet.setListener(destinationListener);
        }
    }

    private CurrenciesBottomSheetListener sourceListener = new CurrenciesBottomSheetListener() {
        @Override
        public void onCurrencySelected(CoinEntity coin) {
            viewModel.onSourceCurrencySelected(coin);
        }
    };

    private CurrenciesBottomSheetListener destinationListener = new CurrenciesBottomSheetListener() {
        @Override
        public void onCurrencySelected(CoinEntity coin) {
            viewModel.onDestinationCurrencySelected(coin);
        }
    };

    private void bindCurrency(String curr, ImageView symbolIcon, TextView symbolText, TextView currencyName) {
        Currency currency = Currency.getCurrency(curr);

        if (currency != null) {
            symbolIcon.setVisibility(View.VISIBLE);
            symbolText.setVisibility(View.GONE);

            symbolIcon.setImageResource(currency.iconRes);
        } else {
            symbolIcon.setVisibility(View.GONE);
            symbolText.setVisibility(View.VISIBLE);
            Drawable background = symbolText.getBackground();
            Drawable wrapped = DrawableCompat.wrap(background);
            DrawableCompat.setTint(wrapped, colors[random.nextInt(colors.length)]);

            symbolText.setText(String.valueOf(curr.charAt(0)));
        }

        currencyName.setText(curr);
    }


    @Override
    public void onDestroyView() {
        disposables.dispose();
        super.onDestroyView();
        unbinder.unbind();
    }
}
