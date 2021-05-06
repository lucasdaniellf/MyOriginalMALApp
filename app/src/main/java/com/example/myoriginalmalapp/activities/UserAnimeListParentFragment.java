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
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Objects;

public class UserAnimeListParentFragment extends Fragment {

    private HashMap<String, UserAnimeListChildFragment> fragmentUserAnimeListMap;
    private final String[] tabs = new String[5];



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.user_anime_list_parent_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tabs[0] = "watching";
        tabs[1] = "plan_to_watch";
        tabs[2] = "on_hold";
        tabs[3] = "completed";
        tabs[4] = "dropped";

        fragmentUserAnimeListMap = AppConfig.getFragmentUserAnimeListMap();

        fragmentUserAnimeListMap.put(tabs[0], new UserAnimeListChildFragment(tabs[0]));
        fragmentUserAnimeListMap.put(tabs[1], new UserAnimeListChildFragment(tabs[1]));
        fragmentUserAnimeListMap.put(tabs[2], new UserAnimeListChildFragment(tabs[2]));
        fragmentUserAnimeListMap.put(tabs[3], new UserAnimeListChildFragment(tabs[3]));
        fragmentUserAnimeListMap.put(tabs[4], new UserAnimeListChildFragment(tabs[4]));

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 =  view.findViewById(R.id.viewPagerID);
        FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return Objects.requireNonNull(fragmentUserAnimeListMap.get(tabs[position]));
            }

            @Override
            public int getItemCount() {
                return tabs.length;
            }
        };

        viewPager2.setAdapter(fragmentStateAdapter);

        // It's 5 so the synchronization button of Home_Activity doesn't return nullpointer exception
        viewPager2.setOffscreenPageLimit(5);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}