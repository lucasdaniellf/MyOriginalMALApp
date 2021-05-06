package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SeasonalAnimeViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private final String season;
    private final int year;


    public SeasonalAnimeViewModelFactory(Context context, String season, int year) {
        this.context = context;
        this.season = season;
        this.year = year;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(new SeasonalAnimeViewModel(context, season, year));
    }
}