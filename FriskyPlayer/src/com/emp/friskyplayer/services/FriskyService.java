package com.emp.friskyplayer.services;

/*   
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.FriskyPlayerActivity;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.audio.AudioFocusHelper;
import com.emp.friskyplayer.audio.MusicFocusable;
import com.emp.friskyplayer.player.FriskyPlayerCallback;
import com.emp.friskyplayer.utils.PlayerUtils;
import com.spoledge.aacdecoder.AACPlayer;

/**
 * Service that handles media playback.
 */

public class FriskyService extends Service implements MusicFocusable {

	// The tag we put on debug messages
	private final static String TAG = "FriskyService";

	// Intent actions that service can handle
	public static final String ACTION_TOGGLE_PLAYBACK = "com.emp.friskyplayer.services.action.TOGGLE_PLAYBACK";
	public static final String ACTION_PLAY = "com.emp.friskyplayer.services.action.PLAY";
	public static final String ACTION_STOP = "com.emp.friskyplayer.services.action.STOP";
	public static final String STREAM_TITLE = "com.emp.friskyplayer.services.action.STREAM_TITLE";
	public static final String GET_STREAM_TITLE = "com.emp.friskyplayer.services.action.GET_STREAM_TITLE";
	public static final String STATE = "com.emp.friskyplayer.services.action.STATE";
	public static final String GET_STATE = "com.emp.friskyplayer.services.action.GET_STATE";


	// The volume we set the media player to when we lose audio focus, but are
	// allowed to reduce
	// the volume instead of stopping playback.
	public static final float DUCK_VOLUME = 0.1f;

	// Player
	private AACPlayer mPlayer = null;

	// our AudioFocusHelper object, if it's available (it's available on SDK
	// level >= 8)
	// If not available, this will be null. Always check for null before using!
	AudioFocusHelper mAudioFocusHelper = null;

	// State of player
	int mState;

	// why did we pause? (only relevant if mState == State.Paused)
	int mPauseReason = PlayerUtils.USER_REQUEST;

	int mAudioFocus = PlayerUtils.NO_FOCUS_NO_DUCK;

	// Wifi lock that we hold when streaming files from the internet, in order
	// to prevent the
	// device from shutting off the Wifi radio
	WifiLock mWifiLock;

	AudioManager mAudioManager;

	// Notifications
	final int NOTIFICATION_ID = 1;
	NotificationManager mNotificationManager;
	Notification mNotification = null;

	/**
	 * Makes sure the player exists and has been reset. This will create the
	 * player if needed, or reset the existing media player if one already
	 * exists.
	 */
	// TODO: adaptar a aacplayer
	void createPlayer() {
		// TODO: agregar al mplayer los valores de los buffers en el constructor
		mPlayer = new AACPlayer(new FriskyPlayerCallback(
				getApplicationContext(), this));
	}

	@Override
	public void onCreate() {

		// Gets state from Application object
		mState = ((FriskyPlayerApplication) getApplication()).getInstance().getPlayerState();
		
		// Create the Wifi lock (this does not acquire the lock, this just
		// creates it)
		mWifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
				.createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

		// create the Audio Focus Helper, if the Audio Focus feature is
		// available (SDK 8 or above)
		if (android.os.Build.VERSION.SDK_INT >= 8)
			mAudioFocusHelper = new AudioFocusHelper(getApplicationContext(),
					this);
		else
			mAudioFocus = PlayerUtils.FOCUSED; // no focus feature, so we always
												// "have" audio focus
		
	}

	/**
	 * Called when we receive an Intent. When we receive an intent sent to us
	 * via startService(), this is the method that gets called. So here we react
	 * appropriately depending on the Intent's action, which specifies what is
	 * being requested of us.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		String action = intent.getAction();
		Log.d("button", "Recibido Intent en Service -> "+action);		
		if (action.equals(ACTION_TOGGLE_PLAYBACK))
			processTogglePlaybackRequest();
		else if (action.equals(ACTION_PLAY))
			processPlayRequest();
		else if (action.equals(ACTION_STOP))
			processStopRequest();

		return START_NOT_STICKY; // Means we started the service, but don't want
									// it to
									// restart in case it's killed.
	}

	void processTogglePlaybackRequest() {
		
		mState = ((FriskyPlayerApplication) getApplication()).getInstance().getPlayerState();

		if (mState == PlayerUtils.STATE_STOPPED) {
			processPlayRequest();
		} else {
			processStopRequest();
		}
	}

	void processPlayRequest() {

		tryToGetAudioFocus();

		// actually play the song
		mState = ((FriskyPlayerApplication) getApplication()).getInstance().getPlayerState();

		if (mState == PlayerUtils.STATE_STOPPED) {
			// If player has stoped state, play streaming
			playStreaming();
		}
	}

	void processStopRequest() {
		processStopRequest(false);
	}

	void processStopRequest(boolean force) {
		
		mState = ((FriskyPlayerApplication) getApplication()).getInstance().getPlayerState();
		
		if (mState == PlayerUtils.STATE_PLAYING
				|| mState == PlayerUtils.STATE_LOADING || force) {
			
			// Sets state of player to STOPPED
			((FriskyPlayerApplication) getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_STOPPED);
			
			//TODO: borrar la linea de debajo
			//mState = PlayerUtils.STATE_STOPPED;

			// let go of all resources...
			relaxResources(true);
			giveUpAudioFocus();

			// service is no longer necessary. Will be started again if needed.
			stopSelf();
		}
	}

	/**
	 * Releases resources used by the service for playback. This includes the
	 * "foreground service" status and notification, the wake locks and possibly
	 * the MediaPlayer.
	 * 
	 * @param releasePlayer
	 *            Indicates whether the Media Player should also be released or
	 *            not
	 */
	@TargetApi(5)
	public// TODO: adaptar a aacplayer
	void relaxResources(boolean releasePlayer) {
		// stop being a foreground service
		stopForeground(true);

		// stop and release the Player, if it's available
		if (releasePlayer && mPlayer != null) {
			mPlayer.stop();
			mPlayer = null;
		}

		// also release the Wifi lock, if we're holding it
		if (mWifiLock.isHeld())
			mWifiLock.release();
	}

