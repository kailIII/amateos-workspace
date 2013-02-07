package com.emp.friskyplayer.receivers;

import com.emp.friskyplayer.utils.ServiceActionConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Manages lock screen media button actions
 * @author empollica
 *
 */
public class MediaButtonReceiver extends BroadcastReceiver {

	final static String TAG = "MediaButton";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
			
			Log.d(TAG,"Media button pressed");
			
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;

            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                	Log.d(TAG,"KEYCODE_HEADSETHOOK");
                    context.startService(new Intent(ServiceActionConstants.ACTION_TOGGLE_PLAYBACK));
                	break;
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                	Log.d(TAG,"KEYCODE_MEDIA_PLAY_PAUSE");
                	// send an intent to FriskyService to telling it to stop the audio
                    context.startService(new Intent(ServiceActionConstants.ACTION_STOP));
                    break;
            }
		}
		
	}

}
