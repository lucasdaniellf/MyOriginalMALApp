package com.example.myoriginalmalapp.activities;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;
import com.example.myoriginalmalapp.AccessAPI;
import com.example.myoriginalmalapp.AppConfig;
import com.example.myoriginalmalapp.R;
import com.example.myoriginalmalapp.RecyclerAdapter;
import com.example.myoriginalmalapp.SingletonQueue;
import com.example.myoriginalmalapp.animeobject.AnimeObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class UserAnimeListChildFragment extends Fragment {
    private final String tab_selected;
    private final ArrayList<com.example.myoriginalmalapp.animeobject.AnimeObject> animeList = new ArrayList<>();
    private View v;
    private RecyclerAdapter adapter;


    private final AccessAPI accessAPI = AppConfig.getAccessApi();

    public String getTab_selected() {
        return tab_selected;
    }

    public UserAnimeListChildFragment(String tab_selected) {
        super(R.layout.user_anime_list_child_fragment);
        this.tab_selected = tab_selected;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.user_anime_list_child_fragment, container, false);
        return this.v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_search_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new RecyclerAdapter(animeList);
        recyclerView.setAdapter(adapter);


        fragmentRequestMethod(new notifyUpdateListener() {
            @Override
            public void onNotifyUpdateListener() {
                ((Home_Activity)getParentFragment().getActivity()).setFragment_initialization_count(((Home_Activity)getParentFragment().getActivity()).getFragment_initialization_count() + 1);
                if (((Home_Activity)getParentFragment().getActivity()).getFragment_initialization_count() > 4)
                {
                    Home_Activity home_activity = (Home_Activity) getParentFragment().getActivity();
                    home_activity.findViewById(R.id.progressLayoutHomeId).setVisibility(View.GONE);
                    home_activity.findViewById(R.id.home_constraint_layout_id).setAlpha((float) 1);
                    home_activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        });
    }


    public ArrayList<AnimeObject> getAnimeList() {
        return animeList;
    }

    public interface notifyUpdateListener
    {
        void onNotifyUpdateListener();
    }


    public void notifyUpdate(ArrayList<AnimeObject> list)
    {

        UserAnimeListChildFragment.this.animeList.clear();
        UserAnimeListChildFragment.this.animeList.addAll(list);
        adapter.notifyDataSetChanged();
    }



    //NOT IMPLEMENTED SO THE APK WORKS FOR LOW API VERSION
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortList()
    {
        animeList.sort(Comparator.comparing(a -> a.getNode().getTitle()));
    }


    public void fragmentRequestMethod(notifyUpdateListener listener) {
        String path = "/v2/users/@me/animelist";

        String URL = Uri.parse(accessAPI.getUrl()+path).buildUpon().appendQueryParameter("sort", "anime_title")
                .appendQueryParameter("status", tab_selected)
                .appendQueryParameter("fields", "id,num_episodes,status,my_list_status,title,start_season,main_picture,broadcast")
                .appendQueryParameter("limit", "100")
                .build().toString();


        SingletonQueue.getSingleton(v.getContext()).addToRequestQueue(accessAPI.userAnimeList(this.tab_selected, URL, new ArrayList<>(), v.getContext(), new AccessAPI.AnimeListListener() {
            @Override
            public void onAnimeListResponse(ArrayList<AnimeObject> animeList) {
                notifyUpdate(animeList);

                HashMap<String, AnimeObject> animeHashMap = new HashMap<>();
                for (AnimeObject anime : animeList) {
                    animeHashMap.put(anime.getNode().getTitle(), anime);

                    if (anime.getNode().getStatus().equals("currently_airing"))
                    {
                        AppConfig.getUserScheduleDB().remove(anime.getNode().getTitle());
                        AppConfig.getUserScheduleDB().put(anime.getNode().getTitle(), anime);
                    }
                }

                HashMap<String, HashMap<String, AnimeObject>> statusHashMap = AppConfig.getUserAnimeListDB();
                statusHashMap.remove(tab_selected);
                statusHashMap.put(tab_selected, animeHashMap);
                listener.onNotifyUpdateListener();
            }

            @Override
            public void onErrorResponse() {
                AppConfig.getAccessApi().refreshToken(AppConfig.getAccessApi().getRefresh_token(), v.getContext(), new AccessAPI.onAuthTokenListener() {
                    @Override
                    public void onStatusCodeResponse(int status_code) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
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
}
