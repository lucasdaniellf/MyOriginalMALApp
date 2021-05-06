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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoriginalmalapp.GeneralAnimeAdapter;
import com.example.myoriginalmalapp.GeneralAnimeViewModel;
import com.example.myoriginalmalapp.GeneralAnimeViewModelFactory;
import com.example.myoriginalmalapp.R;
import com.example.myoriginalmalapp.animeobject.AnimeObject;

public class FragmentAnimeChildAll extends Fragment
{
    private GeneralAnimeAdapter generalAnimeAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anime_child, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_anime_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        String type = "all";

        GeneralAnimeViewModel generalAnimeViewModel =  new ViewModelProvider(this, new GeneralAnimeViewModelFactory(view.getContext(), type)).get(GeneralAnimeViewModel.class);
        generalAnimeAdapter = new GeneralAnimeAdapter(view.getContext());

        generalAnimeViewModel.getGeneralAnimePagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<AnimeObject>>() {
            @Override
            public void onChanged(@Nullable PagedList<AnimeObject> items) {
                //in case of any changes
                //submitting the items to adapter
                generalAnimeAdapter.submitList(items);
            }
        });

        recyclerView.setAdapter(generalAnimeAdapter);
    }

    public GeneralAnimeAdapter getGeneralAnimeAdapter() {
        return generalAnimeAdapter;
    }
}

