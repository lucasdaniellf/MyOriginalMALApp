package com.example.myoriginalmalapp.animeobject;

public class MyListStatusObject
{
    private int num_episodes_watched;
    private double score;
    private String status;

    public int getNum_episodes_watched() {
        return num_episodes_watched;
    }

    public void setNum_episodes_watched(int num_episodes_watched) {
        this.num_episodes_watched = num_episodes_watched;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MyListStatusObject{" +
                "num_episodes_watched=" + num_episodes_watched +
                ", score=" + score +
                ", status='" + status + '\'' +
                '}';
    }
}
