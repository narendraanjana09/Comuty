package com.nsa.comuty.onboarding.models;

import android.graphics.drawable.Drawable;

public class ScreensModel {
    private String txt1;
    private Drawable img1;
    private String txt2;
    private String txt3;

    public ScreensModel(String txt1, Drawable img1, String txt2, String txt3) {
        this.txt1 = txt1;
        this.img1 = img1;
        this.txt2 = txt2;
        this.txt3 = txt3;
    }

    public String getTxt1() {
        return txt1;
    }

    public void setTxt1(String txt1) {
        this.txt1 = txt1;
    }

    public Drawable getImg1() {
        return img1;
    }

    public void setImg1(Drawable img1) {
        this.img1 = img1;
    }

    public String getTxt2() {
        return txt2;
    }

    public void setTxt2(String txt2) {
        this.txt2 = txt2;
    }

    public String getTxt3() {
        return txt3;
    }

    public void setTxt3(String txt3) {
        this.txt3 = txt3;
    }
}
