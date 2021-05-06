package com.example.myoriginalmalapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myoriginalmalapp.AccessAPI;
import com.example.myoriginalmalapp.AppConfig;
import com.example.myoriginalmalapp.R;
import com.example.myoriginalmalapp.SingletonQueue;
import com.example.myoriginalmalapp.userobject.UserObject;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Home_Activity extends AppCompatActivity
{
    private final AccessAPI accessAPI = AppConfig.getAccessApi();
    private final String user_list_string = UserAnimeListParentFragment.class.getSimpleName();
    private final String anime_string = FragmentAnime.class.getSimpleName();
    private final String seasonal_string = FragmentSeason.class.getSimpleName();
    private final String user_schedule_string = FragmentSchedule.class.getSimpleName();

    public static HashMap<String, Integer> items = new HashMap<String, Integer>();

    private int fragment_initialization_count;

    public int getFragment_initialization_count() {
        return fragment_initialization_count;
    }

    public void setFragment_initialization_count(int fragment_initialization_count) {
        this.fragment_initialization_count = fragment_initialization_count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        findViewById(R.id.progressLayoutHomeId).setVisibility(View.VISIBLE);
        findViewById(R.id.home_constraint_layout_id).setAlpha((float) 0.4);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (savedInstanceState == null)
        {
            userInfo();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            UserAnimeListParentFragment fragment = new UserAnimeListParentFragment();

            fragmentTransaction.add(R.id.frag_containter_id, fragment, UserAnimeListParentFragment.class.getSimpleName());
            fragmentTransaction.setPrimaryNavigationFragment(fragment);
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.commit();
        }

        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Home_Activity.this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        TabLayout tabLayout = findViewById(R.id.tabLayoutHome);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position)
                {
                    case 0:
                    {

                        String tag = UserAnimeListParentFragment.class.getSimpleName();
                        selectFragment(new UserAnimeListParentFragment(), tag);

//                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        Fragment current_frag = getSupportFragmentManager().getPrimaryNavigationFragment();
//
//
//                        if(current_frag != null)
//                        {
//                            fragmentTransaction.hide(current_frag);
//                        }
//
//                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
//
//                        if (fragment == null)
//                        {
//                            fragment = new UserAnimeListParentFragment();
//                            //ANOTHER WAY TO ADD FRAGMENTS:
//                            //fragmentTransaction.add(R.id.frag_containter_id, MainActivityFrag.class, null, MainActivityFrag.class.getSimpleName());
//                            //HOW YOU REPLACE FRAGMENTS: (THIS DO NOT KEEP THEM, ALWAYS CREATES NEW ONE)
//                            //fragmentTransaction.replace(R.id.frag_containter_id, MainActivityFrag.class, null, MainActivityFrag.class.getSimpleName());
//                            fragmentTransaction.add(R.id.frag_containter_id, fragment, tag);
//                        }
//                        fragmentTransaction.show(fragment);
//
//                        fragmentTransaction.setPrimaryNavigationFragment(fragment);
//                        fragmentTransaction.setReorderingAllowed(true);
//                        fragmentTransaction.commit();
                        break;
                    }

                    case 1:
                    {
                        String tag = FragmentAnime.class.getSimpleName();
                        selectFragment(new FragmentAnime(), tag);
                        break;
                    }
                    case 2:
                    {
                        String tag = FragmentSeason.class.getSimpleName();
                        selectFragment(new FragmentSeason(), tag);
                        break;

                    }
                    case 3:
                    {
                        String tag = FragmentSchedule.class.getSimpleName();
                        selectFragment(new FragmentSchedule(), tag);
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileWriter writer = new FileWriter(new File(Home_Activity.this.getFilesDir(), "user_info.json"));
                    writer.write("");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Home_Activity.this, LoginActivity.class);
                startActivity(intent);
                Home_Activity.this.finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int item_id = item.getItemId();
        int item_synchronize_id = R.id.synchronize;

        if (item_id == item_synchronize_id)
        {

            userInfo();
            Fragment current_frag = getSupportFragmentManager().getPrimaryNavigationFragment();
            String tag = current_frag.getTag();

            if (tag.equals(anime_string))
            {

                FragmentAnime fragmentAnime = (FragmentAnime) current_frag;
                ((FragmentAnimeChildAll) fragmentAnime.getChildFragmentManager()
                        .findFragmentById((R.id.top_ranking_fragment)))
                        .getGeneralAnimeAdapter()
                        .getCurrentList()
                        .getDataSource()
                        .invalidate();


                ((FragmentAnimeChildAiring) fragmentAnime.getChildFragmentManager()
                        .findFragmentById((R.id.top_airing_fragment)))
                        .getGeneralAnimeAdapter()
                        .getCurrentList()
                        .getDataSource()
                        .invalidate();

                ((FragmentAnimeChildUpcoming) fragmentAnime.getChildFragmentManager()
                        .findFragmentById((R.id.top_upcoming_fragment)))
                        .getGeneralAnimeAdapter()
                        .getCurrentList()
                        .getDataSource()
                        .invalidate();

                ((FragmentAnimeChildByPopularity) fragmentAnime.getChildFragmentManager()
                        .findFragmentById((R.id.popularity_fragment)))
                        .getGeneralAnimeAdapter()
                        .getCurrentList()
                        .getDataSource()
                        .invalidate();

                ((FragmentAnimeChildSuggestions) fragmentAnime.getChildFragmentManager()
                        .findFragmentById((R.id.suggestions_fragment)))
                        .getGeneralAnimeAdapter()
                        .getCurrentList()
                        .getDataSource()
                        .invalidate();

                Snackbar.make(Home_Activity.this, findViewById(R.id.frag_containter_id), "Refreshed!", BaseTransientBottomBar.LENGTH_LONG).show();
                return true;
            }


            if (tag.equals(user_list_string) || tag.equals(user_schedule_string))
            {
                Toast.makeText(Home_Activity.this, "Refreshing List...", Toast.LENGTH_SHORT).show();
                HashMap<String, UserAnimeListChildFragment> user_frag_list = AppConfig.getFragmentUserAnimeListMap();

                int count = 0;
                for (UserAnimeListChildFragment fragment : user_frag_list.values())
                {
                    count++;
                    int finalCount = count;
                    fragment.fragmentRequestMethod(new UserAnimeListChildFragment.notifyUpdateListener() {
                        @Override
                        public void onNotifyUpdateListener() {
                            if (finalCount == user_frag_list.size()) {

                                FragmentSchedule fragmentSchedule = (FragmentSchedule) getSupportFragmentManager().findFragmentByTag(user_schedule_string);
                                if (fragmentSchedule == null)
                                {
                                    Snackbar.make(Home_Activity.this, findViewById(R.id.frag_containter_id), "Refreshed!", BaseTransientBottomBar.LENGTH_LONG).show();
                                }
                                else
                                {
                                    fragmentSchedule.updateLists(new FragmentSchedule.updateListsListener() {
                                        @Override
                                        public void onUpdateListsResponse() {
                                            Snackbar.make(Home_Activity.this, findViewById(R.id.frag_containter_id), "Refreshed!", BaseTransientBottomBar.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
                return true;
            }


            if (tag.equals(seasonal_string))
            {
                FragmentSeason fragmentSeason = ((FragmentSeason) getSupportFragmentManager().findFragmentByTag(seasonal_string));
                ((FragmentChildSeason) fragmentSeason.getChildFragmentManager().getPrimaryNavigationFragment())
                        .getAdapter().getCurrentList().getDataSource().invalidate();

                Snackbar.make(Home_Activity.this, findViewById(R.id.frag_containter_id), "Refreshed!", BaseTransientBottomBar.LENGTH_LONG).show();
                return true;
            }
        }
        return true;
    }

    public void userInfo()
    {
        SingletonQueue.getSingleton(Home_Activity.this).addToRequestQueue(accessAPI.userInfoRequest(new AccessAPI.UserInfoRequestListener() {
            @Override
            public void userInfoRequestResponse(UserObject userObject) {
                TextView nome = findViewById(R.id.user_name_id);
                nome.setText(userObject.getName());

                TextView watching = findViewById(R.id.user_watching_id);
                watching.setText(("Watching: "+userObject.getAnime_statistics().getNum_items_watching()));

                TextView plan_to_watch = findViewById(R.id.plan_to_watch_user_id);
                plan_to_watch.setText(("Plan to Watch: "+userObject.getAnime_statistics().getNum_items_plan_to_watch()));

                TextView completed = findViewById(R.id.completed_user_id);
                completed.setText(("Completed: "+userObject.getAnime_statistics().getNum_items_completed()));

                TextView on_hold = findViewById(R.id.on_hold_user_id);
                on_hold.setText(("On Hold: "+userObject.getAnime_statistics().getNum_items_on_hold()));

                TextView dropped = findViewById(R.id.dropped_user_id);
                dropped.setText(("Dropped: "+userObject.getAnime_statistics().getNum_items_dropped()));

                TextView days = findViewById(R.id.days_user_id);
                days.setText(("Days Spent: "+userObject.getAnime_statistics().getNum_days()));
            }

            @Override
            public void onErrorResponse() {

            }
        }));
    }

    private void selectFragment(Fragment new_fragment, String tag)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment current_frag = getSupportFragmentManager().getPrimaryNavigationFragment();


        if(current_frag != null)
        {
            fragmentTransaction.hide(current_frag);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (fragment == null)
        {
            fragment = new_fragment;
            //ANOTHER WAY TO ADD FRAGMENTS:
            //fragmentTransaction.add(R.id.frag_containter_id, MainActivityFrag.class, null, MainActivityFrag.class.getSimpleName());
            //HOW YOU REPLACE FRAGMENTS: (THIS DO NOT KEEP THEM, ALWAYS CREATES NEW ONE)
            //fragmentTransaction.replace(R.id.frag_containter_id, MainActivityFrag.class, null, MainActivityFrag.class.getSimpleName());
            fragmentTransaction.add(R.id.frag_containter_id, fragment, tag);
        }
        fragmentTransaction.show(fragment);

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commit();
    }
}
