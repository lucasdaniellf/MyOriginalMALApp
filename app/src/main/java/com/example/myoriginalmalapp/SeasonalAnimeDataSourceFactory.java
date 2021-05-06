package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.myoriginalmalapp.animeobject.AnimeObject;

public class SeasonalAnimeDataSourceFactory extends DataSource.Factory<Integer, AnimeObject> {

    //creating the mutable live data
    private final MutableLiveData<PageKeyedDataSource<Integer, AnimeObject>> seasonalAnimeLiveDataSource = new MutableLiveData<>();
    private final Context context;
    private final String season;
    private final int year;

    public SeasonalAnimeDataSourceFactory(Context context, String season, int year)
    {
        this.context = context;
        this.season = season;
        this.year = year;
    }

    @NonNull
    @Override
    public DataSource<Integer, AnimeObject> create() {
        //getting our data source object
        SeasonalAnimeDataSource seasonalAnimeDataSource = new SeasonalAnimeDataSource(context, season, year);
        //posting the datasource to get the values
        seasonalAnimeLiveDataSource.postValue(seasonalAnimeDataSource);

        //returning the datasource
        return seasonalAnimeDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, AnimeObject>> getSeasonalAnimeLiveDataSource() {
        return seasonalAnimeLiveDataSource;
    }
}