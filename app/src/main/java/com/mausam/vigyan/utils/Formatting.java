package com.mausam.vigyan.utils;

import android.content.Context;

import com.mausam.vigyan.utils.formatters.WeatherFormatter;

public class Formatting {

    private Context context;

    public Formatting(Context context) {
        this.context = context;
    }

    public String setWeatherIcon(int actualId, boolean day) {
        return WeatherFormatter.getWeatherIconAsText(actualId, day, context);
    }
}
