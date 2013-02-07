package com.emp.friskyplayer.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.emp.friskyplayer.utils.ConnectionUtils;
import com.emp.friskyplayer.utils.PlayerConstants;
import com.emp.friskyplayer.utils.PreferencesConstants;
import com.emp.friskyplayer.utils.SleepModeManager;

public class FriskyPlayerApplication extends Application {

	final static String TAG = "FriskyPlayerApplication";

	private FriskyPlayerApplication application;
	private SharedPreferences mPrefs;
	private int playerState;
	private String streamTitle;
	private String quality;

	private SleepModeManager sleepModeManager;
	private boolean sleepModeEnabled;

	@Override
	public void onCreate() {
		super.onCreate();

		application = this;

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		// Sets playerState to STOPPED by default
		playerState = PlayerConstants.STATE_STOPPED;

		// Sets streamTitle to "" by default
		streamTitle = "";

		// Sets quality
		quality = mPrefs.getString(PreferencesConstants.QUALITY,
				PreferencesConstants.QUALITY_HQ);

		// Sets Network connection type
		Editor editor = mPrefs.edit();
		editor.putString(PreferencesConstants.CONNECTION_TYPE,
				ConnectionUtils.getNetworkType(this));

		// Sets Sleep Mode to off by default
		editor.putBoolean(PreferencesConstants.SLEEP_MODE, false);
		editor.commit();
		
		sleepModeManager = new SleepModeManager(getApplicationContext());
		sleepModeEnabled = false;
	}

	public FriskyPlayerApplication getInstance() {
		return application;
	}

	public synchronized int getPlayerState() {
		return playerState;
	}

	public synchronized void setPlayerState(int playerState) {
		this.playerState = playerState;
		Editor editor = mPrefs.edit();
		editor.putInt(PreferencesConstants.PLAYER_STATE, playerState);
		editor.commit();
	}

	public String getStreamTitle() {
		return streamTitle;
	}

	public void setStreamTitle(String streamTitle) {
		this.streamTitle = streamTitle;
		Editor editor = mPrefs.edit();
		editor.putString(PreferencesConstants.STREAMING_TITLE, streamTitle);
		editor.commit();
	}

	public void onQualityPrefChange() {
		quality = mPrefs.getString(PreferencesConstants.QUALITY,
				PreferencesConstants.QUALITY_HQ);
	}

	public String getStreamingUrl() {
		String url = PreferencesConstants.STREAMING_URL_HQ;
		if (quality.equals(PreferencesConstants.QUALITY_HQ)) {
			url = mPrefs.getString(PreferencesConstants.STREAMING_URL,
					PreferencesConstants.STREAMING_URL_HQ);
		}
		if (quality.equals(PreferencesConstants.QUALITY_LQ)) {
			url = mPrefs.getString(PreferencesConstants.STREAMING_URL,
					PreferencesConstants.STREAMING_URL_LQ);
		}
		return url;
	}

	public String getQuality() {
		return quality;
	}

	public SleepModeManager getSleepModeManager() {
		return sleepModeManager;
	}

	/**
	 * Enables sleep mode
	 */
	public void enableSleepMode() {
		int countdownValue = mPrefs.getInt(PreferencesConstants.SLEEP_MODE_TIMEOUT, 30);
		sleepModeEnabled = sleepModeManager.enableSleepMode(countdownValue);

	}
	
	/**
	 * Disables sleep mode
	 */
	public void disableSleepMode(){
		Editor editor = mPrefs.edit();
		editor.putBoolean(PreferencesConstants.SLEEP_MODE, false);
		editor.commit();
		sleepModeEnabled = sleepModeManager.disableSleepMode();
	}

	public boolean isSleepModeEnabled() {
		return sleepModeEnabled;
	}

}
