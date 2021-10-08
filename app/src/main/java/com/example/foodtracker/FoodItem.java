package com.example.foodtracker;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class FoodItem implements Comparable<FoodItem>{
    private String item;
    private Date date;

    public FoodItem(){}

    public String getItem(){
        return item;
    }

    public Date getDate(){
        return date;
    }

    public String getStringDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return Objects.equals(item, foodItem.getItem()) &&
                Objects.equals(date, foodItem.getDate());
    }


    @Override
    public int compareTo(FoodItem foodItem) {
        if(date.compareTo(foodItem.getDate()) < 0) return -1;
        if(date.compareTo(foodItem.getDate()) > 0) return 1;
        return 0;
    }

    public void setItem(String item){
        this.item = item;
    }

    public void setStringDate(String date) throws ParseException {
        this.date = new SimpleDateFormat("MM/dd/yyyy").parse(date);
    }


}
