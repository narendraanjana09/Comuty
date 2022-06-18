package com.nsa.comuty.onboarding.extra;

import android.os.AsyncTask;

import com.nsa.comuty.onboarding.interfaces.DownloadListener;

import java.io.InputStream;

public class DownloadImage extends AsyncTask<String,Void, InputStream> {

    DownloadListener listener;

    public DownloadImage(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected InputStream doInBackground(String... strings) {
        String imageURL = strings[0];
        InputStream input= null;
        try {
            // Download Image from URL
             input = new java.net.URL(imageURL).openStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
        super.onPostExecute(inputStream);
        if(inputStream!=null){
            listener.OnDownloaded(inputStream);
        }
    }
}
