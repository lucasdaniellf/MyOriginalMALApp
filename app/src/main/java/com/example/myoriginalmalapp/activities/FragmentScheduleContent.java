package com.example.myoriginalmalapp.activities;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoriginalmalapp.AppConfig;
import com.example.myoriginalmalapp.R;
import com.example.myoriginalmalapp.RecyclerAdapter;
import com.example.myoriginalmalapp.animeobject.AnimeObject;

import java.util.ArrayList;

public class FragmentScheduleContent extends Fragment
{
    private View view;
    private ArrayList<AnimeObject> animeList;
    private String day;
    private RecyclerAdapter adapter;

    public FragmentScheduleContent(ArrayList<AnimeObject> animeList, String day)
    {
        this.day = day;
        this.animeList = animeList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.user_anime_list_child_fragment, container, false);
        return this.view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_search_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        if (adapter == null)
        {
            adapter = new RecyclerAdapter(animeList);
        }
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<AnimeObject> getAnimeList() {
        return animeList;
    }

    public void arrayDailyListUpdate(AnimeObject anime)
    {
        if (adapter == null)
        {
            adapter = new RecyclerAdapter(animeList);
        }
        AppConfig.getUserScheduleDB().remove(anime.getNode().getTitle());
        AppConfig.getUserScheduleDB().put(anime.getNode().getTitle(), anime);

        AppConfig.getUserScheduleMapDB().get(day).remove(anime.getNode().getTitle());
        AppConfig.getUserScheduleMapDB().get(day).put(anime.getNode().getTitle(), anime);

        notifyArrayListUpdate();
    }

    public void arrayDailyListRemove(AnimeObject anime)
    {
        if (adapter == null)
        {
            adapter = new RecyclerAdapter(animeList);
        }
        AppConfig.getUserScheduleDB().remove(anime.getNode().getTitle());
        AppConfig.getUserScheduleMapDB().get(day).remove(anime.getNode().getTitle());

        notifyArrayListUpdate();
    }

    public void notifyArrayListUpdate() {
        if (adapter == null)
        {
            adapter = new RecyclerAdapter(animeList);
        }
        animeList.clear();
        animeList.addAll(AppConfig.getUserScheduleMapDB().get(day).values());
        adapter.notifyDataSetChanged();
    }
}
