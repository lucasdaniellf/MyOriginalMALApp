package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.myoriginalmalapp.animeobject.AnimeObject;

public class GeneralAnimeViewModel extends ViewModel {

    //creating livedata for PagedList  and PagedKeyedDataSource
    private final LiveData<PagedList<AnimeObject>> generalAnimePagedList;
    private final LiveData<PageKeyedDataSource<Integer, AnimeObject>> liveDataSource;

    //constructor
    public GeneralAnimeViewModel(Context context, String ranking_type) {
        //getting our data source factory
        GeneralAnimeDataSourceFactory generalAnimeDataSourceFactory = new GeneralAnimeDataSourceFactory(context, ranking_type);

        //getting the live data source from data source factory
        liveDataSource = generalAnimeDataSourceFactory.getGeneralAnimeLiveDataSource();

        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(GeneralAnimeDataSource.PAGE_SIZE).build();

        //Building the paged list
        generalAnimePagedList = (new LivePagedListBuilder<>(generalAnimeDataSourceFactory, pagedListConfig)).build();
    }

    public LiveData<PagedList<AnimeObject>> getGeneralAnimePagedList() {
        return generalAnimePagedList;
    }

    public LiveData<PageKeyedDataSource<Integer, AnimeObject>> getLiveDataSource() {
        return liveDataSource;
    }
}