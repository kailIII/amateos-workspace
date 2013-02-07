package com.emp.friskyplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.emp.friskyplayer.utils.ServiceActionConstants;

public class PhoneReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		
		PhoneStateListener phoneListener=new PhoneStateListener(){

			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				super.onCallStateChanged(state, incomingNumber);
				if(state != TelephonyManager.CALL_STATE_IDLE){
					context.startService(new Intent(ServiceActionConstants.ACTION_STOP));
				}
			}
			
		};
	    TelephonyManager telephony = (TelephonyManager) 
	    context.getSystemService(Context.TELEPHONY_SERVICE);
	    telephony.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
	}

}
