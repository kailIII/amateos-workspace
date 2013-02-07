package com.emp.friskyplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.emp.friskyplayer.utils.ConnectionUtils;
import com.emp.friskyplayer.utils.PreferencesConstants;
import com.emp.friskyplayer.utils.ServiceActionConstants;

public class ConnectionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getExtras() != null) {

			NetworkInfo ni = (NetworkInfo) intent.getExtras().get(
					ConnectivityManager.EXTRA_NETWORK_INFO);

			// Network connected
			if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {

				Log.i("Connection", "Network " + ni.getTypeName()
						+ " connected");
				String oldConnectionType = PreferenceManager
						.getDefaultSharedPreferences(context).getString(
								PreferencesConstants.CONNECTION_TYPE,
								ConnectionUtils.NETWORK_TYPE_NO_CONNECTION);

				// Connection type has changed
				if (!ni.getTypeName().equalsIgnoreCase(oldConnectionType)) {
					
					SharedPreferences.Editor editor = PreferenceManager
							.getDefaultSharedPreferences(context).edit();
					editor.putString(PreferencesConstants.CONNECTION_TYPE,
							ni.getTypeName());
					editor.commit();
					
				}
				
				context.startService(new Intent(
						ServiceActionConstants.ACTION_STOP));
			}

			// No network
			if (intent.getExtras().getBoolean(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.TRUE)) {
				
				Log.d("Connection", "There's no network connectivity");
				SharedPreferences.Editor editor = PreferenceManager
						.getDefaultSharedPreferences(context).edit();
				editor.putString(PreferencesConstants.CONNECTION_TYPE,
						ConnectionUtils.NETWORK_TYPE_NO_CONNECTION);
				editor.commit();
				
				context.startService(new Intent(
						ServiceActionConstants.ACTION_STOP));
			}
		}

	}

}
