package com.codelite.kr4k3rz.kotha6;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

 public  class UtilHelper {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
