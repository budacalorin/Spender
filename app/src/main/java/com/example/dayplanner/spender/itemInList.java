package com.example.dayplanner.spender;

public class itemInList {
    public String description;
    public Double value;
    public String details;
    public long id;
    public String date;

    public itemInList(){
        description=new String();
        details = new String();
        date = new String();
    }
    public itemInList(String name,double valoare,long ID,String details,String Date){
        description=name;
        value=valoare;
        id=ID;
        this.details = details;
        date = Date;

    }
}
