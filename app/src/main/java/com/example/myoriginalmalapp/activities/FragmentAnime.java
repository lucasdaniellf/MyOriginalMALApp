 package com.example.myoriginalmalapp.activities;

 import android.content.DialogInterface;
 import android.content.Intent;
 import android.os.Bundle;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.ImageView;
 import android.widget.SearchView;
 import android.widget.TextView;

 import androidx.annotation.NonNull;
 import androidx.annotation.Nullable;
 import androidx.appcompat.app.AlertDialog;
 import androidx.constraintlayout.widget.ConstraintLayout;
 import androidx.fragment.app.Fragment;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.example.myoriginalmalapp.AccessAPI;
 import com.example.myoriginalmalapp.AppConfig;
 import com.example.myoriginalmalapp.GeneralAnimeAdapter;
 import com.example.myoriginalmalapp.GeneralAnimeViewModel;
 import com.example.myoriginalmalapp.R;
 import com.example.myoriginalmalapp.SingletonQueue;
 import com.example.myoriginalmalapp.animeobject.AnimeObject;
 import com.google.gson.Gson;
 import com.squareup.picasso.Picasso;

 import java.util.ArrayList;
 import java.util.HashMap;


// public class FragmentAnime extends Fragment
// {
//     private final HashMap<String, ArrayList<AnimeObject>> animeFragListmap = new HashMap<>();
//     private final HashMap<String, RecyclerAdapterAnimeList> adapter_map = new HashMap<>();
//     private final String[] ranking_types = new String[5];
//     private View view;
//     private final AccessAPI accessAPI = AppConfig.getAccessApi();
//     @Nullable
//     @Override
//     public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//         view = inflater.inflate(R.layout.fragment_anime, container, false);
//         return view;
//     }
//
//     @Override
//     public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//         super.onViewCreated(view, savedInstanceState);
//
//         SearchView searchView = view.findViewById(R.id.searchViewid);
//         searchView.setSubmitButtonEnabled(true);
//         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//             @Override
//             public boolean onQueryTextSubmit(String query) {
//                 if(!query.isEmpty()){
//                     String search = String.valueOf(searchView.getQuery());
//                     Intent intent = new Intent(view.getContext(), SearchAnimeActivity.class);
//                     intent.putExtra("search", search);
//                     startActivity(intent);
//                 }
//                 return false;
//             }
//
//             @Override
//             public boolean onQueryTextChange(String newText) {
//                 return false;
//             }
//         });
//
//         ranking_types[0] = "all";
//         ranking_types[1] = "airing";
//         ranking_types[2] = "upcoming";
//         ranking_types[3] = "bypopularity";
//         ranking_types[4] = "favorite";
//
//         HashMap<String, RecyclerView> recycler_view_map = new HashMap<>();
//         recycler_view_map.put(ranking_types[0], view.findViewById(R.id.all_anime_id));
//         recycler_view_map.put(ranking_types[1], view.findViewById(R.id.top_airing_anime_id));
//         recycler_view_map.put(ranking_types[2], view.findViewById(R.id.top_upcoming_anime_id));
//         recycler_view_map.put(ranking_types[3], view.findViewById(R.id.by_populrity_anime_id));
//         recycler_view_map.put(ranking_types[4], view.findViewById(R.id.favorite_anime_id));
//
//         for (RecyclerView recyclerView : recycler_view_map.values())
//         {
//             recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
//
//         }
//
//         for (String type : ranking_types)
//         {
//             animeFragListmap.put(type, new ArrayList<>());
//             adapter_map.put(type, new RecyclerAdapterAnimeList(animeFragListmap.get(type)));
//         }
//
//         for (String type : ranking_types)
//         {
//             recycler_view_map.get(type).setAdapter(adapter_map.get(type));
//         }
//
//         for (String type : ranking_types)
//         {
//             fragmentRequestMethod(adapter_map.get(type), type, new fragmentRequestListener() {
//                 @Override
//                 public void onFragmentRequestResponse() { }
//             });
//         }
//     }
public class FragmentAnime extends Fragment
{
    private final HashMap<String, ArrayList<AnimeObject>> animeFragListmap = new HashMap<>();
    private final HashMap<String, GeneralAnimeAdapter> adapter_map = new HashMap<>();
    private final HashMap<String, GeneralAnimeViewModel> view_model_map = new HashMap<>();
    private final String[] ranking_types = new String[5];
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_anime, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView searchView = view.findViewById(R.id.searchViewid);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty()){
                    String search = String.valueOf(searchView.getQuery());
                    Intent intent = new Intent(view.getContext(), SearchAnimeActivity.class);
                    intent.putExtra("search", search);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ranking_types[0] = "all";
        ranking_types[1] = "airing";
        ranking_types[2] = "upcoming";
        ranking_types[3] = "bypopularity";
        ranking_types[4] = "favorite";

    }

    //     public interface fragmentRequestListener
