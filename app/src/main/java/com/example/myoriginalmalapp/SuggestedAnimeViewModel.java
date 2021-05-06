package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.myoriginalmalapp.animeobject.AnimeObject;

public class SuggestedAnimeViewModel extends ViewModel {

    //creating livedata for PagedList  and PagedKeyedDataSource
    private final LiveData<PagedList<AnimeObject>> suggestedAnimePagedList;
    private final LiveData<PageKeyedDataSource<Integer, AnimeObject>> liveDataSource;

    //constructor
    public SuggestedAnimeViewModel(Context context) {
        //getting our data source factory
        SuggestedAnimeDataSourceFactory suggestedAnimeDataSourceFactory = new SuggestedAnimeDataSourceFactory(context);

        //getting the live data source from data source factory
        liveDataSource = suggestedAnimeDataSourceFactory.getSuggestedAnimeLiveDataSource();

        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(GeneralAnimeDataSource.PAGE_SIZE).build();

        //Building the paged list
        suggestedAnimePagedList = (new LivePagedListBuilder<>(suggestedAnimeDataSourceFactory, pagedListConfig)).build();
    }

    public LiveData<PagedList<AnimeObject>> getSuggestedAnimePagedList() {
        return suggestedAnimePagedList;
    }

    public LiveData<PageKeyedDataSource<Integer, AnimeObject>> getLiveDataSource() {
        return liveDataSource;
    }
}