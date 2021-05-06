package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.myoriginalmalapp.animeobject.AnimeObject;



public class GeneralAnimeDataSourceFactory extends DataSource.Factory<Integer, AnimeObject> {

    //creating the mutable live data
    private final MutableLiveData<PageKeyedDataSource<Integer, AnimeObject>> generalAnimeLiveDataSource = new MutableLiveData<>();
    private final Context context;
    private final String ranking_type;

    public GeneralAnimeDataSourceFactory(Context context, String ranking_type)
    {
        this.context = context;
        this.ranking_type = ranking_type;
    }

    @NonNull
    @Override
    public DataSource<Integer, AnimeObject> create() {
        //getting our data source object
        GeneralAnimeDataSource generalAnimeDataSource = new GeneralAnimeDataSource(context, ranking_type);
        //posting the datasource to get the values
        generalAnimeLiveDataSource.postValue(generalAnimeDataSource);

        //returning the datasource
        return generalAnimeDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, AnimeObject>> getGeneralAnimeLiveDataSource() {
        return generalAnimeLiveDataSource;
    }
}