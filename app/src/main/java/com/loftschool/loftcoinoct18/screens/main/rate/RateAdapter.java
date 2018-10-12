package com.loftschool.loftcoinoct18.screens.main.rate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.loftschool.loftcoinoct18.data.api.model.Coin;
import com.loftschool.loftcoinoct18.data.prefs.Prefs;

import java.util.Collections;
import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateViewHolder> {
    private List<Coin> coins = Collections.emptyList();

    private Prefs prefs;

     RateAdapter(Prefs prefs) {
         this.prefs = prefs;
    }

    public void setCoins(List<Coin> coins) {
        this.coins = coins;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RateAdapter.RateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RateAdapter.RateViewHolder rateViewHolder, int i) {

    }

    public void setHasStableIds(boolean b) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class RateViewHolder extends RecyclerView.ViewHolder {
        public RateViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
