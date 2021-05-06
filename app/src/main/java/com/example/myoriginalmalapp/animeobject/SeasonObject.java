package com.example.myoriginalmalapp.animeobject;

public class SeasonObject {
    private String season;
    private int year;

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Season: " + season + " " + year;
    }
}
