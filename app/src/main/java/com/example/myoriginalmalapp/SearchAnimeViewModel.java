package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.myoriginalmalapp.animeobject.AnimeObject;

public class SearchAnimeViewModel extends ViewModel {

    //creating livedata for PagedList  and PagedKeyedDataSource
    private final LiveData<PagedList<AnimeObject>> searchAnimePagedList;
    private final LiveData<PageKeyedDataSource<Integer, AnimeObject>> liveDataSource;

    //constructor
    public SearchAnimeViewModel(Context context, String search) {
        //getting our data source factory
        SearchAnimeDataSourceFactory searchAnimeDataSourceFactory = new SearchAnimeDataSourceFactory(context, search);

        //getting the live data source from data source factory
        liveDataSource = searchAnimeDataSourceFactory.getSearchAnimeLiveDataSource();

        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(GeneralAnimeDataSource.PAGE_SIZE).build();

        //Building the paged list
        searchAnimePagedList = (new LivePagedListBuilder<>(searchAnimeDataSourceFactory, pagedListConfig)).build();
    }

    public LiveData<PagedList<AnimeObject>> getSearchAnimePagedList() {
        return searchAnimePagedList;
    }

    public LiveData<PageKeyedDataSource<Integer, AnimeObject>> getLiveDataSource() {
        return liveDataSource;
    }
}