//     {
//         void onFragmentRequestResponse();
//     }


//     public void fragmentRequestMethod(RecyclerAdapterAnimeList adapter, String ranking_type, fragmentRequestListener listener)
//     {
//         SingletonQueue.getSingleton(view.getContext()).addToRequestQueue(accessAPI.generalAnimeList(ranking_type, new AccessAPI.AnimeListListener() {
//             @Override
//             public void onAnimeListResponse(ArrayList<AnimeObject> animeList) {
//                 FragmentAnime.this.animeFragListmap.get(ranking_type).clear();
//                 FragmentAnime.this.animeFragListmap.get(ranking_type).addAll(animeList);
//                 adapter.notifyDataSetChanged();
//             }
//
//             @Override
//             public void onErrorResponse() {
//                 AppConfig.getAccessApi().refreshToken(AppConfig.getAccessApi().getRefresh_token(), view.getContext(), new AccessAPI.onAuthTokenListener() {
//
//                     @Override
//                     public void onStatusCodeResponse(int status_code) {
//                         AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                         builder.setMessage("We're refreshing your token, please try again.\nIf you keep getting this message please relogin.")
//                                 .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                     public void onClick(DialogInterface dialog, int id) {
//                                         dialog.cancel();
//                                     }
//                                 });
//                         builder.create();
//                         builder.show();
//                     }
//                 });
//             }
//         }));
//     }
//
//     @Override
//     public void onResume() {
//         super.onResume();
//         for (String type : ranking_types)
//         {
//             fragmentRequestMethod(adapter_map.get(type), type, new fragmentRequestListener() {
//                 @Override
//                 public void onFragmentRequestResponse() { }
//             });
//         }
//     }
//
//     public HashMap<String, RecyclerAdapterAnimeList> getAdapter_map() {
//         return adapter_map;
//     }

     public String[] getRanking_types() {
         return ranking_types;
     }
 }


 class RecyclerAdapterAnimeList extends RecyclerView.Adapter<RecyclerAdapterAnimeList.ViewHolderAnimeList>
 {
     private final ArrayList<AnimeObject> animeList;
     private final Gson gson = AppConfig.getGson();

     public RecyclerAdapterAnimeList(ArrayList<AnimeObject> animeList)
     {
         this.animeList = animeList;
     }

     @NonNull
     @Override
     public com.example.myoriginalmalapp.activities.RecyclerAdapterAnimeList.ViewHolderAnimeList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_fragment_anime, parent, false);
         return new ViewHolderAnimeList(view);
     }

     @Override
     public void onBindViewHolder(@NonNull RecyclerAdapterAnimeList.ViewHolderAnimeList holder, int position) {
         holder.getTitle().setText(animeList.get(position).getNode().getTitle());
         holder.getConstraintLayout().setOnClickListener(v -> {
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

     public static class ViewHolderAnimeList extends RecyclerView.ViewHolder {
         private ImageView imageView;
         private TextView title;
         private ConstraintLayout constraintLayout;

         public ViewHolderAnimeList(@NonNull View itemView) {
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
