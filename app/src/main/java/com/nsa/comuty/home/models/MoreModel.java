package com.nsa.comuty.home.models;

import android.graphics.drawable.Drawable;

public class MoreModel {
    private String title;
    private Drawable drawable;

    public MoreModel(String title, Drawable drawable) {
        this.title = title;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
