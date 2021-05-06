package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SuggestedAnimeViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;


    public SuggestedAnimeViewModelFactory(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(new SuggestedAnimeViewModel(context));
    }
}