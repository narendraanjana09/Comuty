package com.nsa.comuty.onboarding.extra;

import android.content.Context;
import android.content.SharedPreferences;

import com.nsa.comuty.R;

public class SavedText {
    private SharedPreferences sharedpreferences;
   private Context context;

    public SavedText(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }
    public void deleteAll(){
        sharedpreferences.edit().clear().commit();
    }
    public void setText(String key,String text){
        sharedpreferences.edit().putString(key,text).apply();
    }
    public String getText(String key){
        return sharedpreferences.getString(key,"");
    }
    public void remove(String key){
        sharedpreferences.edit().remove(key).commit();
    }
}
