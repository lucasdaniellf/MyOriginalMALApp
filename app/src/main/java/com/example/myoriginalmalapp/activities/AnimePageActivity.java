package com.example.myoriginalmalapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoriginalmalapp.AccessAPI;
import com.example.myoriginalmalapp.AppConfig;
import com.example.myoriginalmalapp.R;
import com.example.myoriginalmalapp.SingletonQueue;
import com.example.myoriginalmalapp.animeobject.AnimeObject;
import com.example.myoriginalmalapp.animeobject.GenreObject;
import com.example.myoriginalmalapp.animeobject.MyListStatusObject;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class AnimePageActivity extends AppCompatActivity
{
    private TextView warning;
    private Button button_remove;

    private final Gson gson = AppConfig.getGson();
    private String episodes_string;
    private String score_value;
    private int my_progress;
    private double my_score_value;
    private String my_status;
    private String original_status;
    private int mal_id;
    private RecyclerAdapterAnimeList adapter;

    private AnimeObject anime;

    private String anime_status(String status)
    {
        switch (status)
        {
            case "finished_airing":
            {
                return "Finished Airing";
            }
            case "currently_airing":
            {
                return "Currently Airing";
            }
            case "not_yet_aired":
                return "Not yet Aired";
        }
        return status;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anime_activity);

        findViewById(R.id.progressLayoutHomeId).setVisibility(View.VISIBLE);
        findViewById(R.id.linearLayout).setAlpha((float) 0.4);
        findViewById(R.id.btn_remove_id).setAlpha((float) 0.4);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Intent intent = getIntent();

        Toolbar mtoolbar = findViewById(R.id.toolbar_anime_activity_id);
        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //String json_anime = intent.getStringExtra("JsonAnime");
        //anime = gson.fromJson(json_anime, AnimeObject.class);

        int id = intent.getIntExtra("mal_id", 1);


        ImageView imageView = findViewById(R.id.image_anime_activity_id);
        TextView title = findViewById(R.id.title_anime_activity_id);
        TextView alternative_titles = findViewById(R.id.alternative_titles_id);
        TextView season = findViewById(R.id.season_anime_activity_id);
        this.warning = findViewById(R.id.warning_text_view);


        SeekBar episode_progress = findViewById(R.id.seekBar_episodes_anime_activity_id);
        Button minus_button = findViewById(R.id.button_minus_anime_activity_id);
        Button plus_button = findViewById(R.id.button_plus_anime_activity_id);

        TextView text_episode_progress = findViewById(R.id.episodes_progress_anime_activity_id);
        SeekBar my_score = findViewById(R.id.seekBar_score_anime_activity_id);
        TextView text_my_score = findViewById(R.id.my_score_anime_activity_id);

        TextView episodes = findViewById(R.id.episodes_value_id);
        TextView score = findViewById(R.id.score_value_id);
        TextView status = findViewById(R.id.status_value_id);
        TextView rating = findViewById(R.id.rating_value_id);
        TextView source = findViewById(R.id.source_value_id);
        TextView genres = findViewById(R.id.genres_value_id);
        TextView media_type = findViewById(R.id.media_type_value_id);
        TextView episode_duration = findViewById(R.id.episode_duration_value_id);
        TextView synopsis = findViewById(R.id.synopsis_value_id);
        RecyclerView recyclerView = findViewById(R.id.recycler_anime_details);

        recyclerView.setLayoutManager(new LinearLayoutManager(AnimePageActivity.this, RecyclerView.HORIZONTAL, false));
        adapter = new RecyclerAdapterAnimeList(new ArrayList<AnimeObject>());
        recyclerView.setAdapter(adapter);

        button_remove = findViewById(R.id.btn_remove_id);
        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingletonQueue.getSingleton(AnimePageActivity.this).addToRequestQueue(AppConfig.getAccessApi().removeFromAnimeList(mal_id, new AccessAPI.onResponseUpdateUserAnimeList() {
                    @Override
                    public void onUpdateUserAnimeListListener() {

                        HashMap<String, HashMap<String, AnimeObject>> statusHashMap = AppConfig.getUserAnimeListDB();
                        statusHashMap.get(original_status).remove(anime.getNode().getTitle());

                        UserAnimeListChildFragment fragment_original_status = AppConfig.getFragmentUserAnimeListMap().get(original_status);
                        fragment_original_status.notifyUpdate(new ArrayList<>(statusHashMap.get(original_status).values()));
                        //fragment_original_status.sortList();



                        if (anime.getNode().getStatus().equals("currently_airing"))
                        {
                            FragmentScheduleContent fragmentUserScheduleContent = AppConfig.getFragmentUserScheduleContent().get(anime.getNode().getBroadcast().getDay_of_the_week().toLowerCase());

                            if (fragmentUserScheduleContent != null)
                            {
                                fragmentUserScheduleContent.arrayDailyListRemove(anime);
                            }
                            else
                            {
                                AppConfig.getUserScheduleDB().remove(anime.getNode().getTitle());
                            }
                        }

                        if(warning != null)
                        {
                            Toast.makeText(AnimePageActivity.this, "Removed from List!", Toast.LENGTH_SHORT).show();
                            warning.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onErrorResponse() {
                        AppConfig.getAccessApi().refreshToken(AppConfig.getAccessApi().getRefresh_token(), AnimePageActivity.this, new AccessAPI.onAuthTokenListener() {

                            @Override
                            public void onStatusCodeResponse(int status_code) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AnimePageActivity.this);
                                builder.setMessage("We're refreshing your token, please try again.\nIf you keep getting this message please relogin.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.create();
                                builder.show();
                            }
                        });
                    }
                }));
            }
        });

        // --------------------------STATUS BUTTON---------------------------//
        HashMap<String, ToggleButton> button_my_status_list = new HashMap<>();

        ToggleButton button_watching = findViewById(R.id.button_watching);
        ToggleButton button_on_hold = findViewById(R.id.button_on_hold);
        ToggleButton button_plan_to_watch = findViewById(R.id.button_plan_to_watch);
        ToggleButton button_completed = findViewById(R.id.button_completed);
        ToggleButton button_dropped = findViewById(R.id.button_dropped);

        button_my_status_list.put("watching", button_watching);
        button_my_status_list.put("on_hold", button_on_hold);
        button_my_status_list.put("plan_to_watch", button_plan_to_watch);
        button_my_status_list.put("completed", button_completed);
        button_my_status_list.put("dropped", button_dropped);

        SingletonQueue.getSingleton(this).addToRequestQueue(AppConfig.getAccessApi().animeDetails(id, new AccessAPI.AnimeListListener() {
            @Override
            public void onAnimeListResponse(ArrayList<AnimeObject> animeList) {
                anime = animeList.get(0);
                if (anime.getNode().getMy_list_status() == null)
                {
                    warning.setVisibility(View.VISIBLE);
                    button_remove.setEnabled(false);
                    MyListStatusObject myListStatusObject = new MyListStatusObject();
                    anime.getNode().setMy_list_status(myListStatusObject);

                    anime.getNode().getMy_list_status().setStatus("plan_to_watch");
                    anime.getNode().getMy_list_status().setNum_episodes_watched(0);
                    anime.getNode().getMy_list_status().setScore(0.0);
                }

                mal_id = anime.getNode().getId();
                original_status = anime.getNode().getMy_list_status().getStatus();
                ArrayList<AnimeObject> anime_related = anime.getNode().getRelated_anime();
                my_status = original_status;

                Objects.requireNonNull(button_my_status_list.get(original_status)).setChecked(true);
                Objects.requireNonNull(button_my_status_list.get(original_status)).setEnabled(false);
                Objects.requireNonNull(button_my_status_list.get(original_status)).setTextColor(Objects.requireNonNull(button_my_status_list.get(original_status)).getContext().getResources().getColor(R.color.black));

                CompoundButton.OnCheckedChangeListener changeListener = (buttonView, isChecked) -> {
                    if (isChecked) {
                        for (String key : button_my_status_list.keySet())
                        {
                            if (buttonView != button_my_status_list.get(key))
                            {
                                Objects.requireNonNull(button_my_status_list.get(key)).setChecked(false);
                                Objects.requireNonNull(button_my_status_list.get(key)).setEnabled(true);
                            }
                            if (buttonView == button_my_status_list.get(key))
                            {
                                my_status = key;
                                buttonView.setEnabled(false);
                                buttonView.setTextColor(buttonView.getContext().getResources().getColor(R.color.black));
                            }
                        }
                    }
                };

                button_watching.setOnCheckedChangeListener(changeListener);
                button_plan_to_watch.setOnCheckedChangeListener(changeListener);
                button_on_hold.setOnCheckedChangeListener(changeListener);
                button_completed.setOnCheckedChangeListener(changeListener);
                button_dropped.setOnCheckedChangeListener(changeListener);

                // ----------------------------STATUS BUTTON END------------------------------------//



                if (anime.getNode().getMain_picture() != null) {
                    Picasso.get().load(anime.getNode().getMain_picture().getLarge()).fit().into(imageView);
                }

                title.setText(anime.getNode().getTitle());

                if (anime.getNode().getAlternative_titles() != null) {
                    alternative_titles.setText(anime.getNode().getAlternative_titles().toString());
                }
                else
                {
                    alternative_titles.setText("");
                }

                if (anime.getNode().getStart_season() != null)
                {
                    season.setText(anime.getNode().getStart_season().toString());
                }
                else
                {
                    season.setText("Not Yet Aired");
                }


                my_progress = anime.getNode().getMy_list_status().getNum_episodes_watched();
                episodes_string = "Episodes: " + my_progress + " / " + anime.getNode().getNum_episodes();
                text_episode_progress.setText(episodes_string);

                my_score_value = anime.getNode().getMy_list_status().getScore();
                if (my_score_value != 0) {
                    score_value = "My Score: " + my_score_value;
                }
                else
                {
                    score_value = "My Score: Not yet Scored";
                }
                text_my_score.setText(score_value);

                episodes.setText(String.valueOf(anime.getNode().getNum_episodes()));
                if (anime.getNode().getMean() != null)
                {
                    score.setText(String.valueOf(anime.getNode().getMean()));
                }

                if (anime.getNode().getStatus() != null)
                {
                    status.setText(anime_status(anime.getNode().getStatus()));
                }
                if (anime.getNode().getRating() != null)
                {
                    rating.setText(anime.getNode().getRating().toUpperCase());
                }
                if (anime.getNode().getSource() != null)
                {
                    source.setText(anime.getNode().getSource());
                }

                StringBuffer genre_anime = new StringBuffer();
                if (anime.getNode().getGenres() != null)
                {
                    for (GenreObject genre: anime.getNode().getGenres())
                    {
                        genre_anime.append("  ").append(genre.getName()).append("  ");
                    }
                    genres.setText(genre_anime);
                }
                else
                {
                    genres.setText("-");
                }
                String duration = anime.getNode().getAverage_episode_duration() / 60 + " min";
                episode_duration.setText(duration);

                if (anime.getNode().getMedia_type() != null)
                {
                    media_type.setText(anime.getNode().getMedia_type());
                }

                if (anime.getNode().getSynopsis() != null)
                {
                    synopsis.setText(anime.getNode().getSynopsis());
                }
                else
                {
                    synopsis.setText("-");
                }


                episode_progress.setMax(anime.getNode().getNum_episodes());
                episode_progress.setProgress(anime.getNode().getMy_list_status().getNum_episodes_watched());
                episode_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        episodes_string = "";
                        episodes_string = "Episodes: " + progress + " / " + anime.getNode().getNum_episodes();
                        my_progress = progress;

                        text_episode_progress.setText(episodes_string);

                        if ((progress == episode_progress.getMax()) && (episode_progress.getMax() != 0))
                        {
                            for (ToggleButton button : button_my_status_list.values())
                            {
                                button.setChecked(false);
                                button.setEnabled(true);
                            }
                            Objects.requireNonNull(button_my_status_list.get("completed")).setChecked(true);
                            Objects.requireNonNull(button_my_status_list.get("completed")).setEnabled(false);
                            Objects.requireNonNull(button_my_status_list.get("completed")).setTextColor(Objects.requireNonNull(button_my_status_list.get("completed")).getContext().getResources().getColor(R.color.black));
                            my_status = "completed";
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                my_score.setProgress((int) my_score_value);
                my_score.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        score_value = "";
                        if (progress != 0)
                        {
                            score_value = "My Score: " + progress;
                        }
                        else
                        {
                            score_value = "My Score: Not Yet Scored";
                        }

                        my_score_value = progress;

                        text_my_score.setText(score_value);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                minus_button.setOnClickListener(v -> {
                    if (my_progress > 0) {
                        my_progress = my_progress - 1;
                        episode_progress.setProgress((my_progress));

                        episodes_string = "";
                        episodes_string = "Episodes: " + my_progress + " / " + anime.getNode().getNum_episodes();
                        text_episode_progress.setText(episodes_string);
                    }
                });

                plus_button.setOnClickListener(v -> {
                    if ((my_progress < episode_progress.getMax()))
                    {
                        my_progress = my_progress + 1;
                        episode_progress.setProgress(my_progress);

                        episodes_string = "";
                        episodes_string = "Episodes: " + my_progress + " / " + anime.getNode().getNum_episodes();
                        text_episode_progress.setText(episodes_string);
                    }
                    if ((anime.getNode().getNum_episodes() == 0) && (!anime.getNode().getStatus().equals("not_yet_aired")))
                    {
                        my_progress = my_progress + 1;

                        episodes_string = "";
                        episodes_string = "Episodes: " + my_progress + " / " + anime.getNode().getNum_episodes();
                        text_episode_progress.setText(episodes_string);
                    }
                });

                if (anime_related != null)
                {
                    recyclerView.setLayoutManager(new LinearLayoutManager(AnimePageActivity.this, RecyclerView.HORIZONTAL, false));
                    adapter = new RecyclerAdapterAnimeList(anime_related);
                    recyclerView.setAdapter(adapter);
                }

                findViewById(R.id.linearLayout).setAlpha((float) 1);
                findViewById(R.id.btn_remove_id).setAlpha((float) 1);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                findViewById(R.id.progressLayoutHomeId).setVisibility(View.GONE);
            }

            @Override
            public void onErrorResponse() {

                findViewById(R.id.linearLayout).setAlpha((float) 1);
                findViewById(R.id.btn_remove_id).setAlpha((float) 1);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                findViewById(R.id.progressLayoutHomeId).setVisibility(View.GONE);

                AlertDialog.Builder builder = new AlertDialog.Builder(AnimePageActivity.this);
                builder.setMessage("We're refreshing your token, please try again.\nIf you keep getting this message please relogin (or check your connction).")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
            }
        }));
//        if (anime.getNode().getMy_list_status() == null)
//        {
//            warning.setVisibility(View.VISIBLE);
//            button_remove.setEnabled(false);
//            MyListStatusObject myListStatusObject = new MyListStatusObject();
//            anime.getNode().setMy_list_status(myListStatusObject);
//
//            anime.getNode().getMy_list_status().setStatus("plan_to_watch");
//            anime.getNode().getMy_list_status().setNum_episodes_watched(0);
//            anime.getNode().getMy_list_status().setScore(0.0);
//        }
//
//        mal_id = anime.getNode().getId();
//        original_status = anime.getNode().getMy_list_status().getStatus();
//        my_status = original_status;
//
//        Objects.requireNonNull(button_my_status_list.get(original_status)).setChecked(true);
//        Objects.requireNonNull(button_my_status_list.get(original_status)).setEnabled(false);
//        Objects.requireNonNull(button_my_status_list.get(original_status)).setTextColor(Objects.requireNonNull(button_my_status_list.get(original_status)).getContext().getResources().getColor(R.color.black));
//
//        CompoundButton.OnCheckedChangeListener changeListener = (buttonView, isChecked) -> {
//            if (isChecked) {
//                for (String key : button_my_status_list.keySet())
//                {
//                    if (buttonView != button_my_status_list.get(key))
//                    {
//                        Objects.requireNonNull(button_my_status_list.get(key)).setChecked(false);
//                        Objects.requireNonNull(button_my_status_list.get(key)).setEnabled(true);
//                    }
//                    if (buttonView == button_my_status_list.get(key))
//                    {
//                        my_status = key;
//                        buttonView.setEnabled(false);
//                        buttonView.setTextColor(buttonView.getContext().getResources().getColor(R.color.black));
//                    }
//                }
//            }
//        };
//
//        button_watching.setOnCheckedChangeListener(changeListener);
//        button_plan_to_watch.setOnCheckedChangeListener(changeListener);
//        button_on_hold.setOnCheckedChangeListener(changeListener);
//        button_completed.setOnCheckedChangeListener(changeListener);
//        button_dropped.setOnCheckedChangeListener(changeListener);
//
//        // ----------------------------STATUS BUTTON END------------------------------------//
//
//
//
//        if (anime.getNode().getMain_picture() != null) {
//            Picasso.get().load(anime.getNode().getMain_picture().getLarge()).fit().into(imageView);
//        }
//
//        title.setText(anime.getNode().getTitle());
//
//        if (anime.getNode().getAlternative_titles() != null) {
//            alternative_titles.setText(anime.getNode().getAlternative_titles().toString());
//        }
//        else
//        {
//            alternative_titles.setText("");
//        }
//
//        if (anime.getNode().getStart_season() != null)
//        {
//            season.setText(anime.getNode().getStart_season().toString());
//        }
//        else
//        {
//            season.setText("Not Yet Aired");
//        }
//
//
//        my_progress = anime.getNode().getMy_list_status().getNum_episodes_watched();
//        episodes_string = "Episodes: " + my_progress + " / " + anime.getNode().getNum_episodes();
//        text_episode_progress.setText(episodes_string);
//
//        my_score_value = anime.getNode().getMy_list_status().getScore();
//        if (my_score_value != 0) {
//            score_value = "My Score: " + my_score_value;
//        }
//        else
//        {
//            score_value = "My Score: Not yet Scored";
//        }
//        text_my_score.setText(score_value);
//
//        episodes.setText(String.valueOf(anime.getNode().getNum_episodes()));
//        if (anime.getNode().getMean() != null)
//        {
//            score.setText(String.valueOf(anime.getNode().getMean()));
//        }
//
//        if (anime.getNode().getStatus() != null)
//        {
//            status.setText(anime.getNode().getStatus());
//        }
//        if (anime.getNode().getRating() != null)
//        {
//            rating.setText(anime.getNode().getRating().toUpperCase());
//        }
//        if (anime.getNode().getSource() != null)
//        {
//            source.setText(anime.getNode().getSource());
//        }
//
//        StringBuffer genre_anime = new StringBuffer();
//        if (anime.getNode().getGenres() != null)
//        {
//            for (GenreObject genre: anime.getNode().getGenres())
//            {
//                genre_anime.append(genre.getName()).append(" ");
//            }
//            genres.setText(genre_anime);
//        }
//        else
//        {
//            genres.setText("-");
//        }
//
//        if (anime.getNode().getSynopsis() != null)
//        {
//            synopsis.setText(anime.getNode().getSynopsis());
//        }
//        else
//        {
//            synopsis.setText("-");
//        }
//
//
//        episode_progress.setMax(anime.getNode().getNum_episodes());
//        episode_progress.setProgress(anime.getNode().getMy_list_status().getNum_episodes_watched());
//        episode_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                episodes_string = "";
//                episodes_string = "Episodes: " + progress + " / " + anime.getNode().getNum_episodes();
//                my_progress = progress;
//
//                text_episode_progress.setText(episodes_string);
//
//                if ((progress == episode_progress.getMax()) && (episode_progress.getMax() != 0))
//                {
//                    for (ToggleButton button : button_my_status_list.values())
//                    {
//                        button.setChecked(false);
//                        button.setEnabled(true);
//                    }
//                    Objects.requireNonNull(button_my_status_list.get("completed")).setChecked(true);
//                    Objects.requireNonNull(button_my_status_list.get("completed")).setEnabled(false);
//                    Objects.requireNonNull(button_my_status_list.get("completed")).setTextColor(Objects.requireNonNull(button_my_status_list.get("completed")).getContext().getResources().getColor(R.color.black));
//                    my_status = "completed";
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
//
//        my_score.setProgress((int) my_score_value);
//        my_score.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                score_value = "";
//                if (progress != 0)
//                {
//                    score_value = "My Score: " + progress;
//                }
//                else
//                {
//                    score_value = "My Score: Not Yet Scored";
//                }
//
//                my_score_value = progress;
//
//                text_my_score.setText(score_value);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        minus_button.setOnClickListener(v -> {
//            if (my_progress > 0) {
//                my_progress = my_progress - 1;
//                episode_progress.setProgress((my_progress));
//
//                episodes_string = "";
//                episodes_string = "Episodes: " + my_progress + " / " + anime.getNode().getNum_episodes();
//                text_episode_progress.setText(episodes_string);
//            }
//        });
//
//        plus_button.setOnClickListener(v -> {
//            if ((my_progress < episode_progress.getMax()))
//            {
//                my_progress = my_progress + 1;
//                episode_progress.setProgress(my_progress);
//
//                episodes_string = "";
//                episodes_string = "Episodes: " + my_progress + " / " + anime.getNode().getNum_episodes();
//                text_episode_progress.setText(episodes_string);
//            }
//            if ((anime.getNode().getNum_episodes() == 0) && (!anime.getNode().getStatus().equals("not_yet_aired")))
//            {
//                my_progress = my_progress + 1;
//
//                episodes_string = "";
//                episodes_string = "Episodes: " + my_progress + " / " + anime.getNode().getNum_episodes();
//                text_episode_progress.setText(episodes_string);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_anime_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int item_id = item.getItemId();
        int save = R.id.update_id;
        if (item_id == save) {
            Toast.makeText(AnimePageActivity.this, "Updating...", Toast.LENGTH_SHORT).show();
            SingletonQueue.getSingleton(AnimePageActivity.this).addToRequestQueue(AppConfig.getAccessApi().updateUserAnimeList(mal_id, my_status, my_progress, (int) my_score_value, new AccessAPI.onResponseUpdateUserAnimeList() {
                @Override
                public void onUpdateUserAnimeListListener() {

                    anime.getNode().getMy_list_status().setNum_episodes_watched(my_progress);
                    anime.getNode().getMy_list_status().setScore(my_score_value);
                    anime.getNode().getMy_list_status().setStatus(my_status);


                    HashMap<String, HashMap<String, AnimeObject>> statusHashMap = AppConfig.getUserAnimeListDB();

                    statusHashMap.get(original_status).remove(anime.getNode().getTitle());
                    statusHashMap.get(my_status).put(anime.getNode().getTitle(), anime);

                    UserAnimeListChildFragment fragment_original_status = AppConfig.getFragmentUserAnimeListMap().get(original_status);
                    fragment_original_status.notifyUpdate(new ArrayList<>(statusHashMap.get(original_status).values()));
                    //fragment_original_status.sortList();

                    UserAnimeListChildFragment fragment_my_status = AppConfig.getFragmentUserAnimeListMap().get(my_status);
                    fragment_my_status.notifyUpdate(new ArrayList<>(statusHashMap.get(my_status).values()));
                    //fragment_my_status.sortList();

                    if (anime.getNode().getStatus().equals("currently_airing"))
                    {
                        FragmentScheduleContent fragmentUserScheduleContent = AppConfig.getFragmentUserScheduleContent().get(anime.getNode().getBroadcast().getDay_of_the_week().toLowerCase());
                        if (fragmentUserScheduleContent != null)
                        {
                            fragmentUserScheduleContent.arrayDailyListUpdate(anime);
                        }
                        else
                        {
                            AppConfig.getUserScheduleDB().remove(anime.getNode().getTitle());
                            AppConfig.getUserScheduleDB().put(anime.getNode().getTitle(), anime);
                        }
                    }

                    original_status = my_status;

                    Toast.makeText(AnimePageActivity.this, "List Updated!", Toast.LENGTH_LONG).show();

                    original_status = my_status;

                    if (warning != null) {
                        warning.setVisibility(View.INVISIBLE);
                    }

                    if (button_remove != null) {
                        button_remove.setEnabled(true);
                    }
                }

                @Override
                public void onErrorResponse() {
                    AppConfig.getAccessApi().refreshToken(AppConfig.getAccessApi().getRefresh_token(), AnimePageActivity.this, new AccessAPI.onAuthTokenListener() {

                        @Override
                        public void onStatusCodeResponse(int status_code) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AnimePageActivity.this);
                            builder.setMessage("We're refreshing your token, please try again.\nIf you keep getting this message please relogin.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.create();
                            builder.show();
                        }
                    });
                }
            }));
        }
        else
        {
            AnimePageActivity.this.finish();
        }
        return true;
    }
}
