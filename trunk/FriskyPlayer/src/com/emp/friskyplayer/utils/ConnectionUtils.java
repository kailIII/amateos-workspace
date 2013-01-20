package com.emp.friskyplayer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtils {
	
	/**
	 * Network type constants
	 */
	public static String NETWORK_TYPE_MOBILE = "mobile";
	public static String NETWORK_TYPE_WIFI = "WIFI";
	public static String NETWORK_TYPE_NO_CONNECTION = "no_connection";
	
	/**
	 * Checks network connection
	 * @param context
	 * @return
	 */
	public static boolean haveNetworkConnection(Context context) {

	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()
                     && cm.getActiveNetworkInfo().isAvailable()
                     && cm.getActiveNetworkInfo().isConnected()) {
               return true;
        }
        return false;
	}
	
	/**
	 * Checks network connection
	 * @param context
	 * @return
	 */
	public static String getNetworkType(Context context) {

		String netWorkType = NETWORK_TYPE_NO_CONNECTION;
		
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();

	    if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
	    	if(netInfo.getTypeName().equalsIgnoreCase(NETWORK_TYPE_MOBILE)){
	    		netWorkType = NETWORK_TYPE_MOBILE;
	    	}else if(netInfo.getTypeName().equalsIgnoreCase(NETWORK_TYPE_WIFI)){
	    		netWorkType = NETWORK_TYPE_WIFI;
	    	}
	    }
		return netWorkType;
	}
	
	
}
