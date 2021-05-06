package com.example.myoriginalmalapp;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myoriginalmalapp.animeobject.AnimeObject;
import com.example.myoriginalmalapp.animeobject.Node;
import com.example.myoriginalmalapp.userobject.UserObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessAPI
{
    private final Gson gson = new Gson();
    private int status_code;
    private String token_type = "";
    private int expires_in;
    private String access_token = "";
    private String refresh_token = "";

    private final String url = "https://api.myanimelist.net";
    private final String client_id = "";

    public String getUrl() {
        return url;
    }

    public interface onAuthTokenListener
    {
        void onStatusCodeResponse(int status_code);
    }

    public StringRequest refreshToken(String refresh, Context ctx, onAuthTokenListener listener)
    {

        String url = "https://myanimelist.net/v1/oauth2/token";

        return new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AccessObject accessObject = gson.fromJson(response, AccessObject.class);
                token_type = accessObject.getToken_type();
                expires_in = accessObject.getExpires_in();
                access_token = accessObject.getAccess_token();
                refresh_token = accessObject.getRefresh_token();

                listener.onStatusCodeResponse(status_code);


                try {
                    FileWriter writer = new FileWriter(new File(ctx.getFilesDir(), "user_info.json"));
                    BufferedWriter bw = new BufferedWriter(writer);
                    bw.write(response);
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status_code = error.networkResponse.statusCode;
                listener.onStatusCodeResponse(status_code);
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("client_id", client_id);
                params.put("grant_type", "refresh_token");
                params.put("refresh_token", refresh);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }





    public StringRequest authToken(String username, String password, Context ctx, onAuthTokenListener listener)
    {

        String auth_path = "/v2/auth/token";

        String url_path = url + auth_path;

        return new StringRequest(Request.Method.POST, url_path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    AccessObject accessObject = gson.fromJson(response, AccessObject.class);
                    token_type = accessObject.getToken_type();
                    expires_in = accessObject.getExpires_in();
                    access_token = accessObject.getAccess_token();
                    refresh_token = accessObject.getRefresh_token();

                    listener.onStatusCodeResponse(status_code);

                    FileWriter writer = new FileWriter(new File(ctx.getFilesDir(), "user_info.json"));
                    BufferedWriter bw = new BufferedWriter(writer);
                    bw.write(response);
                    bw.flush();
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status_code = error.networkResponse.statusCode;
                listener.onStatusCodeResponse(status_code);
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("client_id", client_id);
                params.put("grant_type", "password");
                params.put("password", password);
                params.put("username", username);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }





    public interface AnimeListListener
    {
        void onAnimeListResponse(ArrayList<AnimeObject> animeList);
        void onErrorResponse();
    }

    public StringRequest userAnimeList(String tab_selected, String url, ArrayList<AnimeObject> myList, Context ctx, AnimeListListener listener)
    {
        String path = "/v2/users/@me/animelist";

//        String URL = Uri.parse(url+path).buildUpon().appendQueryParameter("sort", "anime_title")
//                                        .appendQueryParameter("status", tab_selected)
//                                        .appendQueryParameter("fields", "id,num_episodes,status,my_list_status,title,start_season,synopsis,source,rating,mean,alternative_titles,genres,broadcast")
//                                        .appendQueryParameter("limit", "15")
//                                        .build().toString();

        return new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

                    PagingClass pagingClass = gson.fromJson(jsonObject.get("paging"), PagingClass.class);
                    ArrayList<AnimeObject> animeList = gson.fromJson(jsonObject.get("data"), new TypeToken<List<AnimeObject>>(){}.getType());

                    myList.addAll(animeList);
                    if (pagingClass.getNext() != null)
                    {
                        SingletonQueue.getSingleton(ctx).addToRequestQueue(userAnimeList(tab_selected, pagingClass.getNext(), myList, ctx, listener));
                    }
                    else
                    {
                        listener.onAnimeListResponse(myList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("client_id", client_id);
                headers.put("Authorization", token_type+" "+access_token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }




    public interface onResponseUpdateUserAnimeList
    {
        void onUpdateUserAnimeListListener();
        void onErrorResponse();
    }

    public StringRequest updateUserAnimeList(int mal_id, String status, int num_watched_episodes, int score, onResponseUpdateUserAnimeList listener)
    {
        String path = "/v2/anime/"+mal_id+"/my_list_status";
        String URL = url+path;

        return new StringRequest(Request.Method.PUT, URL, response -> {
            listener.onUpdateUserAnimeListListener();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("client_id", client_id);
                headers.put("Authorization", token_type+" "+access_token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", status);
                params.put("num_watched_episodes", String.valueOf(num_watched_episodes));
                params.put("score", String.valueOf(score));
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }


    public interface GeneralAnimeListener
    {
        void onResponse(String response);
        void onErrorResponse();
    }

    public StringRequest generalAnimeList(String ranking_type, int limit, int offset, GeneralAnimeListener listener)
    {
        String path =  "/v2/anime/ranking";
        String URL = Uri.parse(url+path).buildUpon()
                .appendQueryParameter("ranking_type", ranking_type)
                .appendQueryParameter("limit", String.valueOf(limit))
                .appendQueryParameter("fields", "id,main_picture,title")
                .appendQueryParameter("offset", String.valueOf(offset*limit))
                .build().toString();

        return new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("client_id", client_id);
                headers.put("Authorization", token_type + " " + access_token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }



    public StringRequest suggestedAnime(int limit, int offset, GeneralAnimeListener listener)
    {
        String path =  "/v2/anime/suggestions";
        String URL = Uri.parse(url+path).buildUpon()
                .appendQueryParameter("limit", String.valueOf(limit))
                .appendQueryParameter("fields", "id,main_picture,title")
                .appendQueryParameter("offset", String.valueOf(offset*limit))
                .build().toString();

        return new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("client_id", client_id);
                headers.put("Authorization", token_type + " " + access_token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }





    public StringRequest removeFromAnimeList(int mal_id, onResponseUpdateUserAnimeList listener)
    {
        String path = "/v2/anime/"+mal_id+"/my_list_status";
        String URL = url+path;

        return new StringRequest(Request.Method.DELETE, URL, response -> {
            listener.onUpdateUserAnimeListListener();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("client_id", client_id);
                headers.put("Authorization", token_type+" "+access_token);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }



    public StringRequest SeasonalAnimeList(String season, String year, int limit, int offset, GeneralAnimeListener listener)
    {
        String path =  "/v2/anime/season";
        String URL = Uri.parse(url+path+"/"+year+"/"+season).buildUpon()
                .appendQueryParameter("sort", "anime_score")
                .appendQueryParameter("limit", String.valueOf(limit))
                .appendQueryParameter("offset", String.valueOf(limit*offset))
                .appendQueryParameter("fields", "id,main_picture,title")
                .build().toString();

        return new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("client_id", client_id);
                headers.put("Authorization", token_type + " " + access_token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }









    public StringRequest searchAnime(String nome, int limit, int offset, GeneralAnimeListener listener)
    {
        String path = "/v2/anime";
        String URL = Uri.parse(url+path).buildUpon()
                .appendQueryParameter("q", nome)
                .appendQueryParameter("limit", String.valueOf(limit))
                .appendQueryParameter("offset", String.valueOf(offset*limit))
                .appendQueryParameter("fields", "id,main_picture,title")
                .build().toString();

        return new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("client_id", client_id);
                headers.put("Authorization", token_type + " " + access_token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }



    public StringRequest animeDetails(int mal_id, AnimeListListener listener)
    {
        String path = "/v2/anime/"+String.valueOf(mal_id);
        String URL = Uri.parse(url+path).buildUpon()
                .appendQueryParameter("fields", "id,num_episodes,main_picture,status,my_list_status,title,start_season,synopsis,source,rating,mean,alternative_titles,genres,broadcast,related_anime,media_type,average_episode_duration")
                .build().toString();

        return new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Node node_anime = gson.fromJson(response, Node.class);

                AnimeObject animeObject = new AnimeObject();
                animeObject.setNode(node_anime);
                ArrayList<AnimeObject> animeList = new ArrayList<>();
                animeList.add(animeObject);

                listener.onAnimeListResponse(animeList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("client_id", client_id);
                headers.put("Authorization", token_type + " " + access_token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }




    public interface UserInfoRequestListener
    {
        void userInfoRequestResponse(UserObject userObject);
        void onErrorResponse();
    }

    public StringRequest userInfoRequest(UserInfoRequestListener listener)
    {
        String path =  "/v2/users/@me";
        String URL = Uri.parse(url+path).buildUpon()
                .appendQueryParameter("fields", "name,anime_statistics")
                .build().toString();

        return new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                UserObject userObject = gson.fromJson(response, UserObject.class);
                listener.userInfoRequestResponse(userObject);
            }
        }, Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("client_id", client_id);
                headers.put("Authorization", token_type + " " + access_token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset-UTF-8";
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
    }

    // ----------TOKENS------------//

    public String getRefresh_token() {
        return refresh_token;
    }
}
