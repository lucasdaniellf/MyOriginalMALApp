package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SearchAnimeViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private final String search;


    public SearchAnimeViewModelFactory(Context context, String search) {
        this.context = context;
        this.search = search;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(new SearchAnimeViewModel(context, search));
    }
}