package com.example.myoriginalmalapp.userobject;

public class AnimeStatisticsObject {
    private int num_items_watching;
    private int num_items_completed;
    private int num_items_on_hold;
    private int num_items_dropped;
    private int num_items_plan_to_watch;
    private double num_days;

    public int getNum_items_watching() {
        return num_items_watching;
    }

    public void setNum_items_watching(int num_items_watching) {
        this.num_items_watching = num_items_watching;
    }

    public int getNum_items_completed() {
        return num_items_completed;
    }

    public void setNum_items_completed(int num_items_completed) {
        this.num_items_completed = num_items_completed;
    }

    public int getNum_items_on_hold() {
        return num_items_on_hold;
    }

    public void setNum_items_on_hold(int num_items_on_hold) {
        this.num_items_on_hold = num_items_on_hold;
    }

    public int getNum_items_dropped() {
        return num_items_dropped;
    }

    public void setNum_items_dropped(int num_items_dropped) {
        this.num_items_dropped = num_items_dropped;
    }

    public int getNum_items_plan_to_watch() {
        return num_items_plan_to_watch;
    }

    public void setNum_items_plan_to_watch(int num_items_plan_to_watch) {
        this.num_items_plan_to_watch = num_items_plan_to_watch;
    }

    public double getNum_days() {
        return num_days;
    }

    public void setNum_days(double num_days) {
        this.num_days = num_days;
    }
}
