package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.myoriginalmalapp.animeobject.AnimeObject;

public class SeasonalAnimeViewModel extends ViewModel {

    //creating livedata for PagedList  and PagedKeyedDataSource
    private final LiveData<PagedList<AnimeObject>> seasonalAnimePagedList;
    private final LiveData<PageKeyedDataSource<Integer, AnimeObject>> liveDataSource;

    //constructor
    public SeasonalAnimeViewModel(Context context, String season, int year) {
        //getting our data source factory
        SeasonalAnimeDataSourceFactory seasonalAnimeDataSourceFactory = new SeasonalAnimeDataSourceFactory(context, season, year);

        //getting the live data source from data source factory
        liveDataSource = seasonalAnimeDataSourceFactory.getSeasonalAnimeLiveDataSource();

        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(GeneralAnimeDataSource.PAGE_SIZE).build();

        //Building the paged list
        seasonalAnimePagedList = (new LivePagedListBuilder<>(seasonalAnimeDataSourceFactory, pagedListConfig)).build();
    }

    public LiveData<PagedList<AnimeObject>> getSeasonalAnimePagedList() {
        return seasonalAnimePagedList;
    }

    public LiveData<PageKeyedDataSource<Integer, AnimeObject>> getLiveDataSource() {
        return liveDataSource;
    }
}