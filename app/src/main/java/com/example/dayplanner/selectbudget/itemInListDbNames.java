package com.example.dayplanner.selectbudget;


public class itemInListDbNames{
    public String name;
    public long id;
    public Double value;
    public boolean sortDate;

    itemInListDbNames(){
        name = new String();
        value = (double)0;
        sortDate=false;
    }
    itemInListDbNames(String namee,Double value,boolean sortDat){
        name = namee;
        this.value = value;
        sortDate=sortDat;
    }
}
