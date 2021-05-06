package com.example.myoriginalmalapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoriginalmalapp.activities.AnimePageActivity;
import com.example.myoriginalmalapp.animeobject.AnimeObject;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{

    private final ArrayList<AnimeObject> animeList;
    private final Gson gson = AppConfig.getGson();

    public RecyclerAdapter(ArrayList<AnimeObject> animeList) {
        this.animeList = animeList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user_anime_list_child_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.getTitle().setText(animeList.get(position).getNode().getTitle());

        if (animeList.get(position).getNode().getStart_season() != null)
        {
            holder.getSeason().setText(animeList.get(position).getNode().getStart_season().toString());
        }
        else
        {
            holder.getSeason().setText("Season Not yet Defined");
        }

        StringBuffer episode_progress = new StringBuffer();
        episode_progress
                .append("Episodes: ")
                .append(animeList.get(position).getNode().getMy_list_status().getNum_episodes_watched())
                .append(" / ")
                .append(animeList.get(position).getNode().getNum_episodes());

        holder.getEpisodes().setText(episode_progress);


        StringBuffer myScore = new StringBuffer();
        myScore.append("Score: ");

        if (animeList.get(position).getNode().getMy_list_status().getScore() != 0)
        {
            myScore.append(animeList.get(position).getNode().getMy_list_status().getScore());
            holder.getMyScore().setText(myScore);
        }
        else
        {
            myScore.append("-");
            holder.getMyScore().setText(myScore);
        }

        holder.getLayout().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AnimePageActivity.class);

            int mal_id = animeList.get(position).getNode().getId();

            //String anime = gson.toJson(animeList.get(position));
            //intent.putExtra("JsonAnime", anime);
            intent.putExtra("mal_id", mal_id);
            v.getContext().startActivity(intent);
        });
        if (animeList.get(position).getNode().getMain_picture() != null)
        {
            Picasso.get().load(animeList.get(position).getNode().getMain_picture().getLarge()).fit().into(holder.getImageView());
        }
    }
    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView season;
        private TextView episodes;
        private TextView myScore;
        private ImageView imageView;
        private LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.anime_title_id);
            season = (TextView) itemView.findViewById(R.id.season_id);
            episodes = (TextView) itemView.findViewById(R.id.episodes_id);
            myScore = (TextView) itemView.findViewById(R.id.score_id);
            imageView = itemView.findViewById(R.id.image_view_id);
            layout = (LinearLayout) itemView.findViewById(R.id.constraint_anime_list_id);

        }

        public TextView getTitle() {
            return title;
        }

        public TextView getSeason() {
            return season;
        }

        public TextView getEpisodes() {
            return episodes;
        }

        public TextView getMyScore() { return myScore; }

        public LinearLayout getLayout() { return layout; }

        public ImageView getImageView() { return imageView; }
    }
}
