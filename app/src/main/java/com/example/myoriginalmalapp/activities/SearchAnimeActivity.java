package com.example.myoriginalmalapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myoriginalmalapp.AccessAPI;
import com.example.myoriginalmalapp.AppConfig;
import com.example.myoriginalmalapp.R;
import com.example.myoriginalmalapp.animeobject.AnimeObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class SearchAnimeActivity extends AppCompatActivity
{
    public AccessAPI accessAPI = AppConfig.getAccessApi();
    public Gson gson = AppConfig.getGson();
    public ArrayList<AnimeObject> animeList;
    private RecyclerAdapterAnimeList adapter;
    private String search_intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_anime_list_activity);

        if (savedInstanceState == null)
        {
            animeList = new ArrayList<>();
            Intent intent = getIntent();
            search_intent = intent.getStringExtra("search");

            FragmentSearchContent fragment = new FragmentSearchContent(search_intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.search_activity_content, fragment)
                    .setReorderingAllowed(true)
                    .setPrimaryNavigationFragment(fragment)
                    .commit();
        }

        Toolbar toolbar = findViewById(R.id.toolbar_search_activity_id);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SearchView searchView = findViewById(R.id.searchView_search_anime_activity_id);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query!=null)
                {
                    search_intent = String.valueOf(searchView.getQuery());

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.search_activity_content, new FragmentSearchContent(search_intent))
                            .setReorderingAllowed(true)
                            .commit();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // ------- ADDING SEARCHVIEW TO TOOLBAR ------- //
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_anime_search, menu);
//
//        MenuItem search_item = menu.findItem(R.id.action_search);
//
//        SearchView searchView = (SearchView) search_item.getActionView();
//        searchView.setSubmitButtonEnabled(true);
//        searchView.setBackgroundColor(getResources().getColor(R.color.white));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if(query!=null)
//                {
//                    String search = (String) searchView.getQuery();
//                    requestSearchMethod(search);
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SearchAnimeActivity.this.finish();
        return true;
    }
}
