package com.example.myoriginalmalapp;

import com.example.myoriginalmalapp.activities.FragmentScheduleContent;
import com.example.myoriginalmalapp.animeobject.AnimeObject;
import com.example.myoriginalmalapp.activities.UserAnimeListChildFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class AppConfig
{
    private static final AccessAPI accessAPI = new AccessAPI();
    private static final Gson gson = new Gson();
    private static final HashMap<String, UserAnimeListChildFragment> fragmentUserAnimeListMap = new HashMap<>();
    private static final HashMap<String, FragmentScheduleContent> fragmentUserScheduleContent = new HashMap<>();
    private static final HashMap<String, ArrayList<AnimeObject>> user_schedule_by_status = new HashMap<>();

    private static final HashMap<String, HashMap<String, AnimeObject>> UserAnimeListDB = new HashMap<>();
    private static final HashMap<String, HashMap<String, AnimeObject>> UserScheduleMapDB = new HashMap<>();
    private static final HashMap<String, AnimeObject> UserScheduleDB = new HashMap<>();

    public static HashMap<String, HashMap<String, AnimeObject>> getUserScheduleMapDB() {
        return UserScheduleMapDB;
    }

    public static HashMap<String, AnimeObject> getUserScheduleDB() {
        return UserScheduleDB;
    }

    public static HashMap<String, HashMap<String, AnimeObject>> getUserAnimeListDB() {
        return UserAnimeListDB;
    }

    public static HashMap<String, ArrayList<AnimeObject>> getUser_schedule_by_status() {
        return user_schedule_by_status;
    }

    public static HashMap<String, UserAnimeListChildFragment> getFragmentUserAnimeListMap() {
        return fragmentUserAnimeListMap;
    }

    public static HashMap<String, FragmentScheduleContent> getFragmentUserScheduleContent() {
        return fragmentUserScheduleContent;
    }

    public static AccessAPI getAccessApi()
    {
        return accessAPI;
    }
    public static Gson getGson() { return gson; }

}
