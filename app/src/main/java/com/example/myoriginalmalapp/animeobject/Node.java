package com.example.myoriginalmalapp.animeobject;

import java.util.ArrayList;

public class Node {
    private String title;
    private int num_episodes;
    private int id;
    private String status;
    private MainPicture main_picture;
    private SeasonObject start_season;
    private String synopsis;
    private String source;
    private String rating;
    private Double mean;
    private AlternativeTitlesObject alternative_titles;
    private ArrayList<GenreObject> genres;
    private ArrayList <AnimeObject> related_anime;
    private MyListStatusObject my_list_status;
    private int average_episode_duration;
    private String media_type;
    private BroadcastObject broadcast;


    public int getAverage_episode_duration() {
        return average_episode_duration;
    }

    public void setAverage_episode_duration(int average_episode_duration) {
        this.average_episode_duration = average_episode_duration;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public ArrayList<AnimeObject> getRelated_anime() {
        return related_anime;
    }

    public void setRelated_anime(ArrayList<AnimeObject> related_anime) {
        this.related_anime = related_anime;
    }

    public BroadcastObject getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(BroadcastObject broadcast) {
        this.broadcast = broadcast;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SeasonObject getStart_season() {
        return start_season;
    }

    public void setStart_season(SeasonObject start_season) {
        this.start_season = start_season;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public MainPicture getMain_picture() {
        return main_picture;
    }

    public void setMain_picture(MainPicture main_picture) {
        this.main_picture = main_picture;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(Double mean) {
        this.mean = mean;
    }

    public AlternativeTitlesObject getAlternative_titles() {
        return alternative_titles;
    }

    public void setAlternative_titles(AlternativeTitlesObject alternative_titles) {
        this.alternative_titles = alternative_titles;
    }

    public ArrayList<GenreObject> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<GenreObject> genres) {
        this.genres = genres;
    }

    public MyListStatusObject getMy_list_status() {
        return my_list_status;
    }

    public void setMy_list_status(MyListStatusObject my_list_status) {
        this.my_list_status = my_list_status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNum_episodes() {
        return num_episodes;
    }

    public void setNum_episodes(int num_episodes) {
        this.num_episodes = num_episodes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Node{" +
                "title='" + title + '\'' +
                ", num_episodes=" + num_episodes +
                ", status='" + status + '\'' +
                ", my_list_status=" + my_list_status.toString() +
                '}';
    }
}
