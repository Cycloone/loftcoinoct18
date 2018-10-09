package com.loftschool.loftcoinoct18.data.api.model;

import com.google.gson.annotations.SerializedName;

public class Quote {
    public Double price;

    @SerializedName("percent_change_1h")
    public float percentChange1h;

    @SerializedName("persent_change_24h")
    public float percentChange24h;

    @SerializedName("percent_change_7d")
    public float percentChange7d;

}
