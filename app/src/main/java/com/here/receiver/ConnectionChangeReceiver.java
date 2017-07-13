package com.here.receiver;

/**
 * Created by hyc on 2017/3/30 18:02
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.here.util.ImUtil;
import com.here.util.NetworkState;


/**
 * @author Javen
 *
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //网络不可用
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            NetworkState.netIsActivity =false;
            Log.i("网络","没有网络");
        }else {//网络可用
            Log.i("网络","是否连接"+ImUtil.isConnected);
            NetworkState.netIsActivity = true;
            if (!ImUtil.isConnected){
                Log.i("网络","有网络开始连接");
                ImUtil.connectServer();
            }
        }
    }

}