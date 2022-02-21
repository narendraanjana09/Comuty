package com.nsa.comuty.extra;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.nsa.comuty.R;

public class CustomDialog extends Dialog {

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        getWindow().setBackgroundDrawableResource(R.drawable.round_bg);
    }
    public void setHeightWidth(Activity activity,float widthPercentage,float heightPercentage){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * widthPercentage);
        int dialogWindowHeight = (int) (displayHeight * heightPercentage);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        getWindow().setAttributes(layoutParams);
    }
}
