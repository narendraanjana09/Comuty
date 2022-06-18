package com.nsa.comuty.extra;

import com.nsa.comuty.home.models.DateModel;

public class Util {
    public static DateModel getDate(String date){
        String[] arr = date.split("/");
        DateModel dateModel=new DateModel(arr[0]+"",arr[1]+"",arr[2]+"");
        return dateModel;
    }
}