	public void giveUpAudioFocus() {
		if (mAudioFocus == PlayerUtils.FOCUSED && mAudioFocusHelper != null
				&& mAudioFocusHelper.abandonFocus())
			mAudioFocus = PlayerUtils.NO_FOCUS_NO_DUCK;
	}

	/**
	 * Reconfigures Player according to audio focus settings and
	 * starts it. This method starts/restarts the MediaPlayer
	 * respecting the current audio focus state. So if we have focus, it will
	 * play normally; if we don't have focus, it will either leave the
	 * Player paused or set it to a low volume, depending on what is
	 * allowed by the current focus settings. This method assumes mPlayer !=
	 * null
	 */
	// TODO: configurar con las preferencias!
	void configAndStartPlayer() {
		
		mState = ((FriskyPlayerApplication) getApplication()).getInstance().getPlayerState();
		
		if (mState != PlayerUtils.STATE_PLAYING)
			mPlayer.playAsync("http://205.188.215.229:8024");

	}

	void tryToGetAudioFocus() {
		if (mAudioFocus != PlayerUtils.FOCUSED && mAudioFocusHelper != null
				&& mAudioFocusHelper.requestFocus())
			mAudioFocus = PlayerUtils.FOCUSED;
	}

	/**
	 * Starts playing
	 */
	void playStreaming() {
		//mState = PlayerUtils.STATE_STOPPED;
		((FriskyPlayerApplication) getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_STOPPED);

		relaxResources(false); // release everything except Player

		createPlayer();

		// change state to loading
		
		((FriskyPlayerApplication) getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_STOPPED);

		//mState = PlayerUtils.STATE_LOADING;

		setUpAsForeground(getResources().getString(R.string.loading));

		//TODO: aqu’ habr’a que llamar al configAndStart
		mPlayer.playAsync("http://205.188.215.229:8024");

		// Hold a Wifi lock,
		// which prevents
		// the Wifi radio from going to sleep while the song is playing
		mWifiLock.acquire();

	}


	/** 
	 * Updates the notification. 
	 */
	public void updateNotification(String text) {
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
				0, new Intent(getApplicationContext(),
						FriskyPlayerActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.setLatestEventInfo(getApplicationContext(),
				"friskyPlayer", text, pi);
		mNotificationManager.notify(NOTIFICATION_ID, mNotification);
	}

	/**
	 * Configures service as a foreground service.
	 */
	@TargetApi(5)
	void setUpAsForeground(String text) {
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
				0, new Intent(getApplicationContext(),
						FriskyPlayerActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification = new Notification();
		mNotification.tickerText = text;
		mNotification.icon = R.drawable.ic_stat_notification;
		mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
		mNotification.setLatestEventInfo(getApplicationContext(),
				"friskyPlayer", text, pi);
		startForeground(NOTIFICATION_ID, mNotification);
	}

	
	public void onGainedAudioFocus() {
		
		Log.d(TAG,"Gained audio focus");
		mAudioFocus = PlayerUtils.FOCUSED;

		mState = ((FriskyPlayerApplication) getApplication()).getInstance().getPlayerState();
		
		// restart media player with new focus settings
		if (mState == PlayerUtils.STATE_PLAYING)
			configAndStartPlayer();
	}

	public void onLostAudioFocus(boolean canDuck) {
		Toast.makeText(getApplicationContext(),
				"lost audio focus." + (canDuck ? "can duck" : "no duck"),
				Toast.LENGTH_SHORT).show();
		mAudioFocus = canDuck ? PlayerUtils.NO_FOCUS_CAN_DUCK
				: PlayerUtils.NO_FOCUS_NO_DUCK;

		// start/restart/pause media player with new focus settings
		// if (mPlayer != null && mPlayer.isPlaying())
		
		mState = ((FriskyPlayerApplication) getApplication()).getInstance().getPlayerState();
		
		if (mPlayer != null && mState == PlayerUtils.STATE_PLAYING)
			configAndStartPlayer();
	}

	@Override
	public void onDestroy() {
		// Service is being killed, so make sure we release our resources
		
		((FriskyPlayerApplication) getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_STOPPED);

		//TODO: borrar la linea de debajo
		//mState = PlayerUtils.STATE_STOPPED;
		relaxResources(true);
		giveUpAudioFocus();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void changeState(int newState) {
		mState = newState;
	}

}
