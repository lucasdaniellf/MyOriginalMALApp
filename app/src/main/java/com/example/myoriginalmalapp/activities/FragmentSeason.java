package com.example.myoriginalmalapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myoriginalmalapp.AccessAPI;
import com.example.myoriginalmalapp.AppConfig;
import com.example.myoriginalmalapp.R;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentSeason extends Fragment
{
    private View view;
    private final AccessAPI accessAPI = AppConfig.getAccessApi();

    private String season_selected = "fall";
    private String year_selected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_season, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null)
        {
            FragmentChildSeason fragment = new FragmentChildSeason(season_selected, Calendar.getInstance().get(Calendar.YEAR));
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_season_child, fragment)
                    .setReorderingAllowed(true)
                    .setPrimaryNavigationFragment(fragment)
                    .commit();
        }


        Spinner season_spinner = view.findViewById(R.id.spinner_season_id);
        Spinner year_spinner = view.findViewById(R.id.spinner_year_id);

        ArrayList<String> season_list = new ArrayList<>();
        season_list.add("Fall");
        season_list.add("Winter");
        season_list.add("Spring");
        season_list.add("Summer");

        ArrayList<Integer> year_list = new ArrayList<>();
        int actual_year = Calendar.getInstance().get(Calendar.YEAR);
        year_selected = String.valueOf(actual_year);

        for (int i = actual_year; i >= 1985; i--)
        {
            year_list.add(i);
        }


        ArrayAdapter<String> season_adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, season_list);
        season_spinner.setAdapter(season_adapter);
        season_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                season_selected = parent.getItemAtPosition(position).toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<Integer> year_adapter = new ArrayAdapter<Integer>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, year_list);
        year_spinner.setAdapter(year_adapter);
        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year_selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button search_button = view.findViewById(R.id.button_search_id);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_season_child, new FragmentChildSeason(season_selected,Integer.parseInt(year_selected)))
                        .setReorderingAllowed(true)
                        .commit();
            }
        });
    }
}
