package com.example.myoriginalmalapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoriginalmalapp.GeneralAnimeAdapter;
import com.example.myoriginalmalapp.R;
import com.example.myoriginalmalapp.SeasonalAnimeViewModel;
import com.example.myoriginalmalapp.SeasonalAnimeViewModelFactory;
import com.example.myoriginalmalapp.animeobject.AnimeObject;

public class FragmentChildSeason extends Fragment
{
    String season;
    int year;
    GeneralAnimeAdapter adapter;

    public FragmentChildSeason(String season, int year) {
        this.season = season;
        this.year = year;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anime_child, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_anime_id);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),4));


        SeasonalAnimeViewModel seasonalAnimeViewModel =  new ViewModelProvider(this, new SeasonalAnimeViewModelFactory(view.getContext(), season, year)).get(SeasonalAnimeViewModel.class);
        adapter = new GeneralAnimeAdapter(view.getContext());

        seasonalAnimeViewModel.getSeasonalAnimePagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<AnimeObject>>() {
            @Override
            public void onChanged(@Nullable PagedList<AnimeObject> items) {
                //in case of any changes
                //submitting the items to adapter
                adapter.submitList(items);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    public GeneralAnimeAdapter getAdapter() {
        return adapter;
    }
}

