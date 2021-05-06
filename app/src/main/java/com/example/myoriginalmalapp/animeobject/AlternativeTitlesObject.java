package com.example.myoriginalmalapp.animeobject;

import java.util.ArrayList;

public class AlternativeTitlesObject {

    private String en;
    private ArrayList<String> synonyms;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public String toString() {
        String alternative = en;
        for (String synonym : synonyms)
        {
           alternative = alternative + " | " + synonym;
        }
        return alternative;
    }
}
