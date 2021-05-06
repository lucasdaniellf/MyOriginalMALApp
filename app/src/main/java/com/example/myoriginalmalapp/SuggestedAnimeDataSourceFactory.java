package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.myoriginalmalapp.animeobject.AnimeObject;

public class SuggestedAnimeDataSourceFactory extends DataSource.Factory<Integer, AnimeObject> {

    //creating the mutable live data
    private final MutableLiveData<PageKeyedDataSource<Integer, AnimeObject>> suggestedAnimeLiveDataSource = new MutableLiveData<>();
    private final Context context;

    public SuggestedAnimeDataSourceFactory(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public DataSource<Integer, AnimeObject> create() {
        //getting our data source object
        SuggestedAnimeDataSource suggestedlAnimeDataSource = new SuggestedAnimeDataSource(context);
        //posting the datasource to get the values
        suggestedAnimeLiveDataSource.postValue(suggestedlAnimeDataSource);

        //returning the datasource
        return suggestedlAnimeDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, AnimeObject>> getSuggestedAnimeLiveDataSource() {
        return suggestedAnimeLiveDataSource;
    }
}