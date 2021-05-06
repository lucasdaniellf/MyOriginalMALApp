package com.example.myoriginalmalapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myoriginalmalapp.AppConfig;
import com.example.myoriginalmalapp.R;
import com.example.myoriginalmalapp.animeobject.AnimeObject;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentSchedule extends Fragment
{
    private final String[] days = new String[7];
    private final HashMap<String, FragmentScheduleContent> user_schedule_list = AppConfig.getFragmentUserScheduleContent();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        TabLayout tabLayout = view.findViewById(R.id.tabLayout_schedule);
        for (int i=0; i<tabLayout.getTabCount();i++)
        {
            days[i] = String.valueOf(tabLayout.getTabAt(i).getText()).toLowerCase();
        }

        for (String day : days) {
            user_schedule_list.put(day.toLowerCase(), new FragmentScheduleContent(new ArrayList<>(), day));
        }

        ViewPager2 viewPager = view.findViewById(R.id.viewPager_schedule);
        viewPager.setOffscreenPageLimit(7);

        FragmentStateAdapter adapter = new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return user_schedule_list.get(days[position]);
            }

            @Override
            public int getItemCount() {
                return 7;
            }
        };

        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        updateLists(new updateListsListener() {
            @Override
            public void onUpdateListsResponse() {

            }
        });
    }


    public interface updateListsListener
    {
        void onUpdateListsResponse();
    }


    public void updateLists(updateListsListener listener)
    {

        HashMap<String, HashMap<String, AnimeObject>> userScheduleMapDB = AppConfig.getUserScheduleMapDB();
        for (String day : days) {
            userScheduleMapDB.remove(day);
            userScheduleMapDB.put(day, new HashMap<>());
        }


        HashMap<String, AnimeObject> weekly_anime = AppConfig.getUserScheduleDB();
        for (AnimeObject anime: weekly_anime.values()) {
            if (anime.getNode().getBroadcast() != null) {
                userScheduleMapDB.get(anime.getNode().getBroadcast().getDay_of_the_week().toLowerCase()).put(anime.getNode().getTitle(), anime);
            }
        }

        for (String day : days)
        {
            user_schedule_list.get(day).notifyArrayListUpdate();
        }

        listener.onUpdateListsResponse();
    }

}

