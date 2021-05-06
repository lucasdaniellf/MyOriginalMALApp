package com.example.myoriginalmalapp;

import android.content.Context;

import androidx.paging.PageKeyedDataSource;

import com.example.myoriginalmalapp.animeobject.AnimeObject;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class GeneralAnimeDataSource extends PageKeyedDataSource<Integer, AnimeObject> {

    //the size of a page that we want
    public static final int PAGE_SIZE = 20;

    //we will start from the first page which is 1
    private static final int OFFSET = 0;

    private final Context context;
    private final String ranking_type;

    public GeneralAnimeDataSource(Context context, String ranking_type)
    {
        this.context = context;
        this.ranking_type = ranking_type;
    }

    //this will be called once to load the initial data
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, AnimeObject> callback) {
        SingletonQueue.getSingleton(context).addToRequestQueue(AppConfig.getAccessApi().generalAnimeList(ranking_type, PAGE_SIZE, OFFSET, new AccessAPI.GeneralAnimeListener() {
            @Override
            public void onResponse(String response)
            {
                if (response != null)
                {
                    JsonObject jsonObject = AppConfig.getGson().fromJson(response, JsonObject.class);
                    ArrayList<AnimeObject> animeList = AppConfig.getGson().fromJson(jsonObject.get("data"), new TypeToken<List<AnimeObject>>(){}.getType());
                    callback.onResult(animeList, null, OFFSET + 1);
                }
            }

            @Override
            public void onErrorResponse() { }
        }));
    }

    //this will load the previous page
    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, AnimeObject> callback) {
        SingletonQueue.getSingleton(context).addToRequestQueue(AppConfig.getAccessApi().generalAnimeList(ranking_type, PAGE_SIZE, params.key, new AccessAPI.GeneralAnimeListener() {
            @Override
            public void onResponse(String response) {
                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response != null)
                {
                    JsonObject jsonObject = AppConfig.getGson().fromJson(response, JsonObject.class);
                    ArrayList<AnimeObject> animeList = AppConfig.getGson().fromJson(jsonObject.get("data"), new TypeToken<List<AnimeObject>>(){}.getType());
                    callback.onResult(animeList, adjacentKey);
                }
            }

            @Override
            public void onErrorResponse() {

            }
        }));
    }

    //this will load the next page
    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, AnimeObject> callback) {
        SingletonQueue.getSingleton(context).addToRequestQueue(AppConfig.getAccessApi().generalAnimeList(ranking_type, PAGE_SIZE, params.key, new AccessAPI.GeneralAnimeListener() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    //if the response has next page
                    //incrementing the next page number
                    JsonObject jsonObject = AppConfig.getGson().fromJson(response, JsonObject.class);
                    PagingClass paging = AppConfig.getGson().fromJson(jsonObject.get("paging"), PagingClass.class);

                    Integer key = (paging.getNext() != null) ? params.key + 1 : null;
                    ArrayList<AnimeObject> animeList = AppConfig.getGson().fromJson(jsonObject.get("data"), new TypeToken<List<AnimeObject>>() {
                    }.getType());
                    //passing the loaded data and next page value
                    callback.onResult(animeList, key);
                }
            }

            @Override
            public void onErrorResponse() {

            }
        }));
    }
}