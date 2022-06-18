package com.nsa.comuty.extra;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.nsa.comuty.home.HomeActivity;

public class NetworkStatus {
    private NetworkRequest networkRequest;
    private String TAG="newWork";
    private Activity activity;
    private No_Internet_Fragment noInternetDialog;
    private FragmentManager fragmentManager;
    public NetworkStatus(Activity homeActivity, FragmentManager supportFragmentManager) {
        this.fragmentManager=supportFragmentManager;
        this.activity=homeActivity;
        noInternetDialog=new No_Internet_Fragment();
        noInternetDialog.setCancelable(false);
        networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        noInternetDialog.setRetry(new No_Internet_Fragment.Retry() {
            @Override
            public void onRetry() {
                retry();
            }
        });
        retry();

    }

    private void retry() {
        if(isNetworkAvailable(activity)){
            if(noInternetDialog.isVisible())
                noInternetDialog.dismiss();
        }else{
            if(!noInternetDialog.isVisible())
            noInternetDialog.show(fragmentManager,"kjfdj");
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            Log.e(TAG, "onAvailable: " );
            if(noInternetDialog.isVisible())
            noInternetDialog.dismiss();
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            noInternetDialog.show(fragmentManager,"kjfdj");
            Log.e(TAG, "onLost: " );
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
            Log.e(TAG, "onCapabilitiesChanged: unmetered "+unmetered);
        }
    };
    public void startInternetCheck(){
        ConnectivityManager connectivityManager =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager = (ConnectivityManager) activity.getSystemService(ConnectivityManager.class);
        }
        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }
}
