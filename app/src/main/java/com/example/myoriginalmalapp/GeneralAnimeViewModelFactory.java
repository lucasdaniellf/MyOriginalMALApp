package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class GeneralAnimeViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private final String ranking_type;


    public GeneralAnimeViewModelFactory(Context context, String ranking_type) {
        this.context = context;
        this.ranking_type = ranking_type;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(new GeneralAnimeViewModel(context, ranking_type));
    }
}