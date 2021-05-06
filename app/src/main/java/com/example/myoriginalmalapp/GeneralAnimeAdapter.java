package com.example.myoriginalmalapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myoriginalmalapp.activities.AnimePageActivity;
import com.example.myoriginalmalapp.animeobject.AnimeObject;
import com.squareup.picasso.Picasso;


public class GeneralAnimeAdapter extends PagedListAdapter<AnimeObject, GeneralAnimeAdapter.PageDataViewHolder>
{
    public GeneralAnimeAdapter(Context context)
    {
        super(DIFF_CALLBACK);
    }


    private static final DiffUtil.ItemCallback<AnimeObject> DIFF_CALLBACK = new DiffUtil.ItemCallback<AnimeObject>()
    {
        @Override
        public boolean areItemsTheSame(AnimeObject oldItem, AnimeObject newItem) {
            return oldItem.getNode().getId() == newItem.getNode().getId();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        public boolean areContentsTheSame(AnimeObject oldItem, @NonNull AnimeObject newItem) {
            return oldItem.getNode().getId() == newItem.getNode().getId();
        }
    };



    @NonNull
    @Override
    public PageDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_fragment_anime, parent, false);
        return new PageDataViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PageDataViewHolder holder, int position) {
        AnimeObject animeObject = getItem(position);
        holder.getTitle().setText(animeObject.getNode().getTitle());
        holder.getConstraintLayout().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AnimePageActivity.class);

            int mal_id = animeObject.getNode().getId();
            intent.putExtra("mal_id", mal_id);
            v.getContext().startActivity(intent);
        });
        if (animeObject.getNode().getMain_picture() != null)
        {
            Picasso.get().load(animeObject.getNode().getMain_picture().getLarge()).fit().into(holder.getImageView());
        }
    }





    public static class PageDataViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title;
        private ConstraintLayout constraintLayout;

        public PageDataViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_anime_list_id);
            title = itemView.findViewById(R.id.anime_title_anime_list_id);
            constraintLayout = itemView.findViewById(R.id.constraint_anime_list_id);
        }

        public ConstraintLayout getConstraintLayout() {
            return constraintLayout;
        }

        public void setConstraintLayout(ConstraintLayout constraintLayout) {
            this.constraintLayout = constraintLayout;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }
    }
}
