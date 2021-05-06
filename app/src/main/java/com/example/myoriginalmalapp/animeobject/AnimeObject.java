package com.example.myoriginalmalapp.animeobject;

public class AnimeObject
{
    private Node node;


    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return node.toString();
    }
}
