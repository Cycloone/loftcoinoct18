package com.loftschool.loftcoinoct18.data.model;

import android.support.annotation.DrawableRes;

import com.loftschool.loftcoinoct18.R;

public enum Currency {
    DASH(R.drawable.ic_currency_dash),
    DOGE(R.drawable.ic_currency_doge),
    XMR(R.drawable.ic_currency_xmr),
    XRP(R.drawable.ic_currency_xrp),
    BTC(R.drawable.ic_currency_btc),
    ETH(R.drawable.ic_currency_eth);

    public int iconRes;

    Currency(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
    }

    public static Currency getCurrency(String currency){
        try{
            return Currency.valueOf(currency);
        } catch (IllegalArgumentException e){
            return null;
        }
    }
}
