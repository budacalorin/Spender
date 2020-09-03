package com.example.dayplanner.addspending;

public class suggestionItem {
    public String name;
    public Integer usage;
    public long id;

    public suggestionItem(){ name=new String();
    }
    public suggestionItem(String s,int valoare,long ID){
        name = new String();
        name=s;
        usage =valoare;
        id=ID;
    }
}
