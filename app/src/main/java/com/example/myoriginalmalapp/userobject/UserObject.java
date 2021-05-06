package com.example.myoriginalmalapp.userobject;

public class UserObject {
    private int id;
    private String name;
    private AnimeStatisticsObject anime_statistics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnimeStatisticsObject getAnime_statistics() {
        return anime_statistics;
    }

    public void setAnime_statistics(AnimeStatisticsObject anime_statistics) {
        this.anime_statistics = anime_statistics;
    }
}
