package com.nsa.comuty.onboarding.models;

public class ImageModel {
    private String path;
    private boolean isLink;

    public ImageModel(String path, boolean isLink) {
        this.path = path;
        this.isLink = isLink;
    }

    public String getPath() {
        return path;
    }

    public boolean isLink() {
        return isLink;
    }
}
