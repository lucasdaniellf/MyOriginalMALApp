package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.myoriginalmalapp.animeobject.AnimeObject;

public class SearchAnimeDataSourceFactory extends DataSource.Factory<Integer, AnimeObject> {

    //creating the mutable live data
    private final MutableLiveData<PageKeyedDataSource<Integer, AnimeObject>> searchAnimeLiveDataSource = new MutableLiveData<>();
    private final Context context;
    private final String search;

    public SearchAnimeDataSourceFactory(Context context, String search)
    {
        this.context = context;
        this.search = search;
    }

    @NonNull
    @Override
    public DataSource<Integer, AnimeObject> create() {
        //getting our data source object
        SearchAnimeDataSource searchAnimeDataSource = new SearchAnimeDataSource(context, search);
        //posting the datasource to get the values
        searchAnimeLiveDataSource.postValue(searchAnimeDataSource);

        //returning the datasource
        return searchAnimeDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, AnimeObject>> getSearchAnimeLiveDataSource() {
        return searchAnimeLiveDataSource;
    }
